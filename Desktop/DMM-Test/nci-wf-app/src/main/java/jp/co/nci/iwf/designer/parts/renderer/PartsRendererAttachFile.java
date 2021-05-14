package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAttachFile;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFile;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFileRow;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 添付ファイルパーツのレンダラー
 */
@ApplicationScoped
public class PartsRendererAttachFile extends PartsRenderer<PartsAttachFile, PartsDesignAttachFile> {
	@Inject private I18nService i18n;
	@Inject private RendererHelper helper;

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
	protected String renderInput(PartsAttachFile parts, PartsDesignAttachFile design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {

		// ページNoの補正
		parts.adjustNewPageNo(parts.pageNo);

		final HtmlStringBuilder html = new HtmlStringBuilder(1024);
		html.append("<div class='line-controller'>");

		// ページ制御部分
		if (design.multiple && design.pageSize > 0)
			pagerHtml(html, parts, ctx);

		// テーブル or 単一行
		if (design.multiple)
			tableHtml(html, parts, design, ctx, ec);
		else
			singleHtml(html, parts, design, ctx, ec);

		html.append("</div>");
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
	protected String renderReadonly(PartsAttachFile parts, PartsDesignAttachFile design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(1024);
		html.append("<div class='line-controller'>");

		// ページNoの補正
		parts.adjustNewPageNo(parts.pageNo);

		// ページ制御部分
		if (isEnablePaging(design))
			pagerHtml(html, parts, ctx);

		// ページ制御されたテーブル or 単一行
		if (design.pageSize > 0)
			tableHtml(html, parts, design, ctx, ec);
		else
			singleHtml(html, parts, design, ctx, ec);

		html.append("</div>");
		return html.toString();
	}

	/** ページ制御部分のレンダリング */
	private void pagerHtml(HtmlStringBuilder html, PartsAttachFile parts, DesignerContext ctx) {
		final int pageCount = parts.calcPageCount();
		final int pageNo = parts.rows.isEmpty() ? 0 : parts.pageNo;
		final int totalCount = parts.rows.size();
		helper.pagerHtml(html, totalCount, pageNo, pageCount);
	}

	/** テーブル部分のレンダリング */
	private void tableHtml(HtmlStringBuilder html, PartsAttachFile parts, PartsDesignAttachFile design,
			DesignerContext ctx, EvaluateCondition ec) {
		final boolean isRenderAsInput = isRenderAsInput(parts.dcType, ctx);
		html.append("<table class='table table-striped table-bordered table-condensed responsive attach-file-parts'>");

		// ページ制御による表示範囲
		int from = Integer.MIN_VALUE, to = Integer.MAX_VALUE;
		if (isEnablePaging(design)) {
			int pageNo = defaults(parts.pageNo, 0);
			int pageSize = defaults(parts.pageSize, design.pageSize, 0);
			from = (pageNo - 1) * pageSize + 1;
			to = pageNo * pageSize;
		}

		// 列幅
		html.append("<colgroup>");
		if (isRenderAsInput)	// 読取専用ならCHECKBOXをレンダリングしない
			html.append("<col style='width:50px' />");
		else
			html.append("<col style='width:35px' />");
		if (design.notUseComment) {
			html.append("<col class='col-sm-12' />");
		}
		else {
			html.append("<col class='col-sm-6' />");
			html.append("<col class='col-sm-6' />");
		}
		html.append("</colgroup>");

		// ヘッダ部分
		html.append("<thead><tr>");
		html.append("<th class='text-center line-number'>No</th>");	// 行番号とチェックボックス
		html.append("<th class='text-center'>");
		html.appendEscape(i18n.getText(MessageCd.fileName));
		html.append("</th>");
		if (!design.notUseComment) {
			html.append("<th class='text-center'>");
			html.appendEscape(i18n.getText(MessageCd.comment));
			html.append("</th>");
		}
		html.append("</tr></thead>");

		// 本体部分
		html.append("<tbody>");
		for (PartsAttachFileRow row : parts.rows) {
			// ページ制御
			if (between(row.sortOrder, from, to)) {
				html.append("<tr>");

				// 行番号とチェックボックス
				html.append("<th class='text-center vertical-middle line-number'>");
				if (isRenderAsInput) {						// 読取専用ならCHECKBOXをレンダリングしない
					html.append(	"<input type='checkbox' class='selectable' ");
					html.appendProperty("value", row.partsAttachFileWfId).append("/>&nbsp;");
				}
				html.appendEscape(row.sortOrder);
				html.append("</th>");

				// ファイル名リンク
				html.append("<td>");
				linkHtml(html, row);
				html.append("</td>");

				// コメント
				if (!design.notUseComment) {
					html.append("<td>");
					html.appendEscape(row.comments);
					html.append("</td>");
				}
				html.append("</tr>");
			}
		}
		html.append("</tbody>");
		html.append("</table>");

		// 行の追加／削除ボタン
		if (isRenderAsInput(parts.dcType, ctx)) {
			html.append("<span class='pull-left'>");
			allButtonHtml(html, parts, design, ec.control, ec);
			html.append("</span>");
		}
	}

	/** ページ制御するか */
	private boolean isEnablePaging(PartsDesignAttachFile design) {
		return (design.multiple && helper.isEnablePaging(design.pageSize));
	}

	/** リンク部分のレンダリング */
	private void linkHtml(HtmlStringBuilder html, PartsAttachFileRow row) {
		// リンク
		html.append("<a draggable='false' ");
		if (row.partsAttachFileWfId != null) {
			html.appendProperty("href", "../../endpoint/vd0310/download/partsAttachFileWf?partsAttachFileWfId=" + row.partsAttachFileWfId);
		}
		html.appendProperty("target", "_blank");
		html.append(">");
		html.appendEscape(row.fileName);
		html.append("</a>");
	}

	/** 行の追加／削除ボタンのレンダリング */
	private void allButtonHtml(HtmlStringBuilder html, PartsAttachFile parts, PartsDesignAttachFile design,
			boolean ecTarget, EvaluateCondition ec) {
		// パーツが無効ないし読取専用であればボタンは非活性
		boolean disabled = !ec.enabled || ec.readonly;

		buttonHtml(html, MessageCd.btnAdd, "btnOpenAttachFilePopup", ecTarget, disabled);
		if (design.multiple)
			buttonHtml(html, MessageCd.btnDelete, "btnDeleteMultiAttachFile", ecTarget, disabled);
		else
			buttonHtml(html, MessageCd.btnDelete, "btnDeleteSingleAttachFile", ecTarget, disabled);
	}

	/**
	 * 単一ボタンのHTMLを生成
	 * @param html 追記対象の文字バッファ
	 * @param messageCd ボタン名用enum
	 * @param cssClass CSSクラス
	 * @param ecTarget 有効条件による制御対象か
	 * @param disabled trueなら非活性
	 */
	private void buttonHtml(HtmlStringBuilder html, MessageCd messageCd, String cssClass, boolean ecTarget, boolean disabled) {
		html.append("<button");
		html.append(toCssClassHtml("btn btn-default btn-sm", cssClass));
		if (ecTarget)
			html.appendProperty( getEcTargetProperty() );
		if (disabled)
			html.appendProperty("disabled");
		html.append(">");
		html.appendEscape(i18n.getText(messageCd));
		html.append("</button>");
	}

	/** 単一行のレンダリング */
	private void singleHtml(HtmlStringBuilder html, PartsAttachFile parts, PartsDesignAttachFile design, DesignerContext ctx, EvaluateCondition ec) {
		// ファイルリンク
		html.append("<span class='pull-left'>");
		if (parts.rows != null && !parts.rows.isEmpty()) {
			PartsAttachFileRow row = parts.rows.get(0);
			linkHtml(html, row);
		}
		html.append("</span>");

		// 行の追加／削除ボタン
		if (isRenderAsInput(parts.dcType, ctx)) {
			html.append("<span class='pull-right'>");
			allButtonHtml(html, parts, design, ec.control, ec);
			html.append("</span>");
		}
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsAttachFile p, PartsDesignAttachFile d, DesignerContext ctx) {
		return p.rows.stream()
				.map(row -> row.fileName)
				.collect(Collectors.joining(", "));
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsAttachFile p, PartsDesignAttachFile d, DesignerContext ctx) {
		return p.rows.stream()
				.map(row -> row.fileName)
				.collect(Collectors.joining(" "));
	}
}
