package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignSearchButton;
import jp.co.nci.iwf.designer.parts.runtime.PartsSearchButton;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 検索ボタンのパーツレンダラー
 * V5で汎用マスタパーツが有していた「ボタン押下で汎用テーブル検索ポップアップを開く機能」は当パーツへ移管された。
 */
@ApplicationScoped
public class PartsRendererSearchButton extends PartsRendererAjax<PartsSearchButton, PartsDesignSearchButton> {
	/**
	 * 入力項目としてHTMLを生成
	 * @param parts パーツ
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderInput(PartsSearchButton parts, PartsDesignSearchButton design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {

		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		final String buttonSize = getButtonSizeCssClass(design.buttonSize);
		html.append("<button ");
		{
			// CSSクラス
			html.append(toButtonCssClassHtml(design, buttonSize));
			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			// タブ順を付与
			if (!design.grantTabIndexFlag)
				html.appendProperty("tabindex", "-1");
			// パーツ条件の評価結果に伴う活性／非活性の切替
			// パーツ条件によりパーツが無効ないし読取専用であればボタンは非活性
			if (!ec.enabled || ec.readonly)
				html.appendProperty("disabled");
			// パーツ条件により活性・非活性を切り替えるための属性を付与
			// これがある要素が制御対象となる
			if (ec.control)
				html.appendProperty( getEcTargetProperty() );

			// 検索画面ポップアップ用の属性を出力
			if (!isDesignMode && design.tableSearchId != null) {
				// 属性を付与（これを設定しておくことでクライアント側で検索ボタンと認識してスクリプトが動く）
				html.appendProperty("data-table-search-id");
				html.appendProperty("data-ajax-results");
			}
			html.append(">");

			// ボタン内のアイコン
			if (isNotEmpty(design.iconCssClass)) {
				html.append("<i ");
				html.append(toCssClassHtml(design.iconCssClass));
				html.append("></i>");
			}

			// ボタンのテキスト
			if (isNotEmpty(design.buttonLabel)) {
				html.appendEscape(design.buttonLabel);
			}
		}
		html.append("</button>");

		// クリアボタン
		if (!design.hideClearButton) {
			html.append("<button");
			html.append(toButtonCssClassHtml(design, buttonSize, "btnClear"));
			html.appendProperty("data-ajax-results", toJsonResults(design.relations, parts, ctx));
			// パーツ条件の評価結果に伴う活性／非活性の切替
			// パーツ条件によりパーツが無効ないし読取専用であればボタンは非活性
			if (!ec.enabled || ec.readonly)
				html.appendProperty("disabled");
			// パーツ条件により活性・非活性を切り替えるための属性を付与
			// これがある要素が制御対象となる
			if (ec.control)
				html.appendProperty( getEcTargetProperty() );
			html.append(">");
			html.append("<i class='glyphicon glyphicon-remove-circle'></i>");
			html.append("</button>");
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
	protected String renderReadonly(PartsSearchButton parts, PartsDesignSearchButton design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		// 読取時には非表示である
		return "";
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsSearchButton p, PartsDesignSearchButton d, DesignerContext ctx) {
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
	public String getDisplayValue(PartsSearchButton p, PartsDesignSearchButton d, DesignerContext ctx) {
		return null;
	}
}
