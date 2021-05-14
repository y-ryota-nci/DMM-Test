package jp.co.nci.iwf.designer.parts.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * Gridパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererGrid
		extends PartsRendererPagingBase<PartsGrid, PartsDesignGrid> {

	/**
	 * 入力項目としてHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderInput(PartsGrid parts, PartsDesignGrid design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(1024);

		// ページNoの補正
		parts.adjustNewPageNo(parts.pageNo);

		// ページ制御
		if (isEnablePaging(design))
			pagerHtml(html, parts, ctx);

		// パーツ条件によりパーツが無効ないし読取専用であればボタンは非活性
		boolean disabled = !ec.enabled || ec.readonly;

		// ボタン(上部に表示するケース)
		if (design.showButtonTopFlag)
			allButtonHtml(html, parts, design, ec.control, disabled);

		// 子パーツをレンダリング
		if (!parts.rows.isEmpty())
			tableHtml(html, parts, design, ctx, ecCache);

		// ボタン(下部に表示するケース)
		if (!design.showButtonTopFlag)
			allButtonHtml(html, parts, design, ec.control, disabled);

		return html.toString();
	}

	/**
	 * 読取専用項目としてHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderReadonly(PartsGrid parts, PartsDesignGrid design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(1024);

		// ページNoの補正
		parts.adjustNewPageNo(parts.pageNo);

		// ページ制御
		if (design.pageSize != null && design.pageSize > 0)
			pagerHtml(html, parts, ctx);

		// 子パーツをレンダリング
		if (!parts.rows.isEmpty())
			tableHtml(html, parts, design, ctx, ecCache);

		return html.toString();
	}


	/**
	 * テーブル部分のHTMLを生成
	 * @param html 追記対象の文字バッファ
	 * @param parts パーツ
	 * @param ctx デザイナーコンテキスト
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void tableHtml(HtmlStringBuilder html, PartsGrid parts, PartsDesignGrid design, DesignerContext ctx, Map<String, EvaluateCondition> ecResult) {
		final boolean isRenderAsInput = isRenderAsInput(parts.dcType, ctx);

		html.append("<div class='table-responsive'>");
		html.append("<table class='table table-striped table-bordered table-condensed responsive'");
		html.appendProperty("style", "min-width:" + design.minWidth + "px;");
		html.append(">");

		// ページ制御による表示範囲
		int from = Integer.MIN_VALUE, to = Integer.MAX_VALUE;
		if (isEnablePaging(design)) {
			int pageNo = defaults(parts.pageNo, 0);
			int pageSize = defaults(parts.pageSize, design.pageSize, 0);
			from = (pageNo - 1) * pageSize + 1;
			to = pageNo * pageSize;
		}

		// 選択用チェックボックスをレンダリングするか
		boolean showCheckbox = isRenderAsInput && (design.showCopyButtonFlag || design.showDeleteButtonFlag);

		// 列幅
		final int lineNumberWidth = showCheckbox ? 50 : 35;	// 「行番号」列の幅
		html.append("<colgroup>");
		html.append("<col style='width:").append(lineNumberWidth).append("px' />");
		final int DEFINE_COL = 12;
		// 固定で12個のcolを定義
		for (int i = 0; i < DEFINE_COL; i++) {
			html.append("<col class='col-xs-1' />");
		}
		html.append("</colgroup>");

		// 行と列を定義
		final List<List<PartsRelation>> DEFINE_ROWS = new ArrayList<>();
		{
			int row = 0;
			int col = 0;
			for (PartsRelation pr : design.relations) {
				if (DEFINE_ROWS.size() - 1 < row) {
					DEFINE_ROWS.add(new ArrayList<>());
				}
				col += pr.width;
				if (col <= DEFINE_COL) {
					DEFINE_ROWS.get(row).add(pr);
				}
				// col数が12になったらcolを初期化し行を追加
				if (col == DEFINE_COL) {
					col = 0;
					row++;
				}
			}
		}

		// ヘッダ部分
		final String theadClass = design.hideTableHeader ? " class=\"hide\"" : "";
		html.append("<thead").append(theadClass).append("><tr>");
		html.append("<th class='line-number'");	// 行番号とチェックボックス
		if (DEFINE_ROWS.size() > 1) {
			html.append(" rowspan='").append(DEFINE_ROWS.size()).append("'");
		}
		html.append(">No");
		html.append("</th>");

		boolean firstRow = true;
		for (List<PartsRelation> cols : DEFINE_ROWS) {
			if (firstRow) {
				firstRow = false;
			} else {
				html.append("<tr>");
			}
			for (int i = 0; i < cols.size(); i++) {
				PartsRelation pr = cols.get(i);
				PartsDesign d = ctx.designMap.get(pr.targetPartsId);
				boolean required = (d != null && d.requiredFlag);

				// 前列がないか列名が異なれば、新しい列とする
				// （前列と列名が同じなら列が連結されているはずなのでスキップ）
				PartsRelation prev = (i > 0) ? cols.get(i - 1) : null;
				if (prev != null && eq(pr.columnName, prev.columnName))
					continue;

				// いくつ列が連続して連結されるか分からないので先読みしておく
				int colspan = pr.width;
				for (int j = i + 1; j < cols.size(); j++) {
					PartsRelation next = cols.get(j);
					if (eq(pr.columnName, next.columnName))
						colspan += next.width;
					else
						break;
				}

				html.append("<th");
				if (pr.width == 0)	// 非表示列
					html.appendProperty("class", "hide");
				else {
					html.appendProperty("colspan", colspan);
				}
				html.append(">");
				html.append("<span");
				html.appendProperty("class", required ? "required" : "");
				html.append(">");
				html.appendEscape(pr.columnName);
				html.append("</span>");
				html.append("</th>");
			}
			html.append("</tr>");
		}
		html.append("</thead>");

		// 本体部分
		Map<Long, PartsBase<?>> rowRuntimeMap = new HashMap<>();
		html.append("<tbody>");
		for (PartsContainerRow row : parts.rows) {
			// ページ制御
			if (!isEnablePaging(design) || between(row.sortOrder, from, to)) {
				html.append("<tr>");

				// 行番号とチェックボックス
				html.append("<th class='line-number'");
				if (DEFINE_ROWS.size() > 1) {
					html.append(" rowspan='").append(DEFINE_ROWS.size()).append("'");
				}
				html.append(">");
				if (showCheckbox) {						// 読取専用ならCHECKBOX列をレンダリングしない
					html.append(	"<input type='checkbox' class='selectable' ");
					html.appendProperty("value", parts.htmlId + "-" + row.rowId).append(" />&nbsp;");
				}
				html.appendEscape(row.sortOrder);
				html.append("</th>");

				// この行のランタイムパーツをパーツIDで逆引きできるようMapを作成
				rowRuntimeMap.clear();
				for (String htmlId : row.children) {
					final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
					rowRuntimeMap.put(p.partsId, p);
				}

				// グリッドの列に定義されたパーツIDをもとにランタイムパーツを求めて、HTML化
				firstRow = true;
				for (List<PartsRelation> cols : DEFINE_ROWS) {
					if (firstRow) {
						firstRow = false;
					} else {
						html.append("<tr>");
					}
					for (int i = 0; i < cols.size(); i++) {
						PartsRelation pr = cols.get(i);

						// 前列が同じ列名なら連結して、その中に連結列の子パーツをレンダリングする
						PartsRelation prev = (i > 0) ? cols.get(i - 1) : null;
						if (prev == null || !eq(pr.columnName, prev.columnName)) {
							// いくつ列が連続して連結されるか分からないので先読みしておく
							int colspan = pr.width;
							for (int j = i + 1; j < cols.size(); j++) {
								PartsRelation next = cols.get(j);
								if (eq(pr.columnName, next.columnName))
									colspan += next.width;
								else
									break;
							}

							html.append("<td");
							if (pr.width == 0)	// 非表示列
								html.appendProperty("class", "hide");
							else {
								html.appendProperty("colspan", colspan);
							}
							html.append(">");
						}

						// 子パーツのレンダリング
						final PartsBase<?> p = rowRuntimeMap.get(pr.targetPartsId);
						final PartsDesign d = ctx.designMap.get(pr.targetPartsId);
						if (d != null && p != null) {
							final IPartsRenderer r = factory.get(d.partsType);
							html.append(r.render(p, d, ctx, ecResult));
						}

						// 次列がないか列名が異なるなら、この列(の連結)は終わり
						PartsRelation next = (i < cols.size() - 1) ? cols.get(i + 1) : null;
						if (next == null || !eq(pr.columnName, next.columnName)) {
							html.append("</td>");
						}
					}
				}
				html.append("</tr>");
			}
		}
		html.append("</tbody>");
		html.append("</table>");
		html.append("</div>");
	}
}
