package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleRadio;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRadio;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】Radioパーツ
 */
public class PartsRadio extends PartsOption<PartsDesignRadio> implements RoleRadio {

	/** パーツにユーザデータを反映 */
	@Override
	public void fromUserData(PartsDesignRadio d, Map<String, Object> userData) {
		values.clear();
		for (PartsColumn pc : d.columns) {
			if (RADIO_CODE.equals(pc.roleCode)) {
				Object value = userData.get(pc.columnName);
				values.put(RADIO_CODE, toStr(value));
			}
		}
	}

	/** パーツからユーザデータへ値を抜き出し */
	@Override
	public void toUserData(PartsDesignRadio d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		String value = getValue();
		String label = getLabel(d);
		for (PartsColumn pc : d.columns) {
			// コード値
			if (RADIO_CODE.equals(pc.roleCode)) {
				userData.put(pc.columnName, value);
			}
			// ラベル値
			if (RADIO_LABEL.equals(pc.roleCode)) {
				userData.put(pc.columnName, label);
			}
		}
	}

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignRadio d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		final String val = getValue();

		// 必須入力
		if (checkRequired && d.requiredFlag && MiscUtils.isEmpty(val)) {
			return new PartsValidationResult(htmlId, RADIO_CODE, d.labelText, MessageCd.MSG0074);
		}

		return null;
	}
}
