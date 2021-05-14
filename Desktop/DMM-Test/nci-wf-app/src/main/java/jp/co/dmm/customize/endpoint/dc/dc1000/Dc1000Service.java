package jp.co.dmm.customize.endpoint.dc.dc1000;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * OCR状況一覧サービス
 */
@ApplicationScoped
public class Dc1000Service extends BasePagingService {
	@Inject private Dc1000Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Dc1000Response res = createResponse(Dc1000Response.class, req);
		final LoginInfo loginInfo = sessionHolder.getLoginInfo();
		res.ocrFlagItems = repository.getOptionItems(loginInfo.getCorporationCode(), loginInfo.getLocaleCode(), true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Dc1000Request req) {
		final int allCount = repository.count(req);
		final Dc1000Response res = createResponse(Dc1000Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

}
