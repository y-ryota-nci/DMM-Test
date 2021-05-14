package jp.co.nci.iwf.endpoint.al.al0011;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * アクセスログ詳細サービス
 */
@BizLogic
public class Al0011Service extends BaseService {
	@Inject
	private Al0011Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Al0011InitResponse init(Al0011InitRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Al0011InitResponse res = createResponse(Al0011InitResponse.class, req);

		// アクセスログ
		res.accessLog = repository.get(req.accessLogId, localeCode);

		// アクセスログ詳細
		res.details = repository.getDetails(req.accessLogId, localeCode);

		res.success = true;
		return res;
	}

}
