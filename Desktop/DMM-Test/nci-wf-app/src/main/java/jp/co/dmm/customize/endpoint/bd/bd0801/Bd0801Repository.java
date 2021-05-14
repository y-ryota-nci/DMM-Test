package jp.co.dmm.customize.endpoint.bd.bd0801;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.AssignRoleCodes;
import jp.co.dmm.customize.jpa.entity.mw.BgtPln;
import jp.co.dmm.customize.jpa.entity.mw.BgtPlnPK;
import jp.co.dmm.customize.jpa.entity.mw.VOrganizationLevel;
import jp.co.dmm.customize.jpa.entity.mw.YrMst;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmOrganization;
import jp.co.nci.integrated_workflow.model.custom.WfcOrganization;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 予算入力画面リポジトリ
 */
@ApplicationScoped
public class Bd0801Repository extends BaseRepository {
	@Inject WfInstanceWrapper api;
	@Inject private SessionHolder sessionHolder;

	private static final String REPLACE = quotePattern("${REPLACE}");

	/** 参加者ロールCD：法務部/CFO室/主計/DMM経理 */
	private static final Set<String> targets = asSet(
			AssignRoleCodes.LEGAL, AssignRoleCodes.CFO, AssignRoleCodes.ACCOUNT, AssignRoleCodes.BUDGET);

	/** 年度の選択肢を生成 */
	public List<OptionItem> getYearList() {
		LoginInfo login = sessionHolder.getLoginInfo();
		final Object[] params = { login.getCorporationCode() };
		final String sql = getSql("BD0801_01");
		final List<YrMst> list = select(YrMst.class, sql, params);

		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll(list.stream()
				.map(e -> new OptionItem(e.getId().getYrCd(), e.getYrNm()))
				.collect(Collectors.toList()));

		return items;
	}

	/** 操作者は閲覧無制限か、制限されているか */
	public boolean isUnlimitedOperator() {
		// 操作者の所有している参加者ロールコード(複数)に、
		// DGHDの法務部／CFO室／DMM経理／主計が含まれていれば、閲覧無制限
		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = login.getCorporationCode();
		final Stream<String> assignRoleCodes = login.getAssignRoleCodes().stream();
		return eq(CorporationCodes.DGHD, corporationCode)
				&& assignRoleCodes.anyMatch(cd -> targets.contains(cd));
	}

	/** 操作者の所属している組織の組織階層ビューを抽出 */
	private List<VOrganizationLevel> getMyOrganizationLevel() {
		LoginInfo login = sessionHolder.getLoginInfo();
		final List<Object> params = asList(login.getCorporationCode());
		params.addAll(login.getOrganizationCodes());
		final String sql = getSql("BD0801_04")
				.replaceFirst(REPLACE, toInListSql(" and ORGANIZATION_CODE", login.getOrganizationCodes().size()));
		return select(VOrganizationLevel.class, sql, params.toArray());
	}

