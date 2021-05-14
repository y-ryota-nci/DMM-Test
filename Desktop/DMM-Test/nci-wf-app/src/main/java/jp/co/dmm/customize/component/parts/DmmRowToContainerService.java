package jp.co.dmm.customize.component.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.tableSearch.TableSearchEntity;
import jp.co.nci.iwf.component.tableSearch.TableSearchService;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignMaster;
import jp.co.nci.iwf.designer.parts.renderer.MasterPartsColumn;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererAjax;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002PartsResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002Service;

/**
 * DMM用、行データをコンテナパーツへ転写するサービス
 */
@BizLogic
@Typed(DmmRowToContainerService.class)
public class DmmRowToContainerService extends Bl0002Service {
	@Inject private TableSearchService tsService;
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 行データをコンテナへ反映
	 * @param req
	 * @return
	 */
	public Bl0002PartsResponse rowsToContainer(DmmRowToContainerRequest req) {
		final DesignerContext ctx = getDesignContext(req);
		final PartsContainerBase<?> c = (PartsContainerBase<?>)ctx.runtimeMap.get(req.htmlId);
		final PartsDesignContainer d = (PartsDesignContainer)ctx.designMap.get(c.partsId);

		// 新レコードのプライマリキー一覧
		final Set<String> primaryPartsCodes = new HashSet<>(req.primaryPartsCodes);
		final Set<String> newPkValues = req.newRows.stream()
				.map(newRow -> toPkValue(newRow, primaryPartsCodes))
				.collect(Collectors.toSet());

		// 既存データの扱い
		if (req.clearOldRows) {
			// 旧データは不要なので、子要素を削除
			c.rows.stream().flatMap(r -> r.children.stream()).forEach(htmlId -> {
				ctx.runtimeMap.remove(htmlId);
			});
			c.rows.clear();
		}
		else {
			// 既存行に新レコードと同一プライマリーキーがあるか？　あれば重複しないようコンテナから削除する
			// （既存が複数行あって新しいのも複数あると正しく上書きするのが困難なので、DELETE&INSERTする）
			for (Iterator<PartsContainerRow> it = c.rows.iterator(); it.hasNext(); ) {
				final PartsContainerRow row = it.next();
				final String pkValue = toPkValue(row, primaryPartsCodes, ctx);
				if (isEmpty(pkValue) || (isNotEmpty(pkValue) && newPkValues.contains(pkValue))) {
					row.children.stream().forEach(htmlId -> {
						ctx.runtimeMap.remove(htmlId);
					});
					it.remove();
				}
			}
		}

		// 既存行から空き行を見つけて新しいレコードを反映
		for (DmmEntityRow newRow : req.newRows) {
			PartsContainerRow row = findEmptyRow(c, primaryPartsCodes, ctx);
			if (row == null) {
				row = d.addRows(c, ctx);
			}
			fillRow(row, newRow, ctx);
		}

		// 並び順をつけ直し
		for (int i = 0; i < c.rows.size(); i++) {
			c.rows.get(i).sortOrder = (i + 1);
		}

		// HTMLを再描画
		final Bl0002PartsResponse res = createResponse(Bl0002PartsResponse.class, req);
		final Map<String, EvaluateCondition> ecResult = partsCondUtils.createEcResults(ctx);
		res.html = factory.renderDiff(c, d, ctx, ecResult);
		res.runtimeMap = ctx.runtimeMap;
		res.success = true;
		return res;
	}

	/** 新データ行のプライマリーキー値を返す */
	private String toPkValue(DmmEntityRow newRow, Set<String> primaryPartsCodes) {
		final StringJoiner sj = new StringJoiner("/");
		for (DmmEntityField f : newRow.fields)
			if (primaryPartsCodes.contains(f.partsCode))
				sj.add(f.value);
		return sj.toString();
	}

	/** 既存コンテナの行データから未入力行を探す */
	private PartsContainerRow findEmptyRow(PartsContainerBase<?> c, Set<String> primaryKeys, DesignerContext ctx) {
		// プライマリキーが未設定なら未入力とみなす
		if (primaryKeys != null && !primaryKeys.isEmpty()) {
			for (PartsContainerRow row : c.rows) {
				final String key = toPkValue(row, primaryKeys, ctx);
				if (isEmpty(key))
					return row;
			}
		}
		return null;
	}

