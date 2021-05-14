package jp.co.dmm.customize.endpoint.py.py0100;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 支払依頼対象選択画面サービス
 */
@BizLogic
public class Py0100Service extends BasePagingService {
	@Inject private Py0100Repository repository;

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

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Py0100Request req) {
		final int allCount = repository.count(req);
		final Py0100Response res = createResponse(Py0100Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 選択データ取得
	 * @param req
	 * @return
	 */
	public BaseResponse get(Py0100Request req) {
		final Py0100Response res = createResponse(Py0100Response.class, req);
		res.results = repository.getSelectedDataList(req, res);
		res.success = true;
		return res;
	}
}
