package jp.co.nci.iwf.designer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.CalculateCondition;
import jp.co.nci.iwf.designer.service.CalculateConditionItem;
import jp.co.nci.iwf.designer.service.CalculateService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 計算式に関するユーティリティクラス.
 */
@ApplicationScoped
public class PartsCalcUtils extends MiscUtils {
	/** 計算式サービス.(CDI.select()経由で逐次取得すると意外と遅いのでstaticに参照する。) */
	@Inject private CalculateService calculateService;
	/** パーツ条件に関するユーティリティクラス. */
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 計算式の生成.
	 * @param calc
	 * @return
	 */
	public String createCalcFormula(PartsCalc calc) {
		return calculateService.createCalculateFormula(calc.items);
	}

	/**
	 * 計算式の計算元パーツのパーツIDと入力値のMapを取得する
	 * @param parts 計算式が設定されてあるパーツ
	 * @param calcs 計算式定義一覧
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<Long, String> getCalcTargetPartsValueMap(PartsBase<?> parts, PartsCalc calc, DesignerContext ctx) {
		if (isNotEmpty(calc.items)) {
			Map<String, PartsBase<?>> runtimeMap = ctx.runtimeMap;
			// 計算式の計算元パーツは引数のparts自身と同等が下位コンテナ内にいる
			// なので計算元パーツのHTMLIDと計算先パーツのHTMLIDは同一のPREFIXを持っているはず
			// プレフィックスがない場合はルートコンテナ上にいるので
			final boolean hasPrefix = StringUtils.contains(parts.htmlId, "_");
			final String prefix = hasPrefix ? StringUtils.left(parts.htmlId, (parts.htmlId.lastIndexOf("_") + 1)) : "";

			Map<Long, String> map = new HashMap<>();
			for (PartsCalcItem item : calc.items) {
				if (eq(DesignerCodeBook.CalcItemType.PARTS, item.calcItemType)) {
					Long targetPartsId = Long.valueOf(item.calcItemValue);
					if (!map.containsKey(targetPartsId)) {
						BigDecimal val = null;
						// プレフィックスがなければパーツIDが同じ、ある場合はパーツIDが同じで同一のプレフィックスを持つパーツを抽出し金額を足し合わせる
						for (PartsBase<?> p : runtimeMap.values()) {
							if (eq(p.partsId, targetPartsId) && (!hasPrefix || StringUtils.startsWith(p.htmlId, prefix))) {
								BigDecimal temp = toBD(p.getValue());
								if (val == null)
									val = temp;
								else if (temp != null)
									val = val.add(temp);
							}
						}
						map.put(targetPartsId, (val == null ? "" : val.toString()));
					}
				}
			}
			return map;
		}
		return null;
	}

	/**
	 * 計算条件式の生成.
	 * @param calc
	 * @return
	 */
	public String createCalcEcFormula(PartsCalc calc) {
		return partsCondUtils.createFormula(calc.ecs);
	}

	/**
	 * 計算条件の比較対象パーツのパーツIDと入力値のMapを取得する
	 * @param parts 計算条件が設定されてあるパーツ
	 * @param ecs 計算条件
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<Long, String> getEcTargetPartsValueMap(PartsBase<?> parts, PartsCalc calc, DesignerContext ctx) {
		return partsCondUtils.getTargetPartsValueMap(parts, calc.ecs, ctx);
	}

	/**
	 * 計算式の生成.
	 * 計算式定義一覧を受け取り、計算式IDをキーにした計算式Mapを返す
	 * @param list 計算式定義一覧
	 * @return 計算式Map（Key:計算式ID）
	 */
	public Map<Long, String> createCalcFormulaMap(List<PartsCalc> list) {
		if (isNotEmpty(list)) {
			final Map<Long, String> formulas = new LinkedHashMap<>();
			list.stream()
				.forEach(c -> {
					formulas.put(c.partsCalcId, createCalcEcFormula(c));
				});
			return formulas;
		}
		return null;
	}

	/**
	 * 計算条件の評価式の生成.
	 * 計算式定義一覧を受け取り、計算式IDをキーにした計算条件式Mapを返す
	 * @param list 計算式定義一覧
	 * @return 評価式Map（Key：計算式ID）
	 */
	public Map<Long, String> createCalcEcFormulaMap(List<PartsCalc> list) {
		if (isNotEmpty(list)) {
			final Map<Long, String> formulas = new HashMap<>();
			list.stream()
				.filter(c -> isNotEmpty(c.ecs))
				.forEach(c -> {
					formulas.put(c.partsCalcId, partsCondUtils.createFormula(c.ecs));
				});
			return formulas;
		}
		return null;
	}

