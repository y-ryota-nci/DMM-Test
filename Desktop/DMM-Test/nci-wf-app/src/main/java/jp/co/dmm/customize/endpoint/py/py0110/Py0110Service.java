package jp.co.dmm.customize.endpoint.py.py0110;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 前払選択サービス
 */
@ApplicationScoped
public class Py0110Service extends BasePagingService {
	@Inject private Py0110Repository repository;


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
	public BaseResponse search(Py0110SearchRequest req) {
		final int allCount = repository.count(req);
		final Py0110SearchResponse res = createResponse(Py0110SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
