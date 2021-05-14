package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.designer.DesignerCodeBook.CalcItemType;
import jp.co.nci.iwf.designer.DesignerCodeBook.ItemClass;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * コンテナ一覧のEXCELダウンロード用パーツ計算式Bean
 */
public class Vd0010CalcParts extends MiscUtils {

	public Vd0010CalcParts(PartsDesign design, PartsCalc pc, Map<Long, PartsDesign> designMap, Map<String, String> logiclOperators,
			Map<String, String> parentheses, Map<String, String> comparisonOperators, Map<String, String> arithmeticOperators) {
		partsCode = design.partsCode;
		designCode = design.designCode;
		labelText = design.labelText;
		calcName = pc.partsCalcName;
		sortOrder = pc.sortOrder;
		defaultFlag = eq(CommonFlag.ON, pc.defaultFlag) ? "○" : "";
		callbackFunction = pc.callbackFunction;

		// 有効条件
		ecExpression = toEcExpression(pc, designMap, logiclOperators, parentheses, comparisonOperators);
		// 計算式
		calcExpression = toCalcExpression(pc, designMap, parentheses, arithmeticOperators);
	}

	/** 計算式 */
	private String toCalcExpression(PartsCalc pc, Map<Long, PartsDesign> designMap, Map<String, String> parentheses, Map<String, String> arithmeticOperators) {
		StringBuilder sb = new StringBuilder();
		for (PartsCalcItem item : pc.items) {
			if (eq(CalcItemType.PARTS, item.calcItemType)) {
				// パーツ
				Long partsId = Long.valueOf(item.calcItemValue);
				PartsDesign d = designMap.get(partsId);
				sb.append(d.designCode);
			}
			else if (eq(CalcItemType.LITERAL, item.calcItemType)) {
				// リテラル値
				sb.append(item.calcItemValue);
			}
			else if (eq(CalcItemType.ARITHMETIC_OPERATOR, item.calcItemType)) {
				// 算術演算子
				String val = arithmeticOperators.get(item.calcItemValue);
				sb.append(" ").append(val).append(" ");
			}
			else if (eq(CalcItemType.PARENTHESIS, item.calcItemType)) {
				// カッコ
				String val = parentheses.get(item.calcItemValue);
				sb.append(val);
			}
		}
		return sb.toString();
	}

	/** 計算式有効条件  */
	private String toEcExpression(PartsCalc pc, Map<Long, PartsDesign> designMap, Map<String, String> logiclOperators,
			Map<String, String> parentheses, Map<String, String> comparisonOperators) {
		StringBuilder sb = new StringBuilder();
		for (PartsCalcEc ec : pc.ecs) {
			if (eq(ItemClass.PARTS, ec.itemClass)) {
				// パーツ
				Long targetPartsId = Long.valueOf(ec.condType);
				String targetPartsCode = designMap.get(targetPartsId).partsCode;
				String ope = comparisonOperators.get(ec.operator);
				String val = defaults(ec.targetLiteralVal, "");
				if (eq(CommonFlag.ON, ec.numericFlag))
					sb.append(String.format("%s %s %s", targetPartsCode, ope, val));
				else
					sb.append(String.format("%s %s \"%s\"", targetPartsCode, ope, val));
			} else if (eq(ItemClass.PARENTHESIS, ec.itemClass)) {
				// カッコ
				String val = parentheses.get(ec.condType);
				sb.append(val);
			} else if (eq(ItemClass.LOGICAL_OPERATOR, ec.itemClass)){
				// 論理演算子
				String val = logiclOperators.get(ec.condType);
				sb.append(val);
			}
			sb.append(" ");
		}
		return sb.toString();
	}

	/** パーツコード */
	public String partsCode;

	/** デザインコード */
	public String designCode;

	/** 表示ラベル */
	public String labelText;

	/** 計算式名 */
	public String calcName;

	/** 計算条件順 */
	public Integer sortOrder;

	/** デフォルト(計算式有効条件の) */
	public String defaultFlag;

	/** 計算式有効条件 */
	public String ecExpression;

	/** 計算式 */
	public String calcExpression;

	/** コールバック関数 */
	public String callbackFunction;
}
