package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignEventButton;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】イベントボタンパーツ。
 * V5でJavascriptボタンの後継パーツ。単にイベントのトリガーとするためのボタンである
 */
public class PartsEventButton extends PartsBase<PartsDesignEventButton> {

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignEventButton d, DesignerContext ctx, boolean checkRequired,
			Map<String, EvaluateCondition> ecResults) {
		return null;
	}
}
