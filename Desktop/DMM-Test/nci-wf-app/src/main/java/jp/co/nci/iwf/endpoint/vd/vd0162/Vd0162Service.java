package jp.co.nci.iwf.endpoint.vd.vd0162;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * モバイル設定リスト
 */
@BizLogic
public class Vd0162Service extends BaseService {

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