	/**
	 * 計算式の計算元パーツのパーツIDと入力値のMapを取得.
	 * 計算式定義一覧を受け取り、計算式ID毎の入力値Mapを返す
	 * @param parts 計算式が設定されてあるパーツ
	 * @param calcs 計算式定義一覧
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<Long, Map<Long, String>> getCalcTargetPartsValueMap(PartsBase<?> parts, List<PartsCalc> calcs, DesignerContext ctx) {
		if (isNotEmpty(calcs)) {
			final Map<Long, Map<Long, String>> values = new HashMap<Long, Map<Long, String>>();
			for (PartsCalc calc : calcs) {
				Map<Long, String> map = getCalcTargetPartsValueMap(parts, calc, ctx);
				values.put(calc.partsCalcId, map);
			}
			return values;
		}
		return null;
	}

	/**
	 * 計算条件の判定元パーツのパーツIDと入力値のMapを取得する
	 * @param parts 計算式が設定されてあるパーツ
	 * @param calcs 計算式定義一覧
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<Long, Map<Long, String>> getCalcEcTargetPartsValueMap(PartsBase<?> parts, List<PartsCalc> calcs, DesignerContext ctx) {
		if (isNotEmpty(calcs)) {
			final Map<Long, Map<Long, String>> values = new HashMap<Long, Map<Long, String>>();
			for (PartsCalc calc : calcs) {
				if (isNotEmpty(calc.ecs)) {
					Map<Long, String> map = partsCondUtils.getTargetPartsValueMap(parts, calc.ecs, ctx);
					values.put(calc.partsCalcId, map);
				}
			}
			return values;
		}
		return null;
	}

	/**
	 * パーツの計算式定義に関連する項目をレンダリング用のオブジェクトへ変換して返す
	 * @param parts
	 * @param design
	 * @param ctx
	 * @param formulas 計算式
	 * @param values 計算式内で使用する判定元パーツの(レンダリング時点の)値
	 * @param targets (パーツ自身が別のパーツの計算元となっている場合の)計算先パーツID一覧
	 * @param ecFormulas 計算条件
	 * @param ecValues 計算条件内で使用する判定元パーツの(レンダリング時点の)値
	 * @param ecTargets (パーツ自身が別のパーツの計算条件の判定元となっている場合の)計算先パーツID一覧
	 * @return
	 */
	public CalculateCondition getCalculate(PartsBase<?> parts, PartsDesign design, DesignerContext ctx) {
		if (isUseCalc(ctx, design)) {
			final CalculateCondition cc = new CalculateCondition();

			if (isNotEmpty(design.partsCalcs)) {
				final List<CalculateConditionItem> calculates = new ArrayList<>();
				for (PartsCalc calc : design.partsCalcs) {
					final CalculateConditionItem bean = new CalculateConditionItem();
					bean.formula = createCalcFormula(calc);
					bean.values  = getCalcTargetPartsValueMap(parts, calc, ctx);
					bean.ecFormula = createCalcEcFormula(calc);
					bean.ecValues  = getEcTargetPartsValueMap(parts, calc, ctx);
					bean.callbackFunction = calc.callbackFunction;
					calculates.add(bean);
				}
				cc.calculates = calculates;
			}
			if (ctx.targetCalcMap.containsKey(design.partsId)) {
				final Set<Long> targets = ctx.targetCalcMap.get(design.partsId);
				cc.targets = targets;
			}
			if (ctx.targetCalcEcMap.containsKey(design.partsId)) {
				final Set<Long> ecTargets = ctx.targetCalcEcMap.get(design.partsId);
				cc.ecTargets = ecTargets;
			}

			return cc;
		}
		return null;
	}

	/**
	 * 計算式を使用するか
	 * @param ctx
	 * @param design
	 * @return
	 */
	public boolean isUseCalc(DesignerContext ctx, PartsDesign design) {
		return ctx.renderMode != RenderMode.DESIGN
				&& (isNotEmpty(design.partsCalcs)
						|| ctx.targetCalcMap.containsKey(design.partsId)
						|| ctx.targetCalcEcMap.containsKey(design.partsId));
	}
}
