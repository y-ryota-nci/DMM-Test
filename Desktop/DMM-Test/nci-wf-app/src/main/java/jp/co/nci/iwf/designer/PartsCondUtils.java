package jp.co.nci.iwf.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsConditionType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.EvaluateConditionItem;
import jp.co.nci.iwf.designer.service.EvaluateConditionService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツ条件に関するユーティリティクラス.
 */
@ApplicationScoped
public class PartsCondUtils extends MiscUtils {
	/** 条件評価サービス */
	@Inject private EvaluateConditionService evaluateConditionService;

	/**
	 * 条件の評価式の生成.
	 * @param cond 条件定義
	 * @return 評価式
	 */
	public String createFormula(List<? extends PartsCondItem> items) {
		return evaluateConditionService.createEvaluateFormula(items);
	}

	/**
	 * 条件の評価.
	 * @param formula 評価式
	 * @param values 入力値
	 * @return 評価結果 true：有効 false：無効
	 */
	public boolean evaluate(String formula, Map<Long, String> values) {
		return evaluateConditionService.evaluate(formula, values);
	}

	/**
	 * 条件の比較対象パーツのパーツIDと入力値のMapを取得する
	 * @param parts 有効条件が設定されてあるパーツ
	 * @param items 有効条件
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<Long, String> getTargetPartsValueMap(PartsBase<?> parts, List<? extends PartsCondItem> items, DesignerContext ctx) {
		if (isEmpty(items)) {
			return null;
		}

		Map<String, PartsBase<?>> runtimeMap = ctx.runtimeMap;
		// 戻り値
		Map<Long, String> map = new HashMap<Long, String>();
		for (PartsCondItem ec : items) {
			if (eq(DesignerCodeBook.ItemClass.PARTS, ec.itemClass)) {
				Long targetPartsId = Long.valueOf(ec.condType);
				if (!map.containsKey(targetPartsId)) {
					// 同一のパーツIDを持つパーツを取得
					Map<String, PartsBase<?>> tmpMap =
							runtimeMap.values().stream()
								.filter(v -> eq(targetPartsId, v.partsId))
								.collect(Collectors.toMap(v -> v.htmlId, v -> v));

					PartsBase<?> target = null;
					for (String htmlId : tmpMap.keySet()) {
						if (htmlId.indexOf("_") < 0) {
							target = tmpMap.get(htmlId);
						} else {
							int idx = htmlId.lastIndexOf("_");
							String prefix = StringUtils.left(htmlId,  (idx + 1));
							if (parts.htmlId.startsWith(prefix)) {
								target = tmpMap.get(htmlId);
							}
						}
						if (target != null) {
							map.put(target.partsId, target.getValue());
							break;
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 親コンテナの有効・無効の判定結果を戻す.
	 * @param parts
	 * @param design
	 * @param ctx
	 * @param ecCache 親コンテナの有効・無効の判定結果
	 * @return 有効ならtrue
	 */
	public EvaluateCondition isParentEnabled(PartsBase<?> parts, PartsDesign design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		// 親コンテナのHtmlIdを取得
		final String parentHtmlId = PartsUtils.getParentHtmlId(parts.htmlId);

		if (isNotEmpty(parentHtmlId)) {
			// キャッシュから親コンテナの判定結果を取得する
			// キャッシュにない場合はここで親コンテナの判定処理を行う
			if (!ecCache.containsKey(parentHtmlId)) {
				EvaluateCondition ec = new EvaluateCondition();
				PartsContainerBase<?> parent = (PartsContainerBase<?>)ctx.runtimeMap.get(parentHtmlId);
				if (parent != null) {
					// パーツに有効条件が設定されてあれば有効/無効の判定処理を行って結果を格納する
					PartsDesign parentDesign = ctx.designMap.get(parent.partsId);
					if (parentDesign.partsConds != null) {
						for (PartsCond cond : parentDesign.partsConds) {
							String formula = createFormula(cond.items);
							Map<Long, String> values = getTargetPartsValueMap(parent, cond.items, ctx);
							boolean evalulate = evaluate(formula, values);
							if (eq(PartsConditionType.ENABLED, cond.partsConditionType)) {
								ec.enabled = evalulate;
							} else if (eq(PartsConditionType.VISIBLED, cond.partsConditionType)) {
								ec.visibled = evalulate;
							} else if (eq(PartsConditionType.READONLY, cond.partsConditionType)) {
								ec.readonly = evalulate;
							}
						}
					}
					// さらに親コンテナの判定を行うため再帰呼出
					EvaluateCondition parentEc = isParentEnabled(parent, parentDesign, ctx, ecCache);

					// 有効条件と読取専用条件は自コンテナと親コンテナとの結果をマージ
					ec.enabled  = (ec.enabled && parentEc.enabled);
					ec.readonly = (ec.readonly && parentEc.readonly);
					// キャッシュに格納
					ecCache.put(parent.htmlId, ec);
				}
			}
			return ecCache.get(parentHtmlId);
		}

		return new EvaluateCondition();
	}

