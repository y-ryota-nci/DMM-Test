package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleMasterParts;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignMaster;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】汎用マスタパーツ
 * V5に存在していた「ボタン押下で汎用テーブル検索ポップアップを開く機能」はボタンパーツへ移管された。
 * V6では動的に選択肢を変えるドロップダウンリストである。
 */
public class PartsMaster extends PartsAjax<PartsDesignMaster> implements RoleMasterParts {
	/** 空行区分 */
	public int emptyLineType;
	/** カラム名：値 */
	public String columnNameValue;
	/** カラム名：ラベル */
	public String columnNameLabel;

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignMaster d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		if (checkRequired && d.requiredFlag && isEmpty(values.get(CODE))) {
			return new PartsValidationResult(htmlId, CODE, d.labelText, MessageCd.MSG0074);
		}
		return null;
	}

	/** パーツにユーザデータを反映 */
	@Override
	public void fromUserData(PartsDesignMaster d, Map<String, Object> userData) {
		values.clear();
		for (PartsColumn pc : d.columns) {
			final Object value = userData.get(pc.columnName);
			values.put(pc.roleCode, isEmpty(value) ? "" : value.toString());
		}
	}

	/** パーツからユーザデータへ値を抜き出し */
	@Override
	public void toUserData(PartsDesignMaster d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		// ユーザデータへ反映
		for (PartsColumn pc : d.columns) {
			userData.put(pc.columnName, values.get(pc.roleCode));
		}
	}
}
