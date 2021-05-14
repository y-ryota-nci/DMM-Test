package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStamp;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】スタンプパーツ
 */
public class PartsStamp extends PartsBase<PartsDesignStamp> {

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignStamp d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		return null;
	}
}
