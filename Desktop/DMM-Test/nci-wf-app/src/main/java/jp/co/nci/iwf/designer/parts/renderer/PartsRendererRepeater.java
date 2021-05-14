package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRepeater;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRepeater;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * Repeaterパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererRepeater
		extends PartsRendererPagingBase<PartsRepeater, PartsDesignRepeater> {

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
	protected String renderInput(PartsRepeater parts, PartsDesignRepeater design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
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
	protected String renderReadonly(PartsRepeater parts, PartsDesignRepeater design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
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
	private void tableHtml(HtmlStringBuilder html, PartsRepeater parts, PartsDesignRepeater design, DesignerContext ctx, Map<String, EvaluateCondition> ecResult) {
		final boolean isRenderAsInput = isRenderAsInput(parts.dcType, ctx);
		html.append("<table class='table table-striped table-bordered table-condensed responsive'>");

		// 選択用チェックボックスをレンダリングするか
		boolean showCheckbox = isRenderAsInput && (design.showCopyButtonFlag || design.showDeleteButtonFlag);

		html.append("<colgroup>");
		if (showCheckbox)
			html.append("<col style='width:40px' />");
		else
			html.append("<col style='width:25px' />");
		html.append("<col style='width:99%' />");
		html.append("</colgroup>");

		// ページ制御による表示範囲
		int from = Integer.MIN_VALUE, to = Integer.MAX_VALUE;
		if (isEnablePaging(design)) {
			int pageNo = defaults(parts.pageNo, 0);
			int pageSize = defaults(parts.pageSize, design.pageSize, 0);
			from = (pageNo - 1) * pageSize + 1;
			to = pageNo * pageSize;
		}

		html.append("<tbody>");
		for (PartsContainerRow row : parts.rows) {
			// ページ制御
			if (!isEnablePaging(design) || MiscUtils.between(row.sortOrder, from, to)) {
				html.append("<tr>");

				// 行番号とチェックボックス
				html.append("<th class='line-number'>");
				if (showCheckbox) {						// 読取専用ならCHECKBOXをレンダリングしない
					html.append(	"<input type='checkbox' class='selectable' ");
					html.appendProperty("value", parts.htmlId + "-" + row.rowId).append(" />");
				}
				html.appendEscape(row.sortOrder);
				html.append("</th>");

				// 配下パーツを展開したHTML
				html.append("<td>");
				if (ViewWidth.isMobile(ctx)) {
					html.append(renderWithoutBgHtml(row, design, ctx, ecResult));
				}
				else {
					html.append(renderWithBgHtml(row, design, ctx, ecResult));
				}
				html.append("</td>");

				html.append("</tr>");
			}
		}
		html.append("</tbody>");
		html.append("</table>");
	}
}
