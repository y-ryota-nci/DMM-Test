package jp.co.nci.iwf.endpoint.mm.mm0003;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.JobType;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmPost;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfcPost;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserHasMenuRoleCodeInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.CodeBook.MenuRoleCodes;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.AuthenticateRepository;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.Range;

/**
 * ユーザ編集画面のバリデーション
 */
@ApplicationScoped
public class Mm0003Validator extends MiscUtils {
	@Inject private WfInstanceWrapper wf;
	@Inject private I18nService i18n;
	@Inject AuthenticateRepository repository;
	@Inject SessionHolder sessionHolder;

	/** 未入力を表す値 */
	private static final String EMPTY = "0";

	/**
	 * ユーザ更新用のバリデーション
	 * @param user
	 * @return
	 */
	public List<String> validateUpdateUser(WfmUser user) {
		final List<String> alerts = new ArrayList<>();

		if (isEmpty(user.getUserAddedInfo()))
			alerts.add(i18n.getText(MessageCd.MSG0001, MessageCd.userAddedInfo));
		if (isEmpty(user.getUserName()))
			alerts.add(i18n.getText(MessageCd.MSG0001, MessageCd.userName));
		if (isEmpty(user.getValidStartDate()))
			alerts.add(i18n.getText(MessageCd.MSG0001, MessageCd.validStartDate));
		if (isEmpty(user.getValidEndDate()))
			alerts.add(i18n.getText(MessageCd.MSG0001, MessageCd.validEndDate));
		if (compareTo(user.getValidStartDate(), user.getValidEndDate()) > 0)
			alerts.add(i18n.getText(MessageCd.MSG0025, MessageCd.validStartDate, MessageCd.validEndDate));

		return alerts;
	}

	/**
	 * ユーザ所属用のバリデーション
	 * @param req
	 * @return
	 */
	public List<String> validateUpdateUserBelong(Mm0003UpdateUserRequest req) {
		final List<String> alerts = new ArrayList<>();
		final WfmUser user = req.user;
		final List<WfcUserBelong> merges = new ArrayList<>(req.belongs);
		final Date today = today();

		// すでに削除済みならチェック不要
		if (eq(DeleteFlag.ON, user.getDeleteFlag())) {
			return alerts;
		}

		// 本日時点での生きている所属を生成し、それに対してバリデーションを行う
		boolean existsJobTypeMain = false;	// 主務があるか？
		int rowNo = 1;
		for (Iterator<WfcUserBelong> it = merges.iterator(); it.hasNext(); rowNo++) {
			final WfcUserBelong ub = it.next();

			// 有効期間は必須
			if (isEmpty(ub.getValidStartDate()))
				alerts.add(i18n.getText(MessageCd.MSG0001, MessageCd.validStartDate));
			if (isEmpty(ub.getValidEndDate()))
				alerts.add(i18n.getText(MessageCd.MSG0001, MessageCd.validEndDate));
			if (compareTo(ub.getValidStartDate(), ub.getValidEndDate()) > 0)
				alerts.add(i18n.getText(MessageCd.MSG0025, MessageCd.validStartDate, MessageCd.validEndDate));

			// 組織と役職は必須である。
			// IWFでは、NOT NULL制約があるカラムで未設定を表現するために '0'を設定しているので、値＝'0'は未入力扱いせねばならぬ
			if (isEmpty(ub.getOrganizationCode()) || eq(EMPTY, ub.getOrganizationCode()))
				alerts.add(i18n.getText(MessageCd.MSG0002, MessageCd.organization, rowNo));
			if (isEmpty(ub.getPostCode()) || eq(EMPTY, ub.getPostCode()))
				alerts.add(i18n.getText(MessageCd.MSG0002, MessageCd.post, rowNo));

			// 組織の存在チェック
			if (!existsOrganization(ub.getCorporationCode(), ub.getOrganizationCode())) {
				String field = i18n.getText(MessageCd.organization) + "=" + ub.getOrganizationName();
				alerts.add(i18n.getText(MessageCd.MSG0156, field));
			}
			// 役職の存在チェック
			if (!existsPost(ub.getCorporationCode(), ub.getPostCode())) {
				String field = i18n.getText(MessageCd.post) + "=" + ub.getPostName();
				alerts.add(i18n.getText(MessageCd.MSG0156, field));
			}

			// 主務があるか？
			existsJobTypeMain |= JobType.MAIN.equals(ub.getJobType());

			// 同一部署かつ同一役職で、有効期間が重複していないか
			String error = validateDuplicatedTerm(req.belongs, ub);
			if (isNotEmpty(error))
				alerts.add(error);
		}

		// 有効なユーザは主務があること
		if (compareTo(today, user.getValidEndDate()) <= 0 && !existsJobTypeMain) {
			alerts.add(i18n.getText(MessageCd.MSG0099));
		}

		// ユーザの有効期間中で、組織／役職に所属してない期間がないこと
		String error = validateUserTerm(req.belongs, user);
		if (isNotEmpty(error))
			alerts.add(error);

		return alerts;
	}

