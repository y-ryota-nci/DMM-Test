package jp.co.nci.iwf.endpoint.dc.dc0100.include;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 文書(業務文書)ブロック：業務文書のサービス
 */
@BizLogic
public class DcBl0001Service extends BaseService implements CodeMaster {

	/**
	 * 初期化.
	 * @param req
	 * @param res
	 */
	public void init(Dc0100InitRequest req, Dc0100InitResponse res) {
	}

}
