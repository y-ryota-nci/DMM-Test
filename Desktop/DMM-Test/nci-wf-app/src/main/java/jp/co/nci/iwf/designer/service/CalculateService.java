package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.DesignerCodeBook.CalcItemType;
import jp.co.nci.iwf.designer.DesignerCodeBook.ItemClass;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 計算式サービス.
 */
@BizLogic
public class CalculateService extends BaseRepository {
	/** 条件評価サービス. */
	@Inject private EvaluateConditionService evaluateConditionService;
	/** パーツ条件に関するユーティリティクラス. */
	@Inject private PartsCondUtils partsCondUtils;

	/** ScriptEngine */
	private ScriptEngine engine;

	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(CalculateService.class);

	/**
	 * イニシャライザ
	 */
	@PostConstruct
	public void init() {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
	}

	/**
	 * 計算式の生成.
	 * @param items 計算項目一覧
	 * @return 計算式
	 */
	public String createCalculateFormula(List<PartsCalcItem> items) {
		StringBuilder formula = new StringBuilder();
		if (items != null) {
			for (PartsCalcItem item : items) {
				if (eq(CalcItemType.ARITHMETIC_OPERATOR, item.calcItemType)
						|| eq(CalcItemType.PARENTHESIS, item.calcItemType)
						|| eq(CalcItemType.LITERAL, item.calcItemType)) {
					formula.append(item.calcItemValue);
				} else if (eq(CalcItemType.PARTS, item.calcItemType)) {
					if (item.forceCalcFlag)
						formula.append("({" + item.calcItemValue + "}+0)");	// (パーツ値+0)とすることで、パーツ値=''でも0として強制計算を実現
					else
						formula.append("{" + item.calcItemValue + "}");
				}
			}
		}
		return formula.toString();
	}

	/**
	 * 計算式の評価.
	 * @param formula 評価式
	 * @param values 入力値
	 * @return 評価結果
	 * @throws ScriptException スクリプトエラーが発生した場合
	 */
	public Object verify(String formula, Map<Long, String> values) throws ScriptException {
		final List<String> targets = new ArrayList<String>();
		final List<String> replace = new ArrayList<String>();
		values.keySet().stream().forEach(k -> {
			targets.add("{" + k + "}");
			replace.add(values.get(k));
		});
		String function = StringUtils.replaceEach(formula,
				targets.toArray(new String[targets.size()]),
				replace.toArray(new String[replace.size()]));
		Object result = engine.eval(function);
		return result;
	}

	/**
	 * 計算式と入力値を受け取り、計算を行う.
	 * なお計算式、入力値がなければNullを戻す
	 * @param formula 計算式
	 * @param values 入力値
	 * @return 計算結果
	 */
	public String calculate(String formula, Map<Long, String> values) {
		try {
			if (isNotEmpty(formula) && isNotEmpty(values)) {
				Object result = this.verify(formula, values);
				return result.toString();
			}
			return null;
		} catch (ScriptException e) {
			throw new InternalServerErrorException("計算処理にてスクリプトエラー発生！", e);
		}
	}

