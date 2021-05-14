package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmCorporationInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmMenuRoleDetailInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmMenuRoleInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmPostInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserBelongInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmUserPasswordInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmCorporationOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmMenuRoleDetailOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmMenuRoleOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmOrganizationOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmPostOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmUserBelongOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmUserOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmUserPasswordOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.AdministratorType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.JobType;
import jp.co.nci.integrated_workflow.common.CodeMaster.LocaleCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.LockFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleAssignmentType;
import jp.co.nci.integrated_workflow.common.CodeMaster.MenuRoleType;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.integrated_workflow.model.base.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.base.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.model.base.WfmOrganization;
import jp.co.nci.integrated_workflow.model.base.WfmPost;
import jp.co.nci.integrated_workflow.model.base.WfmUser;
import jp.co.nci.integrated_workflow.model.base.WfmUserBelong;
import jp.co.nci.integrated_workflow.model.base.WfmUserPassword;
import jp.co.nci.integrated_workflow.model.base.impl.WfmMenuRoleDetailImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WfmMenuRoleImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WfmOrganizationImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WfmPostImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WfmUserBelongImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WfmUserImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WfmUserPasswordImpl;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfcPost;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchOrganizationUpInParam;
import jp.co.nci.integrated_workflow.param.input.SearchSubOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.endpoint.mm.mm0002.Mm0002Service;
import jp.co.nci.iwf.endpoint.mm.mm0500.Mm0500Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * プロファイル管理画面サービス
 */
@BizLogic
public class Mm0000Service extends MmBaseService<Object> implements Mm0000CodeBook {
	@Inject private WfInstanceWrapper wf;
	@Inject private CorporationPropertyService corpProperty;
	/** マスタ初期値設定サービス */
	@Inject private Mm0500Service mm0500Service;
	@Inject private Mm0000Repository repository;
	/** 組織編集サービス */
	@Inject private Mm0002Service mm0002service;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0000InitResponse init(Mm0000InitRequest req) {
		final Mm0000InitResponse res = createResponse(Mm0000InitResponse.class, req);

		// 基準日
		res.baseDate = defaults(req.baseDate, today());
		// 有効データのみ表示
		res.displayValidOnly = defaults(req.displayValidOnly, Boolean.TRUE);
		// 初期表示で展開しておく企業と組織のノードIDをリスト化
		res.nodeIds = getNodeIds(req.corporationCode, req.organizationCode, res.baseDate, res.displayValidOnly);

		res.success = true;
		return res;
	}

	/**
	 * 初期表示で展開しておく企業と組織のノードIDをリスト化
	 * @param corporationCode 企業コード
	 * @param organizationCode 表示しておく組織の組織コード
	 * @param baseDate 基準日
	 * @param displayValidOnly
	 * @return
	 */
	private List<String> getNodeIds(String corporationCode, String organizationCode, java.sql.Date baseDate, Boolean displayValidOnly) {
		final List<String> nodeIds = new ArrayList<>();
		if (isNotEmpty(corporationCode) && isNotEmpty(organizationCode)) {
			// 企業ノード
			List<WfmCorporation> corps = getWfmCorporation(corporationCode, displayValidOnly);
			if (corps != null && !corps.isEmpty()) {
				final WfmCorporation c = corps.get(0);
				nodeIds.add(new Mm0000TreeItem(c).id);

				// 指定された組織が削除済みなら、その親組織を使う
				final WfmOrganization org = getWfmOrganization(corporationCode, organizationCode, baseDate);
				if (org != null && DeleteFlag.ON.equals(org.getDeleteFlag())) {
					organizationCode = org.getOrganizationCodeUp();
				}

				// 組織ノードをリスト化
				if (org != null) {
					nodeIds.addAll(getWfmOrganizationUp(corporationCode, organizationCode, baseDate, displayValidOnly)
							.stream()
							.map(o -> new Mm0000TreeItem(o).id)
							.collect(Collectors.toList()));
				}
			}
		}
		return nodeIds;
	}

