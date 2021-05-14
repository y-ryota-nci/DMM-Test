package jp.co.nci.iwf.endpoint.mm.mm0410;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmCorporationOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * 企業一覧のサービス
 */
@BizLogic
public class Mm0410Service extends MmBaseService<WfmCorporation> {
	@Inject private WfmLookupService lookup;
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0410Response init(Mm0410Request req) {
		final Mm0410Response res = createResponse(Mm0410Response.class, req);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		// 企業グループの選択肢
		res.corporations = corp.getMyCorporations(true);
		res.corporationGroups = corp.getMyCorporationGroup(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0410Response search(Mm0410Request req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(req.corporationCode);
		in.setCorporationName(req.corporationName);
		in.setCorporationGroupCode(req.corporationGroupCode);
		in.setDeleteFlag(req.deleteFlag);
		in.setOrderBy(this.toOrderBy(req));
		in.setPageNo(req.pageNo);
		in.setPageSize(req.pageSize);

		// 所有ロールによる暗黙の絞り込み条件
		if (!login.isAspAdmin()) {
			if (isNotEmpty(login.getCorporationGroupCode()))
				// 企業グループに属していれば、グループ内は見える
				in.setCorporationGroupCode(login.getCorporationGroupCode());
			else
				// 企業グループにも属してなければ、表示可能なのは自社のみ。
				in.setCorporationCode(login.getCorporationCode());
		}
		final SearchWfmCorporationOutParam out = wf.searchWfmCorporation(in);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		int allCount = out.getCount();
		final Mm0410Response res = createResponse(Mm0410Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = res.pageNo;

		res.results = out.getCorporations();
		res.success = true;
		return res;
	}

	/**
	 * (IWF APIの)ソート条件へ変換
	 * @param req
	 * @param alias テーブル名の別名(エイリアス)
	 * @return
	 */
	private OrderBy[] toOrderBy(Mm0410Request req) {
		List<OrderBy> list = new ArrayList<>();
		String[] cols = req.sortColumn.split(",");
		for (String c : cols) {
			if (isNotEmpty(c)) {
				final String col = (c.equals("CORPORATION_GROUP_NAME") ? "B." : "A.") + c.trim();
				list.add(new OrderBy(req.sortAsc, col));
			}
		}
		return list.toArray(new OrderBy[list.size()]);
	}
}
