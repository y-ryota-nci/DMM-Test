package jp.co.nci.iwf.component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationGroupInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 企業サービス
 */
@BizLogic
public class CorporationService extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;
	@Inject
	private WfInstanceWrapper wf;

	/**
	 * 自分の会社をリスト化
	 * @param emptyLine 空行を生成するならtrue
	 * @return
	 */
	public List<OptionItem> getThisCorporation(boolean emptyLine) {
		List<WfmCorporation> corps = getWfcCorporationSelf();
		return toOptionItems(corps, emptyLine);
	}

	/**
	 * 自分のアクセス可能な全企業をリスト化
	 * @param emptyLine 空行を生成するならtrue
	 * @return
	 */
	public List<OptionItem> getMyCorporations(boolean emptyLine) {
		final LoginInfo login = sessionHolder.getLoginInfo();

		List<WfmCorporation> corps = null;
		if (login.isAspAdmin())
			// ASP管理者＝全企業
			corps = getWfcCorporationAll();
		else if (isNotEmpty(login.getCorporationGroupCode()))
			// グループ企業があればグループ内の全企業
			corps = getWfcCorporationInGroup();
		else
			// 企業のみ
			corps = getWfcCorporationSelf();

		return toOptionItems(corps, emptyLine);
	}

	/** 企業情報をOptionItemリスト化 */
	private List<OptionItem> toOptionItems(List<WfmCorporation> corps, boolean emptyLine) {
		final List<OptionItem> items = new ArrayList<>();

		if (emptyLine)
			items.add(OptionItem.EMPTY);

		if (corps != null) {
			for (WfmCorporation c : corps) {
				items.add(new OptionItem(c.getCorporationCode(), c.getCorporationName()));
			}
		}
		return items;
	}

	/** 自社を抽出 */
	private List<WfmCorporation> getWfcCorporationSelf() {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(login.getCorporationCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setLanguage(login.getLocaleCode());
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 全企業を抽出 */
	private List<WfmCorporation> getWfcCorporationAll() {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setLanguage(login.getLocaleCode());
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 自社の企業グループに属する企業を抽出 */
	private List<WfmCorporation> getWfcCorporationInGroup() {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationGroupCode(login.getCorporationGroupCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setLanguage(login.getLocaleCode());
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 企業コードをキーに企業マスタを抽出 */
	public WfmCorporation getWfmCorporation(String corporationCode) {
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(corporationCode);
		in.setLanguage(sessionHolder.getLoginInfo().getLocaleCode());
		List<WfmCorporation> list = wf.searchWfmCorporation(in).getCorporations();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/** 企業グループコードをキーに企業マスタを抽出 */
	public List<WfmCorporation> getWfmCorporationByGroup(String corporationGroupCode) {
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationGroupCode(corporationGroupCode);
		in.setLanguage(sessionHolder.getLoginInfo().getLocaleCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] { new OrderBy(true, WfmCorporation.CORPORATION_CODE) });
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		return wf.searchWfmCorporation(in).getCorporations();
	}

	/** 全企業グループをOptionItemリスト化 */
	public List<OptionItem> getCorporationGroup(boolean emptyLine) {
		final SearchWfmCorporationGroupInParam in = new SearchWfmCorporationGroupInParam();
		in.setLoginCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		final List<OptionItem> items = new ArrayList<>();
		if (emptyLine) {
			items.add(OptionItem.EMPTY);
		}
		items.addAll(wf.searchWfmCorporationGroup(in).getCorporationGroupList()
				.stream()
				.map(g -> new OptionItem(g.getCorporationGroupCode(), g.getCorporationGroupName()))
				.collect(Collectors.toList()));
		return items;
	}

	/** 自分がアクセス可能な企業グループをOptionItemリスト化  */
	public  List<OptionItem> getMyCorporationGroup(boolean emptyLine) {
		final List<OptionItem> items = new ArrayList<>();
		if (emptyLine) {
			items.add(OptionItem.EMPTY);
		}

		// 非ASP管理者で企業グループに属していないなら、選択肢は何もない
		final LoginInfo login = sessionHolder.getLoginInfo();
		if (!login.isAspAdmin() && isEmpty(login.getCorporationGroupCode())) {
			return items;
		}

		final SearchWfmCorporationGroupInParam in = new SearchWfmCorporationGroupInParam();
		in.setLoginCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setDeleteFlag(DeleteFlag.OFF);
		if (!login.isAspAdmin()) {
			// ASP管理者は何でも選択できるが、その他は自グループ企業のみ
			in.setCorporationGroupCode(login.getCorporationGroupCode());
		}
		items.addAll(wf.searchWfmCorporationGroup(in).getCorporationGroupList()
				.stream()
				.map(g -> new OptionItem(g.getCorporationGroupCode(), g.getCorporationGroupName()))
				.collect(Collectors.toList()));
		return items;
	}
}