	/**
	 * 受信パラメータのノードIDを直接親のIDへ変換
	 * @param nodeId
	 * @return
	 */
	private Parent parseParent(String nodeId) {
		// 「/」で区切った階層の、最後の要素が直接親
		final String[] parentIds = nodeId.split("/");
		Parent entry = null;
		if (parentIds.length > 0) {
			final String parentId = parentIds[parentIds.length - 1];
			final String[] ids = parentId.split("@");
			if (ids.length < 2) {
				throw new BadRequestException("フォーマットに誤りがあります");
			}
			else if (ids.length == 2){
				String corporationCode = ids[0];
				Long timestampUpdated = Long.valueOf(ids[1]);
				entry = new Parent(corporationCode, timestampUpdated);
			}
			else if (ids.length == 3){
				String corporationCode = ids[0];
				String organizationCode = ids[1];
				Long timestampUpdated = Long.valueOf(ids[2]);
				entry = new Parent(corporationCode, organizationCode, timestampUpdated);
			}
			else if (ids.length == 4) {
				String corporationCode = ids[0];
				String organizationCode = ids[1];
				Long timestampUpdated = Long.valueOf(ids[3]);
				entry = new Parent(corporationCode, organizationCode, timestampUpdated);
			}
		}
		return entry;
	}

