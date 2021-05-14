package jp.co.nci.iwf.endpoint.vd.vd0031;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面設定の初期化レスポンス
 */
public class Vd0031InitResponse extends BaseResponse {
	/** 画面定義 */
	public MwmScreen screen;
	/** スクラッチ区分の選択肢 */
	public List<OptionItem> scratchFlags;
	/** コンテナの選択肢 */
	public List<OptionItem> containers;
	/** トレイタイプの選択肢 */
	public List<OptionItem> trayTypes;
	/** 表示条件の選択肢 */
	public List<OptionItem> dcList;
	/** 画面パーツ条件定義Map */
	public Map<Long, List<PartsCond>> condMap;
	/** 画面計算式定義Map */
	public Map<Long, List<PartsCalc>> calcMap;
	/** 画面Javascript定義 */
	public List<PartsJavascript> scripts;
}