	/**
	 * パーツの有効条件に関連する項目をレンダリング用のオブジェクトへ変換して返す
	 * オブジェクトの中身は、
	 * ・evaluate：有効条件の評価結果
	 * ・disabled：パーツの有効・無効の判定結果
	 * ・control ：有効条件による制御対象か
	 * ・json    ：有効条件のJSON文字列
	 * @param parts
	 * @param design
	 * @param ctx
	 * @param ecCache
	 * @return
	 */
	public EvaluateCondition getEvaluateCondition(PartsBase<?> parts, PartsDesign design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		// キャッシュにあればそれを使う
		if (ecCache.containsKey(parts.htmlId)) {
			return ecCache.get(parts.htmlId);
		}

		// ない場合は条件定義から条件結果を生成
		final EvaluateCondition ec = new EvaluateCondition();
		if (ctx.renderMode != RenderMode.DESIGN) {
			if (isNotEmpty(design.partsConds)) {
				final List<EvaluateConditionItem> conditions = new ArrayList<>();
				for (PartsCond cond : design.partsConds) {
					final EvaluateConditionItem bean = new EvaluateConditionItem();
					bean.partsConditionType = cond.partsConditionType;
					bean.callbackFunction = cond.callbackFunction;
					bean.formula = createFormula(cond.items);
					bean.values  = getTargetPartsValueMap(parts, cond.items, ctx);
					conditions.add(bean);
					// 条件の評価結果を取得
					final boolean evaluate = evaluate(bean.formula, bean.values);
					if (eq(PartsConditionType.ENABLED, cond.partsConditionType))
						ec.enabled = evaluate;
					else if (eq(PartsConditionType.VISIBLED, cond.partsConditionType))
						ec.visibled = evaluate;
					else if (eq(PartsConditionType.READONLY, cond.partsConditionType))
						ec.readonly = evaluate;
				}
				ec.conditions = conditions;
			}
			// 自パーツ自身が(別パーツの)条件定義の判定元になっている場合の判定先パーツID一覧
			if (ctx.targetCondMap.containsKey(design.partsId)) {
				final Set<Long> targets = ctx.targetCondMap.get(design.partsId);
				ec.targets = targets;
			}

			// 親パーツの有効・無効の判定結果
			final EvaluateCondition parentEc = isParentEnabled(parts, design, ctx, ecCache);
			// 自パーツまたは親パーツのどちらかでも有効でなければ自パーツは無効
			ec.enabled = (ec.enabled && parentEc.enabled);
			// 自パーツまたは親パーツのどちらかでも読取専用であれば自パーツは読取専用
			ec.readonly= (ec.readonly || parentEc.readonly);

			// 自パーツが有効条件または読取専用条件による制御対象か
			// 自パーツに有効条件／読取専用条件が設定されてなくても親パーツ(またはその親)に有効条件／読取専用条件があれば
			// 自パーツも制御対象となる
			final boolean ecTarget = isTarget(parts, design, ctx);
			ec.control = ecTarget;
		}

		// 自パーツの判定結果をecCacheに格納
		// ※有効ならtrue、無効であればfalseが設定されます
		// ※判定結果は自パーツの有効条件に基づく結果ではなく、親パーツも含めた結果です
		ecCache.put(parts.htmlId, ec);

		return ec;
	}

