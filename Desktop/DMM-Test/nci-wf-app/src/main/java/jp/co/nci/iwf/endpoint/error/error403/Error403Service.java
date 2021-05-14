package jp.co.nci.iwf.endpoint.error.error403;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * HTTP 403 Forbiddenエラー時のサービス
 * （アクセス権限なしエラー）
 */
@BizLogic
public class Error403Service extends BaseService {
	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}
}
