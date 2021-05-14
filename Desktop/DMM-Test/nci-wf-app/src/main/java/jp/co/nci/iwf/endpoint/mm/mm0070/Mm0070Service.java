package jp.co.nci.iwf.endpoint.mm.mm0070;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmActionOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * アクション一覧のサービス
 */
@BizLogic
public class Mm0070Service extends MmBaseService<WfmAction> {
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0070Response init(Mm0070Request req) {
		final Mm0070Response res = createResponse(Mm0070Response.class, req);
		res.corporations = getAccessibleCorporations(false);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0070Response search(Mm0070Request req) {
		SearchWfmActionInParam inParam = new SearchWfmActionInParam();
		inParam.setSearchType(SearchMode.SEARCH_MODE_LIST);
		inParam.setCorporationCode(req.corporationCode);
		inParam.setActionCode(req.actionCode);
		inParam.setActionName(req.actionName);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setPageSize(req.pageSize);
		inParam.setPageNo(req.pageNo);
		inParam.setOrderBy(toOrderBy(req, "A."));
		SearchWfmActionOutParam outParam = wf.searchWfmAction(inParam);
		final List<WfmAction> results = outParam.getActions();

		int allCount = outParam.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0070Response res = createResponse(Mm0070Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		res.pageNo = pageNo;
		res.pageCount = pageCount;
		res.results = results;
		res.success = true;
		return res;
	}

	/**
	 * アクションマスタ追加
	 * @param req
	 * @return
	 */
	public Mm0070Response add(Mm0070Request req) {
		final WfmAction action = new WfmAction();
		action.setCorporationCode(req.corporationCode);
		action.setDeleteFlag(DeleteFlag.OFF);

		final Mm0070Response res = createResponse(Mm0070Response.class, req);
		res.action = action;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}
}
