package jp.co.nci.iwf.endpoint.vd.vd0034;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/** 画面属性コピー設定画面のレスポンス */
public class Vd0034Response extends BaseResponse {
	/** コピー元画面の選択肢 */
	public List<OptionItem> screenIds;
	/** コピー元画面のパーツ一覧 */
	public List<Vd0034Entity> results;

	/** コピー結果のパーツ条件Map */
	public Map<Long, List<PartsCond>> destCondMap;
	/** コピー結果の計算式Map */
	public Map<Long, List<PartsCalc>> destCalcMap;
}
