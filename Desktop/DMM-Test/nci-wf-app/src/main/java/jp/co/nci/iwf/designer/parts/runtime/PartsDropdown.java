package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleDropdown;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignDropdown;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】Dropdownパーツ
 */
public class PartsDropdown extends PartsOption<PartsDesignDropdown> implements RoleDropdown {

	/** パーツにユーザデータを反映 */
	@Override
	public void fromUserData(PartsDesignDropdown d, Map<String, Object> userData) {
		values.clear();
		for (PartsColumn pc : d.columns) {
			Object value = userData.get(pc.columnName);
			values.put(pc.roleCode, toStr(value));
		}
	}

	/** パーツからユーザデータへ値を抜き出し */
	@Override
	public void toUserData(PartsDesignDropdown d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		String value = getValue();
		String label = getLabel(d);
		for (PartsColumn pc : d.columns) {
			// コード値
			if (DROPDOWN_CODE.equals(pc.roleCode)) {
				userData.put(pc.columnName, value);
			}
			// ラベル値
			if (DROPDOWN_LABEL.equals(pc.roleCode)) {
				userData.put(pc.columnName, label);
			}
		}
	}

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignDropdown d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		final String val = getValue();

		// 必須入力
		if (checkRequired && d.requiredFlag && MiscUtils.isEmpty(val)) {
			return new PartsValidationResult(htmlId, DROPDOWN_CODE, d.labelText, MessageCd.MSG0074);
		}

		return null;
	}
}
