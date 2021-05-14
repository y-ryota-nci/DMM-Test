package jp.co.nci.iwf.endpoint.mm.mm0300;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.api.param.input.UpdateRouteDefInParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateRouteDefOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmProcessDefOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloader;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;

/**
 * ルート一覧サービス
 */
@BizLogic
public class Mm0300Service extends MmBaseService<WfmProcessDef> implements CodeMaster {

	@Inject private WfmLookupService lookup;
	@Inject private ProcessDefDownloader downloader;
	@Inject private RouteSettingService routeSetting;


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0300Response init(Mm0300Request req) {
		// 初期検索
		final Mm0300Response res = createResponse(Mm0300Response.class, req);
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		res.success = true;
		return res;
	}

	/**
	 * 検索処理
	 * @param req
	 * @return
	 */
	public Mm0300Response search(Mm0300Request req) {
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setDeleteFlag(req.deleteFlag);
		in.setProcessDefCode(req.processDefCode);
		in.setProcessDefName(req.processDefName);
		in.setValidStartDate(req.validStartDate);
		in.setValidEndDate(req.validEndDate);
		in.setSearchType(SearchMode.SEARCH_MODE_LIST);
		in.setOrderBy(toOrderBy(req, "PD."));
		in.setPageSize(req.pageSize);
		in.setPageNo(req.pageNo);
		final SearchWfmProcessDefOutParam out = wf.searchWfmProcessDef(in);
		final List<WfmProcessDef> posts =  out.getProcessDefs();

		int allCount = out.getCount();
		int pageCount = calcPageCount(allCount, req.pageSize);
		int pageNo = calcPageNo(req.pageNo, pageCount);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Mm0300Response res = createResponse(Mm0300Response.class, req, allCount);

		// 件数で補正されたページ番号を反映
		req.pageNo = pageNo;

		// 結果の抽出
		res.pageNo = pageNo;
		res.results = posts;
		res.success = true;
		return res;
	}

	/**
	 * プロセス定義のZIPダウンロード
	 * @param screenCode
	 * @return
	 */
	public StreamingOutput downloadZip(List<WfmProcessDef> procList) {
		return downloader.setup(procList);
	}

	/**
	 * 枝番更新処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0300BranchResponse updateBranch(Mm0300BranchRequest req) {
		final Mm0300BranchResponse res = createResponse(Mm0300BranchResponse.class, req);

		WfmProcessDef wfmProcessDef = routeSetting.getProcessDef(req.branchCorporationCode, req.branchProcessDefCode, req.branchProcessDefDetailCode);

		UpdateRouteDefInParam in = new UpdateRouteDefInParam();
		in.setWfmProcessDef(wfmProcessDef);
		in.setValidStartDate(req.updateValidStartDate);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		UpdateRouteDefOutParam out = wf.updateRouteDef(in);
		if (ReturnCode.SUCCESS.compareTo(out.getReturnCode()) == 0) {
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.branchNumber));
			res.success = true;
		} else {
			res.addAlerts(out.getReturnMessage());
			res.success = false;
		}

		return res;
	}

}