	/** 本部の選択肢 */
	public List<OptionItem> getOrgLv2List(String companyCd) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		// すべての第二階層組織
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(companyCd);
		in.setOrganizationLevel(2);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmOrganization.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmOrganization.SORT_ORDER),
				new OrderBy(true, "A." + WfmOrganization.ORGANIZATION_ADDED_INFO),
				new OrderBy(true, "A." + WfmOrganization.ORGANIZATION_CODE),
		});

		final List<WfcOrganization> list = api.searchWfmOrganization(in).getOrganizationList();
		items.addAll(list.stream()
				.map(e -> new OptionItem(e.getOrganizationCode(), e.getOrganizationName()))
				.collect(Collectors.toList()));

		if (!isUnlimitedOperator()) {
			// 第二階層組織が空のレコードがあれば、操作者は第一階層に所属するユーザとみなして全組織を閲覧可能
			// 空のレコードが無ければ所属組織はすべて第二階層以下なので、該当レコードのみに絞り込む
			final List<VOrganizationLevel> orgList = getMyOrganizationLevel();
			if (orgList.stream().allMatch(o -> isNotEmpty(o.getOrganizationCode2()))) {
				// 操作者所属組織の第二階層組織のみとなるよう、不要レコードを削除
				final Set<String> lv2OrgCds = orgList.stream()
						.map(o -> o.getOrganizationCode2())
						.filter(cd -> isNotEmpty(cd))
						.collect(Collectors.toSet());
				for (Iterator<OptionItem> it = items.iterator(); it.hasNext(); ) {
					OptionItem item = it.next();
					if (isNotEmpty(item.getValue()) && !lv2OrgCds.contains(item.getValue()))
						it.remove();
				}
			}
		}
		return items;
	}

	/** 部/室の選択肢 */
	public List<OptionItem> getOrgLv3List(String companyCd, String organizationCodeLv2) {
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		if (isEmpty(organizationCodeLv2))
			return items;

		// すべての第三階層組織
		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(companyCd);
		in.setOrganizationLevel(3);
		in.setOrganizationCodeUp(organizationCodeLv2);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmOrganization.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmOrganization.SORT_ORDER),
				new OrderBy(true, "A." + WfmOrganization.ORGANIZATION_ADDED_INFO),
				new OrderBy(true, "A." + WfmOrganization.ORGANIZATION_CODE),
		});

		final List<WfcOrganization> list = api.searchWfmOrganization(in).getOrganizationList();
		items.addAll(list.stream()
				.map(e -> new OptionItem(e.getOrganizationCode(), e.getOrganizationName()))
				.collect(Collectors.toList()));

		if (!isUnlimitedOperator()) {
			// 第三階層組織が空のレコードがあれば、操作者は第二階層以上に所属するユーザとみなして配下組織を閲覧可能
			// 空のレコードが無ければ所属組織はすべて第三階層以下なので、該当レコードのみに絞り込む
			final List<VOrganizationLevel> orgList = getMyOrganizationLevel();
			if (orgList.stream().allMatch(o -> isNotEmpty(o.getOrganizationCode3()))) {
				// 操作者所属組織の第三階層組織のみとなるよう、不要レコードを削除
				final Set<String> lv3OrgCds = orgList.stream()
						.map(o -> o.getOrganizationCode3())
						.filter(cd -> isNotEmpty(cd))
						.collect(Collectors.toSet());
				for (Iterator<OptionItem> it = items.iterator(); it.hasNext(); ) {
					OptionItem item = it.next();
					if (isNotEmpty(item.getValue()) && !lv3OrgCds.contains(item.getValue()))
						it.remove();
				}
			}
		}
		return items;
	}

	/** 予算計画一覧の抽出 */
	public List<Bd0801Entity> select(Bd0801SearchRequest req) {
		final String sql = getSql("BD0801_02");
		final Object[] params = {
				req.companyCd,
				req.yrCd,
				req.organizationCodeLv3,
				req.rcvCostPayTp,
				req.bsplTp
		};
		final List<Bd0801Entity> list = select(Bd0801Entity.class, sql, params);
		list.forEach(e -> em.detach(e));
		return list;
	}

	/** 既存の予算計画を抽出 */
	public Map<String, BgtPln> getBgtPln(Bd0801SearchRequest req) {
		final Bd0801Entity sample = req.inputs.get(0);
		final Object[] params = { req.companyCd, sample.yrCd, sample.organizationCode, sample.rcvCostPayTp };
		final String sql = getSql("BD0801_03");
		final List<BgtPln> list = select(BgtPln.class, sql, params);
		// 予算科目コードをキーにMap作成
		return list.stream()
				.collect(Collectors.toMap(e -> e.getId().getBgtItmCd(), e -> e));
	}

	/** 予算計画をインサート */
	public void insert(Bd0801Entity input) {
		final BgtPlnPK id = new BgtPlnPK();
		id.setCompanyCd(input.companyCd);
		id.setYrCd(input.yrCd);
		id.setOrganizationCode(input.organizationCode);
		id.setRcvinspPayTp(input.rcvCostPayTp);
		id.setBgtItmCd(input.bgtItmCd);

		final BgtPln e = new BgtPln();
		e.setId(id);
		e.setBgtAmt01(input.bgtAmt01);
		e.setBgtAmt02(input.bgtAmt02);
		e.setBgtAmt03(input.bgtAmt03);
		e.setBgtAmt04(input.bgtAmt04);
		e.setBgtAmt05(input.bgtAmt05);
		e.setBgtAmt06(input.bgtAmt06);
		e.setBgtAmt07(input.bgtAmt07);
		e.setBgtAmt08(input.bgtAmt08);
		e.setBgtAmt09(input.bgtAmt09);
		e.setBgtAmt10(input.bgtAmt10);
		e.setBgtAmt11(input.bgtAmt11);
		e.setBgtAmt12(input.bgtAmt12);
		e.setDltFg(DeleteFlag.OFF);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeCreated(login.getCorporationCode());
		e.setUserCodeCreated(login.getUserCode());
		e.setIpCreated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampCreated(timestamp());
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());

		em.persist(e);
	}

	/** 予算計画を更新 */
	public void update(BgtPln e, Bd0801Entity input) {
		if (!eq(e.getTimestampUpdated(), input.timestampUpdated))
			throw new AlreadyUpdatedException();

		e.setBgtAmt01(input.bgtAmt01);
		e.setBgtAmt02(input.bgtAmt02);
		e.setBgtAmt03(input.bgtAmt03);
		e.setBgtAmt04(input.bgtAmt04);
		e.setBgtAmt05(input.bgtAmt05);
		e.setBgtAmt06(input.bgtAmt06);
		e.setBgtAmt07(input.bgtAmt07);
		e.setBgtAmt08(input.bgtAmt08);
		e.setBgtAmt09(input.bgtAmt09);
		e.setBgtAmt10(input.bgtAmt10);
		e.setBgtAmt11(input.bgtAmt11);
		e.setBgtAmt12(input.bgtAmt12);

		final LoginInfo login = sessionHolder.getLoginInfo();
		e.setCorporationCodeUpdated(login.getCorporationCode());
		e.setUserCodeUpdated(login.getUserCode());
		e.setIpUpdated(sessionHolder.getWfUserRole().getIpAddress());
		e.setTimestampUpdated(timestamp());
	}
}
