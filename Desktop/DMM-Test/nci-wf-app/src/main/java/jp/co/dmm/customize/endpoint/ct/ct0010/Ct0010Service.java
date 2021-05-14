package jp.co.dmm.customize.endpoint.ct.ct0010;

import javax.inject.Inject;

import jp.co.dmm.customize.component.catalog.CatalogService;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * カタログ検索画面サービス
 */
@BizLogic
public class Ct0010Service extends BasePagingService {
	@Inject private Ct0010Repository repository;
	@Inject private CatalogService catalogService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ct0010InitResponse init(Ct0010InitRequest req) {
		Ct0010InitResponse res = createResponse(Ct0010InitResponse.class, req);
		res.catalogCategories = catalogService.getCatalogCategories(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ct0010SearchResponse search(Ct0010SearchRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		req.corporationCode = login.getCorporationCode();
		final int allCount = repository.count(req);
		final Ct0010SearchResponse res = createResponse(Ct0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

}
