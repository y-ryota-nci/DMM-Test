package jp.co.nci.iwf.designer.parts.renderer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStamp;
import jp.co.nci.iwf.designer.parts.runtime.PartsStamp;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.StampInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * スタンプパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererStamp extends PartsRenderer<PartsStamp, PartsDesignStamp> {

	@Inject private I18nService i18n;
	@Inject private SessionHolder sessionHolder;

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
	protected String renderInput(PartsStamp parts, PartsDesignStamp design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		return renderPrint(parts, design, ctx);
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
	protected String renderReadonly(PartsStamp parts, PartsDesignStamp design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		return renderInput(parts, design, ctx, ecCache, ec);
	}

	/** スタンプ出力共通処理 */
	protected String renderPrint(PartsStamp parts, PartsDesignStamp design, DesignerContext ctx) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final boolean isPreviewMode = RenderMode.PREVIEW == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		if (isDesignMode || isPreviewMode || (isNotEmpty(ctx.stampMap) && ctx.stampMap.containsKey(design.stampCode))) {
			StampInfo stampHistory = isEmpty(ctx.stampMap) ? null : ctx.stampMap.get(design.stampCode);
			if (isEmpty(stampHistory)) {
				LoginInfo loginInfo = sessionHolder.getLoginInfo();
				stampHistory = new StampInfo();
				stampHistory.sealExecutionDate = timestamp();
				stampHistory.sealNameUser = isEmpty(loginInfo.getUserNameAbbr()) ? loginInfo.getUserName() : loginInfo.getUserNameAbbr();
				stampHistory.sealNameAction = i18n.getText(MessageCd.stampDefaultActionName);
			}
			html.append(renderSvg(parts, design, ctx, stampHistory));
		}
		return html.toString();
	}

	/** <SVG>タグの出力 */
	private String renderSvg(PartsStamp parts, PartsDesignStamp design, DesignerContext ctx, StampInfo stampHistory) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<svg");
		html.appendProperty("xmlns", "http://www.w3.org/2000/svg");
		html.appendProperty("width", "60px");
		html.appendProperty("height", "60px");
		html.append(toCssClassHtml(design.cssClass));

		// SVGスタイル
		final List<String> svgStyles = new ArrayList<>();
		Collections.addAll(svgStyles, "width: 60px;", "height: 60px;", "margin: 2px;", "border: 1px solid;", "padding: 1px;", "box-sizing: content-box;");

		final String borderColor = design.stampFrameDisplaty ? design.stampBorderColor : "transparent";
		svgStyles.add("border-color: " + borderColor + ";");
		BigDecimal zoom = new BigDecimal(design.stampSize).divide(new BigDecimal(60), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		svgStyles.add("zoom: " + zoom.toString() + "%;");
		// パーツスタイル
		svgStyles.addAll(toCssStyleList(parts, design, ctx));
		html.append(toCssStyleHtml(design.cssStyle, svgStyles));
		html.append(">");

		html.append("<g");
		html.append(toCssStyleHtml(null, "fill: none;", "stroke: red;", "stroke-width: 1;"));
		html.append(">");

		// 円
		html.append("<circle");
		html.appendProperty("cx", "30");
		html.appendProperty("cy", "30");
		html.appendProperty("r", "29");
		html.append(toCssStyleHtml("stroke-width: 2;"));
		html.append(" />");

		// ライン(上部)
		html.append("<line");
		html.appendProperty("x1", "4");
		html.appendProperty("y1", "17");
		html.appendProperty("x2", "57");
		html.appendProperty("y2", "17");
		html.append(" />");

		// ライン(下部)
		html.append("<line");
		html.appendProperty("x1", "0");
		html.appendProperty("y1", "29");
		html.appendProperty("x2", "60");
		html.appendProperty("y2", "29");
		html.append(" />");

		final List<String> textStyles = Arrays.asList("fill: red;", "stroke: none;", "font-weight: normal;", "text-anchor: middle;");
		// 1段目
		if (isNotEmpty(stampHistory.sealNameAction)) {
			html.append("<text");
			html.appendProperty("x", "30");
			html.appendProperty("y", "15");
			html.append(toCssStyleHtml("font-size: 11px;", textStyles));
			html.append(">");
			html.appendEscape(stampHistory.sealNameAction);
			html.append("</text>");
		}

		// 2段目
		if (isNotEmpty(stampHistory.sealExecutionDate)) {
			html.append("<text");
			html.appendProperty("x", "30");
			html.appendProperty("y", "27");
			html.append(toCssStyleHtml("font-size: 11px;", textStyles));
			html.append(">");
			html.appendEscape(toStr(stampHistory.sealExecutionDate, "yy-MM-dd"));
			html.append("</text>");
		}

		// 3段目
		if (isNotEmpty(stampHistory.sealNameUser)) {
			html.append("<text");
			html.appendProperty("x", "30");

			String name = stampHistory.sealNameUser;
			switch (name.length()) {
			case 1:
			case 2:
				html.appendProperty("y", "43");
				html.append(toCssStyleHtml("font-size: 14px;", textStyles));
				break;
			case 3:
				html.appendProperty("y", "42");
				html.append(toCssStyleHtml("font-size: 12px;", textStyles));
				break;
			case 4:
				html.appendProperty("y", "41");
				html.append(toCssStyleHtml("font-size: 11px;", textStyles));
				break;
			case 5:
				html.appendProperty("y", "40");
				html.append(toCssStyleHtml("font-size: 9px;", textStyles));
				break;
			default:
				html.appendProperty("y", "40");
				html.append(toCssStyleHtml("font-size: 8px;", textStyles));
				break;
			}
			html.append(">");
			html.appendEscape(name);
			html.append("</text>");
		}

		html.append("</g>");
		html.append("</svg>");

		return html.toString();
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	public Object getPrintValue(PartsStamp p, PartsDesignStamp d, DesignerContext ctx) {
		return renderPrint(p, d, ctx);
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsStamp p, PartsDesignStamp d, DesignerContext ctx) {
		return null;
	}
}
