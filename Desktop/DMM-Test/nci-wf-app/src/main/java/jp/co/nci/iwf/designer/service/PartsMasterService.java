package jp.co.nci.iwf.designer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tableSearch.TableSearchEntity;
import jp.co.nci.iwf.component.tableSearch.TableSearchService;
import jp.co.nci.iwf.designer.DesignerCodeBook.DcType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsIoType;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesignMaster;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * マスタ選択パーツで、汎用マスタの検索結果を他パーツへばらまき処理を行うためのサービス。
 */
@BizLogic
public class PartsMasterService extends MiscUtils {
	@Inject private TableSearchService tableSearchService;
	@Inject private PartsCondUtils partsCondUtils;

	/** NULLインスタンス */
	private static final TableSearchEntity EMPTY = new TableSearchEntity();

	/**
	 * マスタ選択パーツの現在値を検索条件に汎用マスタを検索して、その検索結果行を他の実行時パーツへばらまく。
	 * ばらまき先はマスタ選択パーツのデザイン時に定義した検索結果
	 * @param parts マスタ選択パーツの実行時パーツ
	 * @param ctx デザイナーコンテキスト
	 */
	public void distributeMasterValues(PartsMaster parts, DesignerContext ctx) {
		// パーツ条件キャッシュ
		final Map<String, EvaluateCondition> ecCache = partsCondUtils.createEcResults(ctx);
		// マスタ選択パーツのデザイン定義
		final PartsDesignMaster design = (PartsDesignMaster)ctx.designMap.get(parts.partsId);
		// 実行時パーツMapから検索条件を抜き出し
		final Map<String, String> conditions = toConditions(design, parts, ctx);
		// 汎用マスタから検索結果の抽出
		final List<TableSearchEntity> results = tableSearchService.search(design.tableSearchId, conditions);
		// パーツへの検索結果を反映
		distribute(design, parts, results, ctx, ecCache);
	}

	/**
	 * パーツの定義に従って汎用マスタの検索結果をパーツへばらまく
	 * @param d ばらまき元のパーツデザイン定義
	 * @param parts ばらまき元の実行時パーツ
	 * @param ctx デザイナーコンテキスト
	 * @return カラム名をキー、検索結果を値とした「ばらまき内容Map」
	 */
	private Map<String, String> distribute(PartsDesignMaster d, PartsBase<?> p, List<TableSearchEntity> results, DesignerContext ctx, final Map<String, EvaluateCondition> ecCache) {
		final Map<String, String> values = new HashMap<>();
		// 検索結果が単一行なら用途=検索結果とされている各パーツへ検索結果をばらまく。
		// 検索結果が０行または複数件なら検索結果不定として値をクリア
		final TableSearchEntity entity = (results.size() == 1 ? results.get(0) : EMPTY);
		for (PartsRelation pr : d.relations) {
			if (in(pr.partsIoType, PartsIoType.OUT, PartsIoType.BOTH)) {
				// 表示条件＝入力可でパーツ条件が有効なら検索結果を反映してもよい
				final PartsBase<?> target = PartsUtils.findParts(pr.targetPartsId, p.htmlId, ctx);
				final EvaluateCondition ec = ecCache.get(p.htmlId);
				if (in(target.dcType, DcType.INPUTABLE, DcType.UKNOWN) && (ec == null || ec.enabled)) {
					final String value = entity.get(pr.columnName);
					target.setValue(value);
					values.put(pr.columnName, value);
				}
			}
		}
		return values;
	}

	/** パーツの定義に従って実行時パーツから検索条件Mapを作成 */
	private Map<String, String> toConditions(PartsDesignMaster d, PartsMaster p, DesignerContext ctx) {

		// 他パーツの絞り込み条件
		final Map<String, String> conditions = new HashMap<>();
		for (PartsRelation pr : d.relations) {
			if (in(pr.partsIoType, PartsIoType.IN, PartsIoType.BOTH)) {
				final Long srcPartsId = pr.targetPartsId;
				final PartsBase<?> trigger = PartsUtils.findParts(srcPartsId, p.htmlId, ctx);
				if (trigger != null) {
					conditions.put(pr.columnName, trigger.getValue());
				}
			}
		}
		// マスタ選択パーツ自身の選択値も絞り込み条件である
		conditions.put(d.columnNameValue, p.getValue());

		return conditions;
	}
}
