package jp.co.nci.iwf.designer.service;

import java.util.Map;

public class CalculateConditionItem {

	/** 計算式 */
	public String formula;
	/** 計算式に渡すパーツの入力値Map */
	public Map<Long, String> values;
	/** 計算条件評価式 */
	public String ecFormula;
	/** 計算条件評価式に渡すパーツの入力値Map */
	public Map<Long, String> ecValues;
	/** 計算後に実行されるコールバック関数 */
	public String callbackFunction;
}