	/**
	 * 自パーツが有効条件または読取専用条件による制御対象かを判定.
	 * @param parts
	 * @param design
	 * @param ctx
	 * @return
	 */
	private boolean isTarget(PartsBase<?> parts, PartsDesign design, DesignerContext ctx) {
		if (ctx.renderMode == RenderMode.DESIGN) return false;
		if (hasEnableOrReadOnlyCondition(design.partsConds)) return true;
		// 親またはその祖先コンテナが有効条件を持っていれば自パーツも制御対象とする
		Long parentPartsId = design.parentPartsId;
		while (parentPartsId != null && parentPartsId > 0) {
			PartsDesign parent = ctx.designMap.get(parentPartsId);
			if (parent == null) {
				return false;
			}
			if (hasEnableOrReadOnlyCondition(parent.partsConds)) {
				return true;
			}
			parentPartsId = parent.parentPartsId;
		}
		return false;
	}

	/**
	 * 条件定義内に有効条件または読取専用条件があるか
	 * @param partsConds 条件定義一覧
	 * @return trueなら有効条件または読取専用条件あり
	 */
	private boolean hasEnableOrReadOnlyCondition(List<PartsCond> partsConds) {
		if (isEmpty(partsConds))
			return false;
		else
			return partsConds.stream().anyMatch(c -> contains(c.partsConditionType, PartsConditionType.ENABLED, PartsConditionType.READONLY));
	}

	/**
	 * パーツ条件の判定結果Mapを生成
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<String, EvaluateCondition> createEcResults(DesignerContext ctx) {
		final Map<String, EvaluateCondition> ecCache = new HashMap<>(64);
		final PartsContainerBase<?> c = (PartsContainerBase<?>)ctx.runtimeMap.get(ctx.root.containerCode);
		collectEcResult(c, ctx, ecCache);
		return ecCache;
	}

	/**
	 * パーツ条件の判定結果Mapを再帰的に作成
	 * @param c パーツコンテナ
	 * @param ctx デザイナーコンテキスト
	 * @param ecCache 有効条件の判定結果Map
	 */
	private void collectEcResult(PartsContainerBase<?> c, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		for (PartsContainerRow row : c.rows) {
			for (String htmlId : row.children) {
				final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);

				// 有効条件を判定し、その結果を ecCacheへセット
				// ここで欲しいのは「結果をecCacheへセットすること」だけなので、それ以外は捨てる
				getEvaluateCondition(p, d, ctx, ecCache);

				// 子要素があれば再帰呼び出し
				if (p instanceof PartsContainerBase) {
					collectEcResult((PartsContainerBase<?>)p, ctx, ecCache);
				}
			}
		}
	}

//	/**
//	 * パーツ条件定義一覧よりパーツ条件区分に合致する条件定義を取得.
//	 * @param partsConds パーツ条件定義一覧
//	 * @param conditionType 取得対象とするパーツ条件区分
//	 * @return パーツ条件
//	 */
//	private PartsCond getPartsCond(List<PartsCond> partsConds, String conditionType) {
//		if (isEmpty(partsConds))
//			return null;
//		else
//			return partsConds.stream().filter(c -> eq(conditionType, c.partsConditionType)).findFirst().orElse(null);
//	}

	/**
	 * 条件式を使用するか
	 * @param ctx
	 * @param design
	 * @return
	 */
	public boolean isUseCond(DesignerContext ctx, PartsDesign design) {
		return ctx.renderMode != RenderMode.DESIGN
				&& (isNotEmpty(design.partsConds)
						|| ctx.targetCondMap.containsKey(design.partsId));
	}
}
