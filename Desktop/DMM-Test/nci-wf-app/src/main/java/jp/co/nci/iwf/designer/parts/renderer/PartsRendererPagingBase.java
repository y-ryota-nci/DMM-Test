package jp.co.nci.iwf.designer.parts.renderer;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * ページ制御ありのコンテナのレンダラー基底クラス
 *
 * @param <P> パーツ
 * @param <D> パーツデザイン定義
 */
public abstract class PartsRendererPagingBase<P extends PartsContainerBase<D>, D  extends PartsDesignChildHolder>
		extends PartsRendererContainerBase<P, D> {
	@Inject private I18nService i18n;
	@Inject private RendererHelper helper;

	/**
	 * ページ制御部分のHTMLをレンダリング
	 * @param html
	 * @param parts
	 * @param ctx
	 */
	protected void pagerHtml(HtmlStringBuilder html, P parts, DesignerContext ctx) {
		final int totalCount = parts.rows.size();
		final int pageNo = parts.rows.isEmpty() ? 0 : parts.pageNo;
		final int pageCount = parts.calcPageCount();
		helper.pagerHtml(html, totalCount, pageNo, pageCount);
	}


	/**
	 * 全ボタン部分のHTMLを生成
	 * @param html 追記対象の文字バッファ
	 * @param design パーツ定義
	 * @param ecTarget 有効条件による制御対象か
	 * @param disabled trueなら非活性
	 */
	protected void allButtonHtml(HtmlStringBuilder html, P parts, D design, boolean ecTarget, boolean disabled) {
		html.append("<div class='line-controller form-inline'>");
		if (design.showAddEmptyButtonFlag)
			buttonHtml(html, MessageCd.btnAdd, "btnAddEmpty", ecTarget, disabled);
		if (design.showDeleteButtonFlag)
			buttonHtml(html, MessageCd.btnDelete, "btnDelete", ecTarget, disabled);
		if (design.showCopyButtonFlag)
			buttonHtml(html, MessageCd.btnCopy, "btnCopy", ecTarget, disabled);
		if (design.showLineCountButtonFlag) {
			final Map<String, Object> v = new HashMap<>();
			v.put("pattern", "integer");
			v.put("min", design.minRowCount);
			v.put("max", 99);
			html.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			html.append("<input type='text' class='lineCount input-sm required' maxlength='3' ");
			html.appendProperty("data-validate", toJson(v));
			html.appendProperty("value", parts.rows.size());
			if (disabled)
				html.appendProperty("disabled");
			if (ecTarget)
				html.appendProperty( getEcTargetProperty() );
			html.append(" />");
			buttonHtml(html, MessageCd.btnChangeLineCount, "btnChangeLineCount", ecTarget, disabled);
		}
		html.append("</div>");
	}

	/**
	 * 単一ボタンのHTMLを生成
	 * @param html 追記対象の文字バッファ
	 * @param messageCd ボタン名用enum
	 * @param cssClass CSSクラス
	 * @param ecTarget 有効条件による制御対象か
	 * @param disabled trueなら非活性
	 */
	protected void buttonHtml(HtmlStringBuilder html, MessageCd messageCd, String cssClass, boolean ecTarget, boolean disabled) {
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


	/** ページ制御するか */
	protected boolean isEnablePaging(PartsDesignChildHolder design) {
		return isEnablePaging(design.pageSize);
	}

	/** ページ制御するか */
	protected boolean isEnablePaging(Integer pageSize) {
		return pageSize != null && pageSize.intValue() > 0;
	}
}
