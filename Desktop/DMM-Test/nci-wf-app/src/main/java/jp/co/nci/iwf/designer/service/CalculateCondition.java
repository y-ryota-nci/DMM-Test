package jp.co.nci.iwf.designer.service;

import java.util.List;
import java.util.Set;

public class CalculateCondition {

	/** 計算式一覧 */
	public List<CalculateConditionItem> calculates;
	/** (パーツ自身が別のパーツの計算元となっている場合の)計算先パーツID一覧 */
	public Set<Long> targets;
	/** (パーツ自身が別のパーツの計算条件の判定元となっている場合の)計算先パーツID一覧 */
	public Set<Long> ecTargets;
}
