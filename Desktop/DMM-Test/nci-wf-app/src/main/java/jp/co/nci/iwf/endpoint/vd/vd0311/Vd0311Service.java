package jp.co.nci.iwf.endpoint.vd.vd0311;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 申請・承認完了画面（処理結果画面）サービス
 */
@BizLogic
public class Vd0311Service extends BaseService implements CodeMaster {

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

}
