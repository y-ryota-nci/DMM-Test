package jp.co.nci.iwf.endpoint.mm.mm0080;

import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmFunctionOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * アクション機能一覧のサービス
 */
@BizLogic
public class Mm0080Service extends MmBaseService<WfmFunction> {
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0080Response init(Mm0080Request req) {
		final Mm0080Response res = createResponse(Mm0080Response.class, req);
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
	public Mm0080Response search(Mm0080Request req) {
		SearchWfmFunctionInParam inParam = new SearchWfmFunctionInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setFunctionCode(req.functionCode);
		inParam.setFunctionName(req.functionName);
		inParam.setDeleteFlag(req.deleteFlag);
		inParam.setPageSize(req.pageSize);
		inParam.setPageNo(req.pageNo);
		inParam.setOrderBy(toOrderBy(req, "F."));
		SearchWfmFunctionOutParam outParam = wf.searchWfmFunction(inParam);

		final List<WfmFunction> results = outParam.getFunctions();

		int allCount = outParam.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0080Response res = createResponse(Mm0080Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		res.results = results;
		res.success = true;
		return res;
	}

	/**
	 * アクションマスタ追加
	 * @param req
	 * @return
	 */
	public Mm0080Response add(Mm0080Request req) {
		final WfmFunction function = new WfmFunction();
		function.setCorporationCode(req.corporationCode);
		function.setDeleteFlag(DeleteFlag.OFF);

		final Mm0080Response res = createResponse(Mm0080Response.class, req);
		res.function = function;
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.post));
		return res;
	}
}
