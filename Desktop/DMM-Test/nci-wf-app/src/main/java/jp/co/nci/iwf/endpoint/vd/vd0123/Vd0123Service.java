package jp.co.nci.iwf.endpoint.vd.vd0123;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 外部Javascript参照設定サービス
 */
@BizLogic
public class Vd0123Service extends BaseService {
	@Inject private Vd0123Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0123InitResponse init(Vd0123InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// Javascript一覧を抽出し、コンテナが参照しているJavascriptとマージ
		final List<Vd0123Entity> allScripts = repository.getMwmJavascript(corporationCode, localeCode);
		merge(allScripts, req.ctx.root.javascripts);

		final Vd0123InitResponse res = createResponse(Vd0123InitResponse.class, req);
		res.scripts = allScripts;
		res.success = true;
		return res;
	}

	/** Javascript一覧に現在コンテナが参照しているものを反映 */
	private void merge(List<Vd0123Entity> rows, List<PartsJavascript> javascripts) {

		// 現在参照しているスクリプトをJavascriptIdをキーにMap化
		final Map<Long, Integer> map = new HashMap<>();
		javascripts.forEach(js -> map.put(js.javascriptId, js.sortOrder));

		// Javascript一覧に現在参照しているスクリプトを反映
		for (Vd0123Entity row : rows) {
			if (map.containsKey(row.javascriptId)) {
				row.selected = true;
				row.sortOrder = map.get(row.javascriptId);
			}
			else {
				row.selected = false;
				row.sortOrder = null;
			}
		}
		// 並び順でソート
		Collections.sort(rows);
	}
}
