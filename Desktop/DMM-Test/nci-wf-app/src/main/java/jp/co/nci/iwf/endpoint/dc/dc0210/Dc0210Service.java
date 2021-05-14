package jp.co.nci.iwf.endpoint.dc.dc0210;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0240.Dc0240Request;
import jp.co.nci.iwf.endpoint.dc.dc0240.Dc0240Response;
import jp.co.nci.iwf.endpoint.dc.dc0240.Dc0240Service;

/**
 * 文書トレイ一覧(個人用)サービス
 */
@BizLogic
@Typed(Dc0210Service.class)
public class Dc0210Service extends Dc0240Service {
	@Inject private Dc0210Repository repository;

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Dc0240Response search(Dc0240Request req) {
		// 自身が所有しているトレイ設定だけを抽出させる
		req.ownerUserCode = sessionHolder.getLoginInfo().getUserCode();

		final int allCount = repository.count(req);
		final Dc0240Response res = createResponse(Dc0240Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