	/** ユーザの有効期間中で、組織／役職に所属してない期間がないこと */
	private String validateUserTerm(List<WfcUserBelong> belongs, final WfmUser user) {
		// まだ終了日を迎えていない所属だけを集めて、有効期間開始日でソート
		final Date today = today();
		final List<WfcUserBelong> sorted = new ArrayList<>(belongs).stream()
				.filter(ub -> compareTo(today, ub.getValidEndDate()) <= 0)
				.sorted((ub1, ub2) -> compareTo(ub1.getValidStartDate(), ub2.getValidStartDate()))
				.collect(Collectors.toList());

		// ユーザ所属の総有効期間（所属の各レコードの有効期間を連結していったもの）
		Range<Date> rSummary = null;

		for (WfcUserBelong ub : sorted) {
			// 組織・役職の有効期間と、所属の有効期間の整合性チェック（まだ終了日を迎えていない所属のみ）
			if (compareTo(today, ub.getValidEndDate()) <= 0) {
				// 過去データに関しては移行データ等でクソッタレなことが多々あって整合性の担保が難しいので、
				// 過去データの開始日を本日に読み替えて整合性確認とする
				final Date start = max(today, ub.getValidStartDate());
				final Date end = max(today, ub.getValidEndDate());

				// 組織の有効期間が、所属の有効期間を包含すること
				final WfcOrganization org = getWfmOrganization(ub);
				if (org != null && !include(org.getValidStartDate(), org.getValidEndDate(), start, end)) {
					String field = i18n.getText(MessageCd.organization) + "=" + org.getOrganizationName();
					return i18n.getText(MessageCd.MSG0199, field, MessageCd.userBelongInfo);
				}
				// 役職の有効期間が、所属の有効期間を包含すること
				final WfmPost post = getWfmPost(ub);
				if (post != null && !include(org.getValidStartDate(), org.getValidEndDate(), start, end)) {
					String field = i18n.getText(MessageCd.post) + "=" + post.getPostName();
					return i18n.getText(MessageCd.MSG0199, field, MessageCd.userBelongInfo);
				}
			}

			// ユーザ所属の総所属期間を求める
			final Range<Date> r = new Range<>(ub.getValidStartDate(), ub.getValidEndDate());
			if (rSummary == null) {
				rSummary = r;
			}
			else {
				// 総有効期間の開始日がこの所属の有効期間内であれば、連続しているものとみなして開始日を伸ばす
				if (between(addDay(rSummary.from, -1), r.from, r.to))
					rSummary.from = min(rSummary.from, r.from);

				// 総有効期間の終了日がこの所属の有効期間内であれば、連続しているものとみなして終了日を伸ばす
				if (between(addDay(rSummary.to, 1), r.from, r.to))
					rSummary.to = max(rSummary.to, r.to);
			}
		}
		// ユーザの有効期間中で、所属してない期間がないこと。
		// つまり「sum(ユーザ所属.有効期間) ⊇ ユーザマスタ.有効期間」であること。
		// ただし、過去データに関しては移行データがクソッタレなことが多々あって整合性の担保が難しいので、
		// 過去データの開始日を本日に読み替えて整合性確認とする
		final Range<Date> rUser = new Range<>(
				max(today, user.getValidStartDate()), max(today, user.getValidEndDate()));
		if (!include(rSummary, rUser)) {
			final int modify = (compareTo(rSummary.to, rUser.to) < 0 ? 1 : -1);
			final String date = toStr(addDay(rSummary.to, modify));
			return i18n.getText(MessageCd.MSG0198, date);
		}
		return null;
	}

