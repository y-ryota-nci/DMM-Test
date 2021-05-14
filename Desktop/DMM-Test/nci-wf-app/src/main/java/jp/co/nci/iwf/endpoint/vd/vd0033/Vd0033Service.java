package jp.co.nci.iwf.endpoint.vd.vd0033;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 画面Javascript設定画面サービス
 */
@BizLogic
public class Vd0033Service extends BaseService {
	@Inject private Vd0033Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0033InitResponse init(Vd0033InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// Javascriptマスタを抽出
		final List<Vd0033Entity> allScripts = repository.getMwmJavascript(corporationCode, localeCode);
		// 画面が参照しているJavascriptとマージ
		merge(allScripts, req.scripts);

		final Vd0033InitResponse res = createResponse(Vd0033InitResponse.class, req);
		res.scripts = allScripts;
		res.success = true;
		return res;
	}

	private void merge(List<Vd0033Entity> allScripts, List<PartsJavascript> screenJavascripts) {
		// 現在参照しているスクリプトをJavascriptIdをキーにMap化
		final Map<Long, Integer> map = new HashMap<>();
		screenJavascripts.forEach(js -> map.put(js.javascriptId, js.sortOrder));

		// Javascript一覧に現在参照しているスクリプトを反映
		for (Vd0033Entity allScript : allScripts) {
			allScript.selected = map.containsKey(allScript.javascriptId);
			allScript.sortOrder = map.get(allScript.javascriptId);
		}
		// 並び順でソート
		Collections.sort(allScripts);
	}

	/**
	 * 保存
	 * @param req
	 * @return
	 */
	public BaseResponse save(Vd0033SaveRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}
}