	/**
	 * 対象者のユーザ所属を抽出
	 * @param corporationCode
	 * @param organizationCode
	 * @param baseDate
	 * @return
	 */
	private List<WfvUserBelongImpl> getWfmUserBelong(String corporationCode, String organizationCode, Date baseDate, Boolean displayValidOnly) {
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setValidStartDateOrganization(baseDate);
		in.setValidEndDateOrganization(baseDate);
		in.setValidStartDateUser(baseDate);
		in.setValidEndDateUser(baseDate);
//		in.setValidStartDatePost(baseDate);		// 初期登録のユーザには役職が未設定
//		in.setValidEndDatePost(baseDate);		// 初期登録のユーザには役職が未設定
		in.setValidStartDateUserBelong(baseDate);
		in.setValidEndDateUserBelong(baseDate);
		if (displayValidOnly != null && displayValidOnly.booleanValue()) {
			in.setDeleteFlagUser(DeleteFlag.OFF);
			in.setDeleteFlagUserBelong(DeleteFlag.OFF);
			in.setDeleteFlagOrganization(DeleteFlag.OFF);
//			in.setDeleteFlagPost(DeleteFlag.OFF);	// 初期登録のユーザには役職が未設定
		} else {
			// 削除区分系はすべて検索初期値が[0]有効が立っているので、明示的にNULLをセットして絞込条件から外す
			in.setDeleteFlagUser(null);
			in.setDeleteFlagUserBelong(null);
			in.setDeleteFlagOrganization(null);
			in.setDeleteFlagPost(null);
		}
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, WfvUserBelong.CORPORATION_CODE),
				new OrderBy(true, WfvUserBelong.USER_ADDED_INFO),
				new OrderBy(true, WfvUserBelong.USER_CODE),
				new OrderBy(true, WfvUserBelong.POST_LEVEL),
				new OrderBy(true, WfvUserBelong.ORGANIZATION_ADDED_INFO),
				new OrderBy(true, WfvUserBelong.ORGANIZATION_CODE),
		});
		return wf.searchWfvUserBelong(in).getUserBelongList();
	}

	/** 指定組織の上位組織リストを抽出 */
	private List<WfcOrganization> getWfmOrganizationUp(String corporationCode, String organizationCode, Date baseDate, Boolean displayValidOnly) {
		final SearchOrganizationUpInParam in = new SearchOrganizationUpInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setBaseDate(baseDate);
		if (displayValidOnly != null && displayValidOnly.booleanValue()) {
			in.setValidDataOnly(displayValidOnly.booleanValue());
		}
		return wf.searchOrganizationUp(in).getOrganizationList();
	}

	/** 指定組織の下位組織リストを抽出 */
	private List<WfcOrganization> getWfmOrganizationSub(String corporationCode, String organizationCodeUp, Date baseDate, Boolean displayValidOnly) {
		final SearchSubOrganizationInParam in = new SearchSubOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCodeUp(organizationCodeUp);
		in.setValidStartDate(baseDate);
		in.setValidEndDate(baseDate);
		if (displayValidOnly != null && displayValidOnly.booleanValue()) {
			in.setDeleteFlag(DeleteFlag.OFF);
		}
		in.setLocaleCode(sessionHolder.getLoginInfo().getLocaleCode());
		return wf.searchSubOrganization(in).getOrganizationList();
	}

	/** 指定組織を抽出 */
	private WfcOrganization getWfmOrganization(String corporationCode, String organizationCode, java.sql.Date baseDate) {
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(corporationCode);
		in.setOrganizationCode(organizationCode);
		in.setValidStartDate(baseDate);
		in.setValidEndDate(baseDate);
//		in.setDeleteFlag(DeleteFlag.OFF);	// 削除レコードの除去は呼び元で行うべし。組織階層ツリーを求めるのに削除済みも含めて抽出せねばならぬ都合があるのだ
		List<WfcOrganization> list = wf.searchWfmOrganization(in).getOrganizationList();
		return (list.isEmpty() ? null : list.get(0));
	}

	/** 企業を抽出 */
	private List<WfmCorporation> getWfmCorporation(String corporationCode, Boolean displayValidOnly) {
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(corporationCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		if (displayValidOnly != null && displayValidOnly.booleanValue()) {
			in.setDeleteFlag(DeleteFlag.OFF);
		}
		in.setLanguage(sessionHolder.getLoginInfo().getLocaleCode());
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_GROUP_CODE),
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_ADDED_INFO),
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_CODE),
		});
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 企業グループコードに紐付く企業を抽出 */
	private List<WfmCorporation> getWfmGroupCorporation(String corporationGroupCode, Boolean displayValidOnly) {
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationGroupCode(corporationGroupCode);
		if (displayValidOnly != null && displayValidOnly.booleanValue()) {
			in.setDeleteFlag(DeleteFlag.OFF);
		}
		in.setLanguage(sessionHolder.getLoginInfo().getLocaleCode());
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_GROUP_CODE),
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_ADDED_INFO),
				new OrderBy(true, "A." + WfmCorporation.CORPORATION_CODE),
		});
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 操作者のアクセス可能な企業一覧を返す */
	private List<WfmCorporation> getWfmCorporationAccessible(Date baseDate, Boolean displayValidOnly) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (login.isAspAdmin())
			// ASP管理者
			return getWfmCorporation(null, displayValidOnly);
		else if (isNotEmpty(login.getCorporationGroupCode()))
			// 企業グループありならグループ内の全企業
			return getWfmGroupCorporation(login.getCorporationGroupCode(), displayValidOnly);
		else
			// その他は自企業
			return getWfmCorporation(login.getCorporationCode(), displayValidOnly);
	}

	/**
	 * 対象組織/企業の直下組織を返す
	 * @param nodeId プロファイル管理ツリーのノードID(企業コード＋組織コードを含んでいる)
	 * @param baseDate
	 * @param displayValidOnly
	 * @return
	 */
	public List<Mm0000TreeItem> getOrgTreeItem(String nodeId, Date baseDate, Boolean displayValidOnly) {
		// ノードID＝'#'は企業を示す
		if ("#".equals(nodeId)) {
			// 操作者がアクセス可能な企業コード一覧
			List<WfmCorporation> corporations = getWfmCorporationAccessible(baseDate, displayValidOnly);

			// 企業＋企業内の第一階層をツリーデータ化
			final List<Mm0000TreeItem> items = new ArrayList<>();
			for (WfmCorporation corporation : corporations) {
				final Mm0000TreeItem c = new Mm0000TreeItem(corporation);
				items.add(c);

				final List<Mm0000TreeItem> children = new ArrayList<>();
				final List<WfcOrganization> organizations = getWfmOrganizationSub(
						corporation.getCorporationCode(), null, baseDate, displayValidOnly);
				for (WfcOrganization org : organizations) {
					children.add(new Mm0000TreeItem(org));
				}
			}
			return items;
		}
		else {
			final Parent parent = parseParent(nodeId);

			if (parent == null || isEmpty(parent.corporationCode))
				throw new BadRequestException("IDが正しいフォーマットではありません");
			if (baseDate == null)
				throw new BadRequestException("基準日が未指定です");

			// 対象組織の直下組織
			final List<WfcOrganization> organizations =
					getWfmOrganizationSub(parent.corporationCode, parent.organizationCode, baseDate, displayValidOnly);

			final List<Mm0000TreeItem> items = new ArrayList<>();
			for (WfcOrganization org : organizations) {
				final Mm0000TreeItem o = new Mm0000TreeItem(org);
				items.add(o);
			}
			return items;
		}
	}

	/**
	 * 対象組織の配下ユーザを返す
	 * @param nodeId プロファイル管理ツリーのノードID(企業コード＋組織コードを含んでいる)
	 * @param baseDate 基準日
	 * @param displayValidOnly
	 * @return
	 */
	public List<Mm0000TreeItem> getUser(String nodeId, Date baseDate, Boolean displayValidOnly) {
		final Parent parent = parseParent(nodeId);

		if (parent == null || isEmpty(parent.corporationCode))
			throw new BadRequestException("IDが正しいフォーマットではありません");
		if (baseDate == null)
			throw new BadRequestException("基準日が未指定です");

		// 対象組織の直下組織
		final List<WfvUserBelongImpl> users =
				getWfmUserBelong(parent.corporationCode, parent.organizationCode, baseDate, displayValidOnly);

		final List<Mm0000TreeItem> items = new ArrayList<>();
		for (WfvUserBelongImpl user : users) {
			items.add(new Mm0000TreeItem(user));
		}
		return items;
	}

	/**
	 * 対象ノードIDの配下にユーザを追加
	 * @param req
	 * @return
	 */
	public Mm0000UpdateResponse addUser(Mm0000AddRequest req) {
		final Parent parent = parseParent(req.nodeId);
		final Mm0000AddUserRequest dummyReq = new Mm0000AddUserRequest();
		dummyReq.corporationCode = parent.corporationCode;
		dummyReq.organizationCode = parent.organizationCode;
		dummyReq.baseDate = req.baseDate;
		return addUser(dummyReq);
	}

	/**
	 * 対象組織の配下にユーザを追加
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0000UpdateResponse addUser(Mm0000AddUserRequest req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.organizationCode))
			throw new BadRequestException("組織コードが未指定です");
		if (req.baseDate == null)
			throw new BadRequestException("基準日が未指定です");

		// ユーザの追加
		final WfmUser newUser = insertWfmUser(req.corporationCode, false, req.baseDate);

		// パスワード
		insertWfmPassword(newUser, req.baseDate);

		// デフォルト役職
		final WfmPost defaultPost = getOrInsertWfmPostDefault(req.corporationCode, req.baseDate);

		// ユーザ所属の追加
		String postCode = defaultPost == null ? null : defaultPost.getPostCode();
		insertWfmUserBelong(newUser, req.organizationCode, postCode, req.baseDate);

		// 正常終了
		final Mm0000UpdateResponse res = createResponse(Mm0000UpdateResponse.class, req);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0102, MessageCd.user));
		res.newUser = newUser;
		return res;
	}

	private void insertWfmUserBelong(final WfmUser newUser, final String organizationCode, final String postCode, java.sql.Date validStartDate) {
		final WfmUserBelong belong = new WfmUserBelongImpl();
		belong.setCorporationCode(newUser.getCorporationCode());
		belong.setUserCode(newUser.getUserCode());
		belong.setOrganizationCode(organizationCode);
		belong.setPostCode(postCode);
		belong.setJobType(JobType.MAIN);
		belong.setImmediateManagerFlag(CommonFlag.OFF);
		belong.setValidStartDate(validStartDate);
		belong.setValidEndDate(ENDDATE);
		final InsertWfmUserBelongInParam in = new InsertWfmUserBelongInParam();
		in.setWfmUserBelong(belong);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final InsertWfmUserBelongOutParam out = wf.insertWfmUserBelongDefault(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);
	}

	private WfmUser insertWfmUser(String corporationCode, boolean isCorpAdmin, java.sql.Date validStartDate) {
		final WfmUser user = new WfmUserImpl();
		user.setCorporationCode(corporationCode);
		if (isCorpAdmin) {
			user.setUserName(i18n.getText(MessageCd.corpAdmin));
			user.setUserAddedInfo(UserAddedInfos.ADMIN);
			user.setAdministratorType(AdministratorType.ADMINISTRATOR);
		} else {
			user.setUserName(i18n.getText(MessageCd.newUserName));
			user.setAdministratorType(AdministratorType.NORMAL_USER);
		}
		user.setValidStartDate(validStartDate);
		user.setValidEndDate(ENDDATE);
		user.setDefaultLocaleCode(LocaleCode.JP);
		final InsertWfmUserInParam in = new InsertWfmUserInParam();
		in.setWfmUser(user);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final InsertWfmUserOutParam out = wf.insertWfmUserDefault(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);
		return out.getWfmUser();
	}

	private void insertWfmPassword(final WfmUser newUser, java.sql.Date validStartDate) {
		// 初回ログイン時にパスワード変更を強制するか？
		Boolean b = corpProperty.getBool(CorporationProperty.FIRST_LOGIN_CHANGE);
		boolean changePassword = (b != null && b.booleanValue());

		final WfmUserPassword pswd = new WfmUserPasswordImpl();
		pswd.setCorporationCode(newUser.getCorporationCode());
		pswd.setUserCode(newUser.getUserCode());
		pswd.setChangeRequestFlag(changePassword ? CommonFlag.ON : CommonFlag.OFF);
		pswd.setDeleteFlag(DeleteFlag.OFF);
		pswd.setLockFlag(LockFlag.OFF);
		pswd.setLoginNgCount(0L);
		pswd.setPassword(SecurityUtils.DEFAULT_HASH_PASSWORD);
		pswd.setValidStartDate(validStartDate);

		final InsertWfmUserPasswordInParam in = new InsertWfmUserPasswordInParam();
		in.setWfmUserPassword(pswd);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final InsertWfmUserPasswordOutParam out = wf.insertWfmUserPassword(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);
	}

	/**
	 * 対象ノードIDの配下に組織を追加
	 * @param req
	 * @return
	 */
	public Mm0000UpdateResponse addOrg(Mm0000AddRequest req) {
		if (isEmpty(req.nodeId))
			throw new BadRequestException("nodeIdが指定されていません");
		if (req.baseDate == null)
			throw new BadRequestException("基準日が指定されていません");
		if (req.displayValidOnly == null)
			throw new BadRequestException("「有効データのみ表示」が指定されていません");

		// ノードIDが表しているのは親組織なので、その組織マスタを求める
		final Parent parent = parseParent(req.nodeId);
		final Mm0000AddOrgRequest dummyReq = new Mm0000AddOrgRequest();
		dummyReq.corporationCode = parent.corporationCode;
		dummyReq.organizationCodeUp = parent.organizationCode;
		dummyReq.baseDate = req.baseDate;
		dummyReq.displayValidOnly = req.displayValidOnly;
		return addOrg(dummyReq);
	}

	/**
	 * 対象ノードIDの配下に組織を追加
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0000UpdateResponse addOrg(Mm0000AddOrgRequest req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが指定されていません");
		if (req.baseDate == null)
			throw new BadRequestException("基準日が指定されていません");
		if (req.displayValidOnly == null)
			throw new BadRequestException("「有効データのみ表示」が指定されていません");

		// ノードIDが表しているのは親組織なので、その組織マスタを求める
		WfcOrganization src = null;
		long level = 1L;
		if (isNotEmpty(req.organizationCodeUp)) {
			src = getWfmOrganization(req.corporationCode, req.organizationCodeUp, req.baseDate);
			if (src != null) {
				level = plus(src.getOrganizationLevel(), 1L);
			}
		}

		final WfmOrganization org = new WfmOrganizationImpl();
		org.setCorporationCode(req.corporationCode);
		org.setOrganizationCodeUp(req.organizationCodeUp);
		org.setOrganizationLevel(level);
		org.setOrganizationName(i18n.getText(MessageCd.newOrgName));
		org.setValidStartDate(req.baseDate);
		org.setValidEndDate(ENDDATE);
		org.setDeleteFlag(DeleteFlag.OFF);

		final InsertWfmOrganizationInParam in = new InsertWfmOrganizationInParam();
		in.setWfmOrganization(org);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final InsertWfmOrganizationOutParam out = wf.insertWfmOrganizationDefault(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		// 正常終了
		final WfmOrganization newOrg = out.getWfmOrganization();
		final Mm0000UpdateResponse res = createResponse(Mm0000UpdateResponse.class, req);
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0102, MessageCd.organization));
		res.newOrganization = newOrg;
		res.nodeIds = getNodeIds(newOrg.getCorporationCode(), newOrg.getOrganizationCode(), req.baseDate, req.displayValidOnly);
		return res;
	}


	/**
	 * 指定組織を削除する
	 * @param req
	 * @return
	 */
	public BaseResponse deleteOrg(Mm0000DeleteRequest req) {
		if (isEmpty(req.nodeId))
			throw new BadRequestException("nodeIdが指定されていません");
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final Parent parent = parseParent(req.nodeId);

		String error = mm0002service.deleteOrg(
				parent.corporationCode, parent.organizationCode, parent.timestampUpdated);
		if (isEmpty(error)) {
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.organization));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	private Long plus(Long organizationLevel, Long mod) {
		if (organizationLevel != null && mod != null)
			return organizationLevel + mod;
		if (organizationLevel != null)
			return organizationLevel;
		return mod;
	}

	private WfmMenuRole insertWfmMenuRole(WfmCorporation newCorp, String menuRoleCd, String menuRoleName, java.sql.Date validStartDate) {
		final WfmMenuRole role = new WfmMenuRoleImpl();
		role.setCorporationCode(newCorp.getCorporationCode());
		role.setMenuRoleCode(menuRoleCd);
		role.setMenuRoleName(menuRoleName);
		if (MenuRoleCodes.ASP.equals(menuRoleCd))
			role.setMenuRoleType(MenuRoleType.ASP);
		else if (MenuRoleCodes.CorpAdmin.equals(menuRoleCd))
			role.setMenuRoleType(MenuRoleType.CORPORATION);
		else if (MenuRoleCodes.GroupAdmin.equals(menuRoleCd))
			role.setMenuRoleType(MenuRoleType.GROUP);
		else
			role.setMenuRoleType(MenuRoleType.NORMAL);
		role.setValidStartDate(validStartDate);
		role.setValidEndDate(ENDDATE);
		role.setDeleteFlag(DeleteFlag.OFF);
		final InsertWfmMenuRoleInParam in = new InsertWfmMenuRoleInParam();
		in.setWfmMenuRole(role);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final InsertWfmMenuRoleOutParam out = wf.insertWfmMenuRole(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		WfmMenuRole newRole = out.getWfmMenuRole();
		return newRole;
	}

	/**
	 * 新規に企業を追加する
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0000UpdateResponse addCorp(Mm0000AddRequest req) {
		Mm0000UpdateResponse res = createResponse(Mm0000UpdateResponse.class, req);

		LoginInfo login = sessionHolder.getLoginInfo();
		if (!login.isAspAdmin())
			throw new ForbiddenException("権限がありません。企業を追加できるのはASP管理者だけです。");
		if (req.baseDate == null)
			throw new BadRequestException("基準日が未指定です");

		// すでに使用されている企業コードか？
		List<WfmCorporation> checks = getWfmCorporation(req.newCorporationCode, null);
		if (isEmpty(req.newCorporationCode)) {
			res.addAlerts(i18n.getText(MessageCd.MSG0001, i18n.getText(MessageCd.corporationCode)));
			res.success = false;
		}
		else if (checks != null && !checks.isEmpty()) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108,
					i18n.getText(MessageCd.corporationCode), req.newCorporationCode));
			res.success = false;
		}
		else {
			WfmCorporation newCorp = new WfmCorporation();
			newCorp.setCorporationCode(req.newCorporationCode);
			newCorp.setCorporationAddedInfo(req.newCorporationCode);
			newCorp.setCorporationName(req.newCorporationName);
			newCorp.setDeleteFlag(DeleteFlag.OFF);

			// 企業
			{
				final InsertWfmCorporationInParam in = new InsertWfmCorporationInParam();
				in.setWfmCorporation(newCorp);
				in.setWfUserRole(sessionHolder.getWfUserRole());
				final InsertWfmCorporationOutParam out = wf.insertWfmCorporation(in);
				if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
					throw new WfException(out);
				newCorp = getWfmCorporation(out.getWfmCorporation().getCorporationCode(), req.displayValidOnly).get(0);
			}
			String newCorporationCode = newCorp.getCorporationCode();

			// 組織
			WfmOrganization newOrg;
			{
				newOrg = new WfmOrganizationImpl();
				newOrg.setCorporationCode(newCorp.getCorporationCode());
				newOrg.setOrganizationName(newCorp.getCorporationName());
				newOrg.setOrganizationLevel(1L);
				newOrg.setValidStartDate(req.baseDate);
				newOrg.setValidEndDate(ENDDATE);
				newOrg.setDeleteFlag(DeleteFlag.OFF);

				final InsertWfmOrganizationInParam in = new InsertWfmOrganizationInParam();
				in.setWfmOrganization(newOrg);
				in.setWfUserRole(sessionHolder.getWfUserRole());
				final InsertWfmOrganizationOutParam out = wf.insertWfmOrganizationDefault(in);
				if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
					throw new WfException(out);

				newOrg = out.getWfmOrganization();
			}

			// 役職
			final WfmPost newPost = getOrInsertWfmPostDefault(newCorporationCode, req.baseDate);

			// ユーザ
			final WfmUser newUser = insertWfmUser(newCorp.getCorporationCode(), true, req.baseDate);

			// パスワード
			insertWfmPassword(newUser, req.baseDate);

			// ユーザ所属
			insertWfmUserBelong(newUser, newOrg.getOrganizationCode(), newPost.getPostCode(), req.baseDate);

			// メニューロール
			insertWfmMenuRoles(newCorp, newUser, req.baseDate);

			// メニューロールでアクセス可能なメニュー
			insertMwmAccessibleMenu(newCorp, req.baseDate);

			res.addSuccesses(i18n.getText(MessageCd.MSG0102, i18n.getText(MessageCd.corporation)));

			// WFM系およびMWM系のマスタの初期値をASP企業からコピーしてインサート
			mm0500Service.insertInitMaster(CorporationCodes.ASP, newCorp.getCorporationCode(), login.getLocaleCode());

			res.newCorporation = newCorp;
			res.success = true;
		}
		return res;
	}

	/** デフォルトの役職があればそのレコードを、なければ追加して返す */
	private WfmPost getOrInsertWfmPostDefault(String newCorporationCode, java.sql.Date validStartDate) {
		// すでにデフォルトの役職が存在していないか？
		{
			final SearchWfmPostInParam in = new SearchWfmPostInParam();
			in.setCorporationCode(newCorporationCode);
			in.setPostAddedInfo(DEFAULT_POST_ADDED_INFO);

			final List<WfcPost> posts = wf.searchWfmPost(in).getPostList();
			if (!posts.isEmpty())
				return posts.get(0);
		}

		// なければインサート
		{
			final WfmPost newPost = new WfmPostImpl();
			newPost.setCorporationCode(newCorporationCode);
			newPost.setPostAddedInfo(DEFAULT_POST_ADDED_INFO);
			newPost.setPostName(i18n.getText(MessageCd.noJobTitle));
			newPost.setValidStartDate(validStartDate);
			newPost.setValidEndDate(ENDDATE);
			newPost.setDeleteFlag(DeleteFlag.OFF);

			final InsertWfmPostInParam in = new InsertWfmPostInParam();
			in.setWfmPost(newPost);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final InsertWfmPostOutParam out = wf.insertWfmPostDefault(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);

			return out.getWfmPost();
		}
	}

	/** メニューロールでアクセス可能なメニュー */
	private void insertMwmAccessibleMenu(WfmCorporation newCorp, java.sql.Date validStartDate) {
		String corporationCode = newCorp.getCorporationCode();

		// 企業管理者がアクセス可能なメニューを挿入
		{
			final String menuRoleCode = MenuRoleCodes.CorpAdmin;
			final List<MwmMenu> menus = repository.getMwmMenus(corporationCode, menuRoleCode, MenuRoleType.CORPORATION);
			repository.insertMwmAccessibleMenu(corporationCode, menuRoleCode, menus, validStartDate);
		}
		// ユーザ管理者がアクセス可能なメニューを挿入
		{
			final String menuRoleCode = MenuRoleCodes.UserAdmin;
			final List<MwmMenu> menus = repository.getMwmMenus(corporationCode, menuRoleCode, MenuRoleType.NORMAL);
			repository.insertMwmAccessibleMenu(corporationCode, menuRoleCode, menus, validStartDate);
		}
		// 全社員がアクセス可能なメニューを挿入
		{
			/* ワークリスト|案件一覧|代理|新規起案 */
			final Set<Long> defaultMenuIds = new HashSet<>(Arrays.asList(
					MenuIds.WORKLIST, MenuIds.MULTI_TRAY, MenuIds.START_DOC
					, MenuIds.TRANSFER_TITLE, MenuIds.TRANSFER));

			final String menuRoleCode = MenuRoleCodes.AllUser;
			final List<MwmMenu> menus = repository.getMwmMenus(corporationCode, menuRoleCode, MenuRoleType.NORMAL)
					.stream()
					.filter(m -> defaultMenuIds.contains(m.getMenuId()))
					.collect(Collectors.toList());
			repository.insertMwmAccessibleMenu(corporationCode, menuRoleCode, menus, validStartDate);
		}
	}

	/** メニューロールのインサート */
	private void insertWfmMenuRoles(WfmCorporation newCorp, final WfmUser newUser, java.sql.Date validStartDate) {
		// 企業管理者メニューロール（企業管理者を企業管理者ロールへ追加）
		{
			// メニューロール
			final WfmMenuRole role = insertWfmMenuRole(
					newCorp, MenuRoleCodes.CorpAdmin, i18n.getText(MessageCd.corpAdmin), validStartDate);
			// メニューロール構成
			final WfmMenuRoleDetail detail = new WfmMenuRoleDetailImpl();
			detail.setCorporationCode(role.getCorporationCode());
			detail.setMenuRoleCode(role.getMenuRoleCode());
			detail.setMenuRoleAssignmentType(MenuRoleAssignmentType.USER);
			detail.setCorporationCodeAccess(newUser.getCorporationCode());
			detail.setUserCodeAccess(newUser.getUserCode());
			detail.setValidStartDate(validStartDate);
			detail.setValidEndDate(ENDDATE);
			detail.setDeleteFlag(DeleteFlag.OFF);
			final InsertWfmMenuRoleDetailInParam in = new InsertWfmMenuRoleDetailInParam();
			in.setWfmMenuRoleDetail(detail);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final InsertWfmMenuRoleDetailOutParam out = wf.insertWfmMenuRoleDetail(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}
		// ユーザ管理者メニューロール（企業管理者をユーザ管理者ロールへ追加）
		{
			// メニューロール
			final WfmMenuRole role = insertWfmMenuRole(
					newCorp, MenuRoleCodes.UserAdmin, i18n.getText(MessageCd.userAdmin), validStartDate);
			// メニューロール構成
			final WfmMenuRoleDetail detail = new WfmMenuRoleDetailImpl();
			detail.setCorporationCode(role.getCorporationCode());
			detail.setMenuRoleCode(role.getMenuRoleCode());
			detail.setMenuRoleAssignmentType(MenuRoleAssignmentType.USER);
			detail.setCorporationCodeAccess(newUser.getCorporationCode());
			detail.setUserCodeAccess(newUser.getUserCode());
			detail.setValidStartDate(validStartDate);
			detail.setValidEndDate(ENDDATE);
			detail.setDeleteFlag(DeleteFlag.OFF);
			final InsertWfmMenuRoleDetailInParam in = new InsertWfmMenuRoleDetailInParam();
			in.setWfmMenuRoleDetail(detail);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final InsertWfmMenuRoleDetailOutParam out = wf.insertWfmMenuRoleDetail(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}
		// 全社員メニューロール
		{
			// メニューロール
			final WfmMenuRole role = insertWfmMenuRole(
					newCorp, MenuRoleCodes.AllUser, i18n.getText(MessageCd.allUserName), validStartDate);
			// メニューロール構成
			final WfmMenuRoleDetail detail = new WfmMenuRoleDetailImpl();
			detail.setCorporationCode(role.getCorporationCode());
			detail.setMenuRoleCode(role.getMenuRoleCode());
			detail.setMenuRoleAssignmentType(MenuRoleAssignmentType.ALL_OF_CORP);
			detail.setCorporationCodeAccess(newUser.getCorporationCode());
			detail.setValidStartDate(validStartDate);
			detail.setValidEndDate(ENDDATE);
			detail.setDeleteFlag(DeleteFlag.OFF);
			final InsertWfmMenuRoleDetailInParam in = new InsertWfmMenuRoleDetailInParam();
			in.setWfmMenuRoleDetail(detail);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final InsertWfmMenuRoleDetailOutParam out = wf.insertWfmMenuRoleDetail(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}
	}

	/** 上位組織を保持するクラス */
	private class Parent {
		public String corporationCode;
		public String organizationCode;
		public Long timestampUpdated;

		/** 企業とみなしてインスタンス化 */
		public Parent(String corporationCode, Long timestampUpdated) {
			this.corporationCode = corporationCode;
			this.timestampUpdated = Long.valueOf(timestampUpdated);
		}

		/** 組織とみなしてインスタンス化 */
		public Parent(String corporationCode, String organizationCode, Long timestampUpdated) {
			this.corporationCode = corporationCode;
			this.organizationCode = organizationCode;
			this.timestampUpdated = Long.valueOf(timestampUpdated);
		}
	}
}
