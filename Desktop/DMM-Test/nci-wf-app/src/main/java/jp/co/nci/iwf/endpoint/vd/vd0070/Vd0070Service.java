package jp.co.nci.iwf.endpoint.vd.vd0070;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 添付ファイルパーツによるパーツアップロード画面サービス
 */
@BizLogic
public class Vd0070Service extends BaseService {

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
