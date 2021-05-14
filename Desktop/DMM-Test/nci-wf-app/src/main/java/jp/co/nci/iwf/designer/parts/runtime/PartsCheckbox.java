package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleCheckbox;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignCheckbox;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】Checkboxパーツ
 */
public class PartsCheckbox extends PartsBase<PartsDesignCheckbox> implements RoleCheckbox {

	/** パーツからユーザデータへ値を抜き出し */
	@Override
	public void toUserData(PartsDesignCheckbox d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		for (PartsColumn pc : d.columns) {
			String value = values.get(pc.roleCode);
			value = eq(value, d.checkedValue) ? d.checkedValue : d.uncheckedValue;
			userData.put(pc.columnName, value);
		}
	}

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignCheckbox d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		final String val = values.get(CHECK);

		// 必須入力
		if (checkRequired && d.requiredFlag && MiscUtils.isEmpty(val)) {
			return new PartsValidationResult(htmlId, CHECK, d.labelText, MessageCd.MSG0074);
		}

		return null;
	}

	/** 現状の値をクリアしてデフォルト値をセット */
	@Override
	public void clearAndSetDefaultValue(PartsDesign d) {
		values.clear();

		// デフォルト値
		final PartsDesignCheckbox design = (PartsDesignCheckbox)d;
		final String value = CommonFlag.ON.equals(design.defaultValue)
				? design.checkedValue : design.uncheckedValue;
		values.put(defaultRoleCode, value);
	}
}
