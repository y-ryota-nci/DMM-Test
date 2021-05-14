package jp.co.nci.iwf.designer.parts.renderer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.MiscUtils;

@ApplicationScoped
public class RendererHelper extends MiscUtils implements CodeBook {
	@Inject private I18nService i18n;

	/**
	 * ページ制御部分のHTMLをレンダリング
	 * @param html
	 * @param parts
	 * @param ctx
	 */
	public void pagerHtml(HtmlStringBuilder html, int totalCount, int pageNo, int pageCount) {
		html.append("<div class='row'>");
		// ページ番号・ページ数
		html.append("<div class='hidden-xs col-sm-12'>");
		html.appendFormat("<span class='form-control-static'>%s : %d / %d</span>&nbsp;&nbsp;", i18n.getText(MessageCd.pageCount), pageNo, pageCount);
		html.appendFormat("<span class='form-control-static hidden-xs'>%s : %d</span>", i18n.getText(MessageCd.totalCount), totalCount);
		html.append("</div>");

		// ページリンク
		final int RANGE = 3;
		final int displayStart = pageNo - RANGE;
		final int displayEnd = pageNo + RANGE;
		final String prevCss = pageNo == 1 ? "invisible" : "";
		final String nextCss = pageNo == pageCount ? "invisible" : "";
		html.append("<div class='col-xs-12 text-center'>");
		html.appendFormat("<ul class='pagination'>");
		html.appendFormat(		"<li class='%s'><a href='#' class='form-control-static' data-page='%d' draggable='false'><i class='glyphicon glyphicon-fast-backward'></i></a></li>", prevCss, 1);
		html.appendFormat(		"<li class='%s'><a href='#' class='form-control-static' data-page='%d' draggable='false'><i class='glyphicon glyphicon-chevron-left'></i>%s</a></li>", prevCss, pageNo - 1, i18n.getText(MessageCd.prevPage));
		for (int n = 1; n <= pageCount; n++) {
			final String active = (n == pageNo ? "active" : "") ;
			if (between(n, displayStart, displayEnd)) {
				html.appendFormat(	"<li class='%s'><a href='#' class='form-control-static' data-page='%d' draggable='false'>%d</a></li>", active, n, n);
			}
		}
		html.appendFormat(		"<li class='%s'><a href='#' class='form-control-static' data-page='%d' draggable='false'>%s<i class='glyphicon glyphicon-chevron-right'></i></a></li>", nextCss, pageNo + 1, i18n.getText(MessageCd.nextPage));
		html.appendFormat(		"<li class='%s'><a href='#' class='form-control-static' data-page='%d' draggable='false'><i class='glyphicon glyphicon-fast-forward'></i></a></li>", nextCss, pageCount);
		html.append(	"</ul>");
		html.append("</div>");

		html.append("</div>");
	}

	/** ページ制御するか */
	public boolean isEnablePaging(Integer pageSize) {
		return (pageSize != null && pageSize.intValue() > 0);
	}

}
