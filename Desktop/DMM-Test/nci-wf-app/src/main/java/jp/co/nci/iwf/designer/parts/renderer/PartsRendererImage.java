package jp.co.nci.iwf.designer.parts.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignImage;
import jp.co.nci.iwf.designer.parts.runtime.PartsImage;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsAttachFileService;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 画像パーツのレンダラー
 */
@ApplicationScoped
public class PartsRendererImage extends PartsRenderer<PartsImage, PartsDesignImage> {
	@Inject private PartsAttachFileService partsAttachFileService;

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
	protected String renderInput(PartsImage parts, PartsDesignImage design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		// 画像
		renderImage(html, parts, design, ctx, true, ec);

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
	protected String renderReadonly(PartsImage parts, PartsDesignImage design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		renderImage(html, parts, design, ctx, false, ec);
		return html.toString();
	}

	/** 画像部分のレンダリング */
	private void renderImage(HtmlStringBuilder html, PartsImage parts, PartsDesignImage design, DesignerContext ctx, boolean inputable, EvaluateCondition ec) {
		// 画像を囲む枠（この枠をoverflowした分はレンダリングされない）
		html.append("<div class='text-center'>");
		html.append("<div ");
		{
			final List<String> styles = new ArrayList<>();
			styles.add("overflow:hidden");
			if (design.borderHeight != null)
				// 幅
				styles.add("height:" + design.borderHeight + "px");
			if (design.borderWidth != null)
				// 高さ
				styles.add("width:" + design.borderHeight + "px");
			html.append(toCssStyleHtml(design.cssStyle, styles));
		}
		html.append(">");

		// 画像
		html.append("<img ");
		if (parts.partsAttachFileWfId != null) {
			// 実行時用の画像は、ワークフローパーツ添付ファイルとして保存されている
			html.appendProperty("src", "../../endpoint/vd0310/download/partsAttachFileWf"
					+ "?partsAttachFileWfId=" + parts.partsAttachFileWfId
					+ "&t=" + System.currentTimeMillis());	// キャッシュ抑制用
		}
		else if (design.partsAttachFileId != null) {
			// デザイン時用の画像はパーツ添付ファイルとして保存されている
			html.appendProperty("src", "../../endpoint/vd0310/download/partsAttachFile"
					+ "?partsAttachFileId=" + design.partsAttachFileId
					+ "&t=" + System.currentTimeMillis());	// キャッシュ抑制用
		}
		else if (design.useDummyIfNoImage) {
			// 画像が何も紐付いていないのは、ダミー画像を表示する
			html.appendProperty("src", "../../assets/nci/images/noImage.png");
		}
		else {
			html.appendProperty("src", "#");
		}

		// 画像の枠
		if (design.useBorderLine) {
			// 枠線のスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			styles.add("border-style:solid");
			if (design.borderLineWidth != null)
				styles.add("border-width:" + design.borderLineWidth + "px");
			if (isNotEmpty(design.borderLineColor))
				styles.add("border-color:" + design.borderLineColor);
			html.append(toCssStyleHtml("", styles));
		}
		html.append(" />");

		// 実行時にアップロードするか
		if (inputable && design.runtimeUploadFlag) {
			// アップロードボタン
			html.append("<div class='text-center'>");	// ボタンが画像と並んでしまわないようにするための枠
			html.append(getButtonHtml(design.uploadButtonLabel, "glyphicon glyphicon-cloud-upload", "btnUploadImage", ec));
			// クリアボタン
			html.append(getButtonHtml(design.clearButtonLabel, "glyphicon glyphicon-minus-sign", "btnClearImage", ec));
			html.append("</div>");
		}
		html.append("</div></div>");
	}

	/** ボタンのレンダリング */
	private CharSequence getButtonHtml(String label, String icon, String btnClass, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<button href='#' ");
		html.appendProperty("class", "btn btn-default btn-sm " + btnClass);

		// 活性・非活性の設定
		// パーツ条件によりパーツが無効ないし読取専用であればボタンは非活性
		if (ec != null && (!ec.enabled || ec.readonly))
			html.appendProperty("disabled");
		// 有効条件により活性・非活性を切り替えるための属性を付与
		// これがある要素が制御対象となる
		if (ec != null && ec.control)
			html.appendProperty( getEcTargetProperty() );
		html.append(">");

		// アイコン
		html.append("<i ");
		html.appendProperty("class", icon);
		html.append("></i>");

		// ボタンラベル
		if (isNotEmpty(label)) {
			html.append("<span>");
			html.appendEscape(label);
			html.append("</span>");
		}
		html.append("</button>");
		return html;
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	public Object getPrintValue(PartsImage p, PartsDesignImage d, DesignerContext ctx) {
		if (p.partsAttachFileWfId != null) {
			MwtPartsAttachFileWf entity = partsAttachFileService.getMwtPartsAttachFileWfByPK(p.partsAttachFileWfId);
			if (entity != null) {
				return entity.getFileData();
			}
		}
		else if (d.partsAttachFileId != null) {
			MwmPartsAttachFile entity = partsAttachFileService.getMwmPartsAttachFileByPK(d.partsAttachFileId);
			if (entity != null) {
				return entity.getFileData();
			}
		}
		return null;
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsImage p, PartsDesignImage d, DesignerContext ctx) {
		return null;
	}
}
