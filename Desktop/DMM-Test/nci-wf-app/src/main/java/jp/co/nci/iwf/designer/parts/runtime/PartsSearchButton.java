package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignSearchButton;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】検索ボタンパーツ。
 * V5で汎用マスタパーツが有していた「ボタン押下で汎用テーブル検索ポップアップを開く機能」は当パーツへ移管された。
 */
public class PartsSearchButton extends PartsAjax<PartsDesignSearchButton> {

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignSearchButton d, DesignerContext ctx, boolean checkRequired,
			Map<String, EvaluateCondition> ecResults) {
		return null;
	}
}
