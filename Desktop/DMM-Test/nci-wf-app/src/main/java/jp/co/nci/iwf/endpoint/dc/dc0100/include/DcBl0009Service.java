package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.document.BinderService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書(業務文書・バインダー)ブロック：バインダーサービス
 */
@BizLogic
public class DcBl0009Service extends BaseService implements CodeMaster {

	/** バインダーサービス */
	@Inject BinderService binderService;

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
		res.contents.binderInfo = binderService.getBinderInfo(req.docId);
	}

	/**
	 * バインダー情報の差分更新.
	 * @param req
	 * @param res
	 */
	public void save(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		final Long docId = defaults(res.docId, req.contents.docId);
		binderService.saveBinderInfo(docId, defaults(req.binderInfo, req.contents.binderInfo));
	}
}