	/** コンテナ行に新しい行データを反映 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fillRow(PartsContainerRow row, DmmEntityRow newRow, DesignerContext ctx) {
		// 新しい行データを反映
		for (DmmEntityField field : newRow.fields) {
			for (String htmlId : row.children) {
				if (htmlId.endsWith(field.partsCode)) {
					final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
					final PartsDesign d = ctx.designMap.get(p.partsId);
					if (eq(field.partsCode, d.partsCode)) {
						p.values.put(p.defaultRoleCode, field.value);
						break;
					}
				}
			}
		}
		// 行内にマスタ選択パーツがあれば選択内容をキーに汎用テーブルを検索し、その結果を他パーツへ配布
		for (String htmlId : row.children) {
			final PartsBase<?> parts = ctx.runtimeMap.get(htmlId);
			final PartsDesign design = ctx.designMap.get(parts.partsId);
			if (design.partsType == PartsType.MASTER) {
				final PartsMaster pm = (PartsMaster)parts;
				final PartsDesignMaster pdm = (PartsDesignMaster)design;
				final PartsRendererAjax renderer = (PartsRendererAjax)factory.get(design.partsType);
				final List<MasterPartsColumn> resultDefs = renderer.getResults(pdm.relations, pm, ctx);
				TableSearchEntity result = null;

				// 配布先パーツがなければ検索するだけ無駄、マスタ選択パーツ自身で値を未選択ならやっぱり検索が無駄
				if (!resultDefs.isEmpty() && !isEmpty(pm.getValue())) {
					// 検索結果を他パーツへ配布
					Map<String, String> conditions = createSeachCondition(ctx, pm, pdm, renderer);
					List<TableSearchEntity> results = tsService.search(pdm.tableSearchId, conditions);

					if (results.isEmpty())
						result = null;
					else if (results.size() == 1)
						result = results.get(0);
					else if (results.size() > 1)
						// 抽出結果が複数件であれば、設計バグである。
						// もし複数抽出を許可してしまえば、同じ選択肢を選んでいても抽出結果の配布内容が保障されなくなってしまう。
						// 「汎用テーブル検索条件」の現行仕様では抽出結果がユニークソートであることを担保出来ない仕様であるため、
						// 複数抽出されると結果が不定である。
						throw new InternalServerErrorException(i18n.getText(MessageCd.MSG0141, results.size()));
				}
				for (MasterPartsColumn resultDef : resultDefs) {
					String val = result == null ? "" : result.get(resultDef.columnName);
					PartsBase<?> p = ctx.runtimeMap.get(resultDef.targetHtmlId);
					p.values.put(p.defaultRoleCode, val);
				}
			}
		}
	}

	/** マスタ検索パーツの検索条件Mapを生成 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, String> createSeachCondition(DesignerContext ctx, final PartsMaster pm,
			final PartsDesignMaster pdm, final PartsRendererAjax renderer) {
		final Map<String, String> conditions = new HashMap<>();
		final List<MasterPartsColumn> conditionDefs = renderer.getConditions(pdm.relations, pm, ctx);
		for (MasterPartsColumn conditionDef : conditionDefs) {
			final PartsBase p = ctx.runtimeMap.get(conditionDef.targetHtmlId);
			conditions.put(conditionDef.columnName, p.getValue());
		}
		conditions.put(pdm.columnNameValue, pm.getValue());
		return conditions;
	}

	/** コンテナ行からプライマリキーに相当するパーツの値を抜き出す（プライマリキーが複数パーツであれば、値を "/"で連結） */
	private String toPkValue(PartsContainerRow row, Set<String> primaryPartsCodes, DesignerContext ctx) {
		final List<String> values = new ArrayList<>();
		for (String htmlId : row.children) {
			final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			if (primaryPartsCodes.contains(d.partsCode)) {
				values.add(p.getValue());
			}
		}
		return String.join("/", values);
	}
}
