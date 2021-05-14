package jp.co.dmm.customize.endpoint.sp.sp0040;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 反社情報選択サービス
 */
@BizLogic
public class Sp0040Service extends BasePagingService {
	@Inject private Sp0040Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Sp0040Request req) {
		final Sp0040Response res = createResponse(Sp0040Response.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Sp0040Response search(Sp0040Request req) {
		final int allCount = repository.count(req);
		final Sp0040Response res = createResponse(Sp0040Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

}
