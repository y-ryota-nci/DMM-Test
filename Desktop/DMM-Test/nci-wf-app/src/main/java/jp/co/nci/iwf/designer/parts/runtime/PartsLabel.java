package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignLabel;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;

/**
 * 【実行時】Labelパーツ
 */
public class PartsLabel extends PartsBase<PartsDesignLabel> {

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignLabel d, DesignerContext ctx, boolean required, Map<String, EvaluateCondition> ecResults) {
		return null;
	}
}
