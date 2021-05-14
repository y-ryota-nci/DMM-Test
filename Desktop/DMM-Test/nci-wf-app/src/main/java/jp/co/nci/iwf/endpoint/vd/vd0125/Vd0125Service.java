package jp.co.nci.iwf.endpoint.vd.vd0125;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * パーツイベント設定サービス
 */
@ApplicationScoped
public class Vd0125Service extends BaseService {
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
