package jp.co.nci.iwf.designer.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツでAjaxを扱うときのユーティリティ
 */
public abstract class PartsAjaxUtils extends MiscUtils {
	/** パーツI/O区分でIN（絞り込み条件）とみなせるもののリスト */
	private static final Set<String> typesIN =new HashSet<>(Arrays.asList(
			PartsIoType.IN, PartsIoType.BOTH));

	/** Ajaxの対象と出来るパーツ種別 */
	private static final Set<Integer> ajaxPartsTypes = new HashSet<>(Arrays.asList(
			PartsType.TEXTBOX, PartsType.CHECKBOX, PartsType.RADIO, PartsType.DROPDOWN, PartsType.MASTER));

	/**
	 * 「Ajaxの起動元パーツ」のパーツIDをキーにMap化。
	 * （キー：トリガーパーツID、値：影響を受けるマスタ選択パーツIDのリスト）
	 * @param designMap
	 * @return
	 */
	public static Map<Long, Set<Long>> createTriggerAjaxMap(Map<Long, PartsDesign> designMap) {
		final Map<Long, Set<Long>> map = new HashMap<>();

		for (PartsDesign d : designMap.values()) {
			// マスタ選択パーツのみ
			if (d.partsType != PartsType.MASTER || d.relations == null) {
				continue;
			}
			for (PartsRelation pr : d.relations) {
				// I/O区分＝INだけが対象
				if (!typesIN.contains(pr.partsIoType)) {
					continue;
				}
				if (!map.containsKey(pr.targetPartsId)) {
					map.put(pr.targetPartsId, new HashSet<>());
				}
				map.get(pr.targetPartsId).add(d.partsId);
			}
		}
		return map;
	}

	/**
	 * 引数のpartsがトリガーとなっている汎用マスタパーツのHtmlId一覧を返す
	 * @param parts
	 * @param design
	 * @param ctx
	 * @return
	 */
	public static Set<String> getAjaxTriggerSet(PartsBase<?> parts, PartsDesign design, DesignerContext ctx) {
		final Set<Long> triggerAjaxs = ctx.triggerAjaxMap.get(parts.partsId);
		if (triggerAjaxs != null) {
			return findHtmlId(triggerAjaxs, parts, ctx);
		}
		return null;
	}

	/**
	 * パーツIDをもつランタイムパーツのHtmlIdを返す。パーツIDは複数存在することがあり得るので、基準パーツと同じまたは下位のコンテナのものとする
	 * @param partsIds パーツIDリスト
	 * @param base 基準パーツ。このパーツと同じコンテナまたは下位コンテナにあるパーツIDを探す
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	private static Set<String> findHtmlId(Set<Long> partsIds, PartsBase<?> base, DesignerContext ctx) {
		// 判定を行う対象となるパーツは自分と同じもしくは下位コンテナ内にいる
		// よってターゲットのパーツは同一のプレフィックスを持っているはず
		String prefix = null;
		if (base.htmlId.indexOf('_') > -1) {
			prefix = base.htmlId.substring(0, (base.htmlId.lastIndexOf('_') + 1));
		}
		// パーツIDが等しく、基準パーツの親パーツのHTML_IDと前方一致するものを探し出す
		final Set<String> htmlIds = new HashSet<>();
		for (PartsBase<?> p : ctx.runtimeMap.values()) {
			if (partsIds.contains(p.partsId) && (isEmpty(prefix) || p.htmlId.startsWith(prefix))) {
				htmlIds.add(p.htmlId);
			}
		}
		return htmlIds;
	}

	/**
	 * Ajaxに使用できるパーツ種別か
	 * @param partsType パーツ種別
	 * @return
	 */
	public static boolean isAjaxablePartsType(int partsType) {
		return ajaxPartsTypes.contains(partsType);
	}
}
