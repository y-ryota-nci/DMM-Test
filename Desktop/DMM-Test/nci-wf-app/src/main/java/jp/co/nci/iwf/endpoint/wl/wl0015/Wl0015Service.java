package jp.co.nci.iwf.endpoint.wl.wl0015;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

import jp.co.nci.iwf.endpoint.wl.wl0010.Wl0010Request;
import jp.co.nci.iwf.endpoint.wl.wl0010.Wl0010Response;
import jp.co.nci.iwf.endpoint.wl.wl0010.Wl0010Service;

/**
 * トレイ設定一覧（個人）サービス
 */
@ApplicationScoped
@Typed(Wl0015Service.class)
public class Wl0015Service extends Wl0010Service {

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Wl0010Response search(Wl0010Request req) {
		// 操作者の所有するトレイ設定だけを抽出されるため、暗黙の絞込条件を追加
		req.ownerUserCode = sessionHolder.getLoginInfo().getUserCode();
		return super.search(req);
	}
}
