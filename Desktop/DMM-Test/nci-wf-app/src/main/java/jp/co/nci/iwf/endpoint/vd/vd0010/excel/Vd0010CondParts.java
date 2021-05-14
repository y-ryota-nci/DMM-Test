package jp.co.nci.iwf.endpoint.vd.vd0010.excel;

import java.util.Map;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.designer.DesignerCodeBook.ItemClass;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * コンテナ一覧のEXCELダウンロード用パーツ条件Bean
 */
public class Vd0010CondParts extends MiscUtils {
	/**
	 * コンストラクタ
	 * @param p パーツ
	 * @param designMap
	 * @param logiclOperators 論理演算子Map
	 * @param parentheses カッコMap
	 * @param comparisonOperators 比較演算子Map
	 */
	public Vd0010CondParts(PartsDesign p, PartsCond pc,  Map<Long, PartsDesign> designMap, Map<String, String> logiclOperators, Map<String, String> parentheses, Map<String, String> comparisonOperators) {
		partsCode = p.partsCode;
		designCode = p.designCode;
		labelText = p.labelText;
		partsConditionTypeName = pc.partsConditionTypeName;
		callbackFunction = pc.callbackFunction;
		sortOrder = pc.sortOrder;

		// 条件式
		expression = toExpression(pc, designMap, logiclOperators, parentheses, comparisonOperators);
	}

	/** パーツ条件式  */
	private String toExpression(PartsCond pc, Map<Long, PartsDesign> designMap, Map<String, String> logiclOperators,
			Map<String, String> parentheses, Map<String, String> comparisonOperators) {
		StringBuilder sb = new StringBuilder();
		for (PartsCondItem item : pc.items) {
			if (eq(ItemClass.PARTS, item.itemClass)) {
				Long targetPartsId = Long.valueOf(item.condType);
				String targetPartsCode = designMap.get(targetPartsId).partsCode;
				String ope = comparisonOperators.get(item.operator);
				String val = defaults(item.targetLiteralVal, "");
				if (eq(CommonFlag.ON, item.numericFlag))
					sb.append(String.format("%s %s %s", targetPartsCode, ope, val));
				else
					sb.append(String.format("%s %s \"%s\"", targetPartsCode, ope, val));
			} else if (eq(ItemClass.PARENTHESIS, item.itemClass)) {
				String ope = parentheses.get(item.condType);
				sb.append(ope);
			} else {
				String ope = logiclOperators.get(item.condType);
				sb.append(ope);
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

	/** パーツ条件区分名 */
	public String partsConditionTypeName;

	/** コールバック関数 */
	public String callbackFunction;

	/** ソート順 */
	public Integer sortOrder;

	/** 条件式 */
	public String expression;
}
