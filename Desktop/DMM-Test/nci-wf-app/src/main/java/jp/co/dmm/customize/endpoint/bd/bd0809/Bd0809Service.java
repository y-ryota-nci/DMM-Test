package jp.co.dmm.customize.endpoint.bd.bd0809;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 予算分析明細確認画面サービス
 */
@BizLogic
public class Bd0809Service extends BasePagingService {
	@Inject private Bd0809Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Bd0809InitRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BasePagingResponse search(Bd0809Request req) {
		final int allCount = repository.count(req);
		final BasePagingResponse res = createResponse(BasePagingResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