	/**
	 * 計算式の計算元パーツIDからみた計算先パーツのパーツID一覧Map。
	 * （計算式のトリガーパーツIDとそのパーツが変更されたことで影響を受けるパーツIDでMap化）
	 * @param designMap
	 * @return
	 */
	public Map<Long, Set<Long>> createCalcTargetMap(Map<Long, PartsDesign> designMap) {
		final Map<Long, Set<Long>> result = new HashMap<Long, Set<Long>>();
		for (PartsDesign design : designMap.values()) {
			for (PartsCalc calc : design.partsCalcs) {
				for (PartsCalcItem item : calc.items) {
					if (eq(CalcItemType.PARTS, item.calcItemType)) {
						Long partsId = Long.valueOf(item.calcItemValue);
						if (!result.containsKey(partsId)) {
							result.put(partsId, new HashSet<Long>());
						}
						result.get(partsId).add(calc.partsId);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 計算条件の判定先パーツIDからみた判定元パーツのパーツID一覧Map。
	 * （計算条件のトリガーパーツIDとそのパーツが変更されたことで影響を受けるパーツIDでMap化）
	 * @param designMap
	 * @return
	 */
	public Map<Long, Set<Long>> createCalcEcTargetMap(Map<Long, PartsDesign> designMap) {
		final Map<Long, Set<Long>> result = new HashMap<Long, Set<Long>>();
		for (PartsDesign design : designMap.values()) {
			for (PartsCalc calc : design.partsCalcs) {
				for (PartsCondItem ec : calc.ecs) {
					if (eq(ItemClass.PARTS, ec.itemClass)) {
						Long partsId = Long.valueOf(ec.condType);
						if (!result.containsKey(partsId)) {
							result.put(partsId, new HashSet<Long>());
						}
						result.get(partsId).add(calc.partsId);
						if (ec.targetPartsId != null) {
							if (!result.containsKey(ec.targetPartsId)) {
								result.put(ec.targetPartsId, new HashSet<Long>());
							}
							result.get(ec.targetPartsId).add(calc.partsId);
						}
					}
				}
			}
		}
		return result;
	}

	/** 全パーツの再計算を実行 */
	public void calculateAll(DesignerContext ctx, final Map<String, EvaluateCondition> ecCache) {
		final long start = System.currentTimeMillis();

		if (ecCache.isEmpty()) {
			ecCache.putAll(partsCondUtils.createEcResults(ctx));
		}

		// パーツIDをキーとしたパーツMap
		final Map<Long, List<PartsBase<?>>> rtMapByPartsId = ctx.runtimeMap.values().stream()
			.collect(Collectors.groupingBy(rt -> rt.partsId, Collectors.toList()));

		// 計算対象パーツリスト
		List<PartsBase<?>> targets = ctx.runtimeMap.values().stream()
			// 計算式のあるものだけが対象
			.filter(rt -> rt.calculateCondition != null)
			.filter(rt -> rt.calculateCondition.calculates != null)
			.filter(rt -> !rt.calculateCondition.calculates.isEmpty())
			// 有効なパーツであること
			.filter(rt -> ecCache.containsKey(rt.htmlId) && ecCache.get(rt.htmlId).enabled)
			// 計算式の依存関係でソート
			.sorted((rt1, rt2) -> {
				Set<Long> triggerPartsIds = rt1.calculateCondition.targets;
				if (triggerPartsIds != null && !triggerPartsIds.isEmpty()) {
					for (long triggerPartsId : triggerPartsIds) {
						// 自分が計算元で相手が計算先なら、自分が計算根拠なので手前
						if (eq(triggerPartsId, rt2.partsId)) {
							return -1;
						}
					}
				}
				// 依存関係がなければHTML_ID順
				return compareTo(rt1.htmlId, rt2.htmlId);
			})
			.collect(Collectors.toList());

		for (PartsBase<?> rt : targets) {
			for (CalculateConditionItem cci : rt.calculateCondition.calculates) {
				// 有効な計算式
				if (!evaluateConditionService.evaluate(cci.ecFormula, cci.ecValues)) {
					continue;
				}

				// 計算実行
				String newVal = calculate(cci.formula, cci.values);
				if (!same(newVal, rt.getValue())) {
					rt.setValue(newVal);

					// 計算結果が変更されたので、当パーツを計算式の一部としているパーツに対して値を反映する
					for (Long partsId : rt.calculateCondition.targets) {
						for (PartsBase<?> p : rtMapByPartsId.get(partsId)) {
							p.calculateCondition.calculates.stream().forEach(item -> {
								if (item.values == null)
									item.values = new HashMap<>();
								item.values.put (rt.partsId, newVal);
							});
						}
					}
					// 計算結果が変更されたので、当パーツを計算式有効条件の一部としているパーツに対して値を反映する
					for (Long partsId : rt.calculateCondition.ecTargets) {
						for (PartsBase<?> p : rtMapByPartsId.get(partsId)) {
							p.calculateCondition.calculates.forEach(item -> {
								if (item.ecValues == null)
									item.ecValues = new HashMap<>();
								item.ecValues.put (rt.partsId, newVal);
							});
						}
					}
					// 計算結果が変更されたので、当パーツを有効条件の一部としているパーツに対して値を反映する
					ecCache.get(rt.htmlId).conditions.stream().forEach(eci -> {
						if (eci.values == null) {
							eci.values = new HashMap<>();
						}
						eci.values.put(rt.partsId, newVal);
					});
				}
				break;
			}
		}
		log.debug("calculateAll() ---> {}msec", System.currentTimeMillis() - start);
	}
}
