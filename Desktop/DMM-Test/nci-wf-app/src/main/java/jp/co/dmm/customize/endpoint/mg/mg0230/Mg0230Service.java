package jp.co.dmm.customize.endpoint.mg.mg0230;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 支払条件マスタサーサービス
 *
 */
@ApplicationScoped
public class Mg0230Service extends BasePagingService  {


	@Inject private Mg0230Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mg0230SearchResponse init(Mg0230SearchRequest req) {
		final Mg0230SearchResponse res = createResponse(Mg0230SearchResponse.class, req);
		LoginInfo u = sessionHolder.getLoginInfo();
		res.companyCd = u.getCorporationCode();
		res.companyItems = repository.getCompanyItems(u.getUserAddedInfo(), u.getLocaleCode());
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mg0230SearchResponse search(Mg0230SearchRequest req) {

		LoginInfo u = sessionHolder.getLoginInfo();

		if (StringUtils.isEmpty(req.companyCd)) {
			req.companyCd = u.getCorporationCode();
		}

		final int allCount = repository.count(req);
		final Mg0230SearchResponse res = createResponse(Mg0230SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	public Mg0230RemoveResponse delete(Mg0230RemoveRequest req) {
		final Mg0230RemoveResponse res = createResponse(Mg0230RemoveResponse.class, req);
		repository.delete(req);
		res.success = true;
		return res;
	}
}
