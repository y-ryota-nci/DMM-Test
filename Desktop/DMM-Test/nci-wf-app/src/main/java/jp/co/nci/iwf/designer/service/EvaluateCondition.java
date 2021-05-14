package jp.co.nci.iwf.designer.service;

import java.util.List;
import java.util.Set;

/**
 * 条件評価結果クラス.
 */
public class EvaluateCondition {

	/** 条件式一覧 */
	public List<EvaluateConditionItem> conditions;
	/** 条件判定先パーツID一覧 */
	public Set<Long> targets;

	/** パーツの有効・無効フラグ */
	public boolean enabled = true;
	/** パーツの可視フラグ */
	public boolean visibled = true;
	/** パーツの読取専用フラグ */
	public boolean readonly = false;
	/** 条件による制御フラグ */
	public boolean control = false;
}
