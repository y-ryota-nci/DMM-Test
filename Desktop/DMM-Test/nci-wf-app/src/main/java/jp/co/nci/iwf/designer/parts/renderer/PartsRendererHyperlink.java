package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignHyperlink;
import jp.co.nci.iwf.designer.parts.runtime.PartsHyperlink;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * ハイパーリンクのレンダラー
 */
@ApplicationScoped
public class PartsRendererHyperlink extends PartsRenderer<PartsHyperlink, PartsDesignHyperlink> {

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
	protected String renderInput(PartsHyperlink parts, PartsDesignHyperlink design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		if (DcType.HIDDEN != parts.dcType && design != null && isNotEmpty(design.labelText)) {
			// URLを決定：アップロードされた添付ファイルから求めるか、指定URLを使うか
			String url = null;
			if (design.requiredUpload) {
				if (!design.attachFiles.isEmpty()) {
					// サーブレット
//					url = "../../partsAttachFile?partsAttachFileId=" + design.attachFiles.get(0).partsAttachFileId;
					url = "../../endpoint/vd0310/download/partsAttachFile?partsAttachFileId=" + design.attachFiles.get(0).partsAttachFileId;
				}
			} else {
				url = design.url;
			}

			html.append("<a draggable='false' ");
			// CSSクラス
			html.append(toCssClassHtml(design.cssClass, "form-control-static"));
			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			// target
			if (isNotEmpty(design.target)) {
				html.appendProperty("target", design.target);
			}
			// URL
			if (isNotEmpty(url)) {
				html.appendProperty("href", url);
			}
			html.append(">");

			// テキスト文言
			html.appendEscape(defaults(design.labelText, url));

			html.append("</a>");
		}
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
	protected String renderReadonly(PartsHyperlink parts, PartsDesignHyperlink design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		return renderInput(parts, design, ctx, ecCache, ec);
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsHyperlink p, PartsDesignHyperlink d, DesignerContext ctx) {
		return d.attachFiles.stream()
				.map(af -> af.fileName)
				.collect(Collectors.joining(", "));
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsHyperlink p, PartsDesignHyperlink d, DesignerContext ctx) {
		return d.attachFiles.stream()
				.map(af -> af.fileName)
				.collect(Collectors.joining(", "));
	}
}
