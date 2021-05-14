package jp.co.dmm.customize.endpoint.pr.pr0010;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 管理_購入依頼サービス
 */
@ApplicationScoped
public class Pr0010Service extends BasePagingService {
	@Inject private SessionHolder sessionHolder;
	@Inject private Pr0010Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Pr0010InitResponse res = createResponse(Pr0010InitResponse.class, req);
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.prcFldTpItems = repository.getOptionItems(loginInfo.getCorporationCode(), loginInfo.getLocaleCode(), true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Pr0010SearchRequest req) {
		final int allCount = repository.count(req);
		final Pr0010SearchResponse res = createResponse(Pr0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

}