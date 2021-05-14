package jp.co.dmm.customize.endpoint.po.po0020;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 管理_定期発注マスタ一覧 サービス
 */
@ApplicationScoped
public class Po0020Service extends BasePagingService {
	@Inject private Po0020Repository repository;


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

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BasePagingResponse search(Po0020SearchRequest req) {
		final int allCount = repository.count(req);
		final BasePagingResponse res = createResponse(BasePagingResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

}
