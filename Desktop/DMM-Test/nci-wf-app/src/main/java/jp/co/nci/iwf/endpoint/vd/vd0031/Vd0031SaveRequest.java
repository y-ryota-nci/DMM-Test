package jp.co.nci.iwf.endpoint.vd.vd0031;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面設定の更新リクエスト
 */
public class Vd0031SaveRequest extends BaseRequest {
	/** 画面定義 */
	public MwmScreen screen;
	/** 画面パーツ条件定義 */
	public Map<Long, List<PartsCond>> condMap;
	/** 画面計算式定義 */
	public Map<Long, List<PartsCalc>> calcMap;
	/** 画面Javascript定義 */
	public List<PartsJavascript> scripts;
}