	private WfmPost getWfmPost(WfcUserBelong ub) {
		SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(ub.getCorporationCode());
		in.setPostCode(ub.getPostCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		List<WfcPost> list = wf.searchWfmPost(in).getPostList();
		return list.isEmpty() ? null : list.get(0);
	}

	private WfcOrganization getWfmOrganization(WfcUserBelong ub) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(ub.getCorporationCode());
		in.setOrganizationCode(ub.getOrganizationCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		List<WfcOrganization> list = wf.searchWfmOrganization(in).getOrganizationList();
		return list.isEmpty() ? null : list.get(0);
	}

	/** 同一部署かつ同一役職で、有効期間が重複していないか */
	private String validateDuplicatedTerm(List<WfcUserBelong> belongs, final WfcUserBelong ub) {
		for (WfcUserBelong inner : belongs) {
			if (inner != ub
					&& !eq(ub.getSeqNoUserBelong(), inner.getSeqNoUserBelong())
					&& eq(ub.getCorporationCode(), inner.getCorporationCode())
					&& eq(ub.getUserCode(), inner.getUserCode())
					&& eq(ub.getOrganizationCode(), inner.getOrganizationCode())
					&& eq(ub.getPostCode(), inner.getPostCode())) {
				Range<Date> r1 = new Range<>(ub.getValidStartDate(), ub.getValidEndDate());
				Range<Date> r2 = new Range<>(inner.getValidStartDate(), inner.getValidEndDate());
				if (overlap(r1, r2)) {
					// {0}「{1}」は{2}が重複しています。
					String belong = i18n.getText(MessageCd.userBelongInfo);
					String detail = ub.getOrganizationName() + " " + ub.getPostName();
					String period = i18n.getText(MessageCd.validTerm);
					return i18n.getText(MessageCd.MSG0188, belong, detail, period);
				}
			}
		}
		return null;
	}


	/** ユーザ削除時のバリデーション */
	public String validateDelete(WfmUser user) {
		// ユーザの所有するメニューロール
		final List<WfvUserBelongImpl> belongs = getWfvUserBelongList(
				user.getCorporationCode(), user.getUserAddedInfo());
		final Set<String> menuRoleCodes = repository.getMenuRoleCds(belongs);

		// 対象者が管理者ロールを持ち、かつそのロールを持つ他者がいない時は
		// システム管理不能状態になるため、対象者を削除できない
		final Map<String, MessageCd> conds = new HashMap<>();
		conds.put(MenuRoleCodes.ASP, MessageCd.aspAdmin);			// ASP管理者
		conds.put(MenuRoleCodes.CorpAdmin, MessageCd.corpAdmin);	// 企業管理者
		conds.put(MenuRoleCodes.GroupAdmin, MessageCd.groupAdmin);	// グループ企業管理者
		final java.sql.Date baseDate = today();	// 未来のどこかではなく、今日そのロールを持っているのはだれかを調べる
		for (String menuRoleCode : conds.keySet()) {
			if (menuRoleCodes.contains(menuRoleCode)) {
				if (countUserHasMenuRoleCode(menuRoleCode, baseDate) <= 1) {
					MessageCd target = conds.get(menuRoleCode);
					return i18n.getText(MessageCd.MSG0195, toStr(baseDate), target);
				}
			}
		}
		return null;
	}

	/** 指定されたメニューロールコードを持つユーザ数を返す */
	private int countUserHasMenuRoleCode(String menuRoleCode, java.sql.Date baseDate) {
		final SearchWfmUserHasMenuRoleCodeInParam in = new SearchWfmUserHasMenuRoleCodeInParam();
		in.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setMenuRoleCode(menuRoleCode);
		in.setBaseDate(baseDate);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		List<WfmUser> users = wf.searchWfmUserHasMenuRoleCode(in).getUserList();
		return (users == null || users.isEmpty() ? 0 : users.size());
	}

	/** ユーザ所属ビュー抽出 */
	private List<WfvUserBelongImpl> getWfvUserBelongList(String corporationCode, String userAddedInfo) {
		final java.sql.Date today = today();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(corporationCode);
		in.setUserAddedInfo(userAddedInfo);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today);
		in.setValidEndDateOrganization(today);
		in.setValidStartDatePost(today);
		in.setValidEndDatePost(today);
		in.setValidStartDateUser(today);
		in.setValidEndDateUser(today);
		in.setValidStartDateUserBelong(today);
		in.setValidEndDateUserBelong(today);
		in.setOrderBy(new OrderBy[] {new OrderBy(true, WfcUserBelong.JOB_TYPE)});
		return wf.searchWfvUserBelong(in).getUserBelongList();
	}

	/** 組織の存在チェック */
	private boolean existsOrganization(String corporationCode, String organizationCode) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		return wf.searchWfmOrganization(in).getOrganizationList().size() > 0;
	}

	/** 役職の存在チェック */
	private boolean existsPost(String corporationCode, String postCode) {
		final SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(corporationCode);
		in.setPostCode(postCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		return wf.searchWfmPost(in).getPostList().size() > 0;
	}
}
