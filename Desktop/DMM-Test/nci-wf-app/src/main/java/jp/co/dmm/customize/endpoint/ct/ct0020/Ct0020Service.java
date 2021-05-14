package jp.co.dmm.customize.endpoint.ct.ct0020;

import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.dmm.customize.component.catalog.CatalogService;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * カタログ選択画面サービス
 */
@BizLogic
public class Ct0020Service extends BasePagingService {
	@Inject private Ct0020Repository repository;
	@Inject private CatalogService catalogService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Ct0020InitResponse init(Ct0020InitRequest req) {
		Ct0020InitResponse res = createResponse(Ct0020InitResponse.class, req);
		res.catalogCategories = catalogService.getCatalogCategories(true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Ct0020SearchResponse search(Ct0020SearchRequest req) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		req.corporationCode = login.getCorporationCode();
		final int allCount = repository.count(req);
		final Ct0020SearchResponse res = createResponse(Ct0020SearchResponse.class, req, allCount);
		res.results = repository.select(req, res).stream().map(r -> {
			if (isNotEmpty(r.remarks))
				r.escapeRemarks = escapeHtmlBR(r.remarks);
			return r;
		}).collect(Collectors.toList());

		res.success = true;
		return res;
	}

}
