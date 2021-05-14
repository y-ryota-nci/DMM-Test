package jp.co.nci.iwf.designer.service;

import java.util.Map;

/**
 * 有効条件、可視条件、読取専用条件の条件クラス.
 */
public class EvaluateConditionItem {

	/** パーツ条件区分 */
	public String partsConditionType;
	/** コールバック関数 */
	public String callbackFunction;
	/** 条件式 */
	public String formula;
	/** 条件式に渡すパーツの入力値Map */
	public Map<Long, String> values;
}
