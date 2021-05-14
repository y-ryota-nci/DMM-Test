package jp.co.nci.iwf.designer.parts.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.designer.DesignerCodeBook;
import jp.co.nci.iwf.designer.PartsCalcUtils;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.CalculateCondition;
import jp.co.nci.iwf.designer.service.CalculateConditionItem;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.jersey.provider.JacksonConfig;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツのレンダラーの基底クラス
 */
public abstract class PartsRenderer<P extends PartsBase<D>, D  extends PartsDesign>
		extends MiscUtils
		implements IPartsRenderer<P, D>, DesignerCodeBook {

	@Inject protected PartsCalcUtils partsCalcUtils;
	@Inject protected PartsCondUtils partsCondUtils;

	/** JSON化ユーティリティ（Jackson） */
	public static final ObjectMapper om = JacksonConfig.getObjectMapper();

	protected Logger log = LoggerFactory.getLogger(getClass());

	public PartsRenderer() {
	}

	/**
	 * パーツ本体のHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @return
	 */
	@Override
	public String render(P parts, D design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {

		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		if (isRenderAsHidden(parts.dcType, ctx)) {
			;	// HIDDEN項目はレンダリングしない
		}
		else {
			// 条件評価結果
			final EvaluateCondition ec = getEvaluateCondition(parts, design, ctx, ecCache);
			// 計算式
			final CalculateCondition cc = getCalculate(parts, design, ctx);

			// article.partsを生成
			html.append("<article");
			html.appendProperty("id", parts.htmlId);
			html.appendProperty("data-parts-id", design.partsId);
			html.appendProperty("data-parts-type", design.partsType);

			// パーツイベント
			String partsEventHtml = toPartsEventsHtml(design);
			if (isNotEmpty(partsEventHtml)) {
				html.appendProperty("data-parts-event", partsEventHtml);
			}

			if (ctx.renderMode == RenderMode.DESIGN && isNotEmpty(design.designCode)) {
				html.appendProperty("title", "[" + design.designCode + "] " + defaults(design.labelText, design.partsCode, ""));
			}
			// CSSクラス
			final String fontSize = getFontSizeCssClass(design);
			final String partsEditable = isEditableInDesignMode(design, ctx) ? "parts-editable" : "";
			final String partsLabel = (design.partsType == PartsType.LABEL ? "parts-label" : "");
			final String inputable = (isRenderAsInput(parts.dcType, ctx) && ec.enabled) ? "inputable" : "";	// トレイタイプ＝ワークリスト/新規/強制変更 and 表示条件＝入力可 and 有効条件＝有効
			final String hide = isTextboxHidden(design, ctx) || !ec.visibled ? "hide" : "";
			final String hasFeedback = hasFeedbackCssClass(design) ? "has-feedback" : "";
			final List<String> cssClasses = toWidthCssClassList(parts, design, ctx);	// 幅の指定はここでやってる
			cssClasses.addAll(asList(partsEditable, inputable, partsLabel, hide, fontSize, hasFeedback));
			html.append(toCssClassHtml("parts", cssClasses));
			// 有効条件のDATA属性を設定
			html.append(toEnabledConditionHtml(ec));
			// 計算式のDATA属性を設定
			html.append(toCalculateConditionHtml(cc));
			html.append(">");

			// 実際のレンダリング
			if (isRenderAsInput(parts.dcType, ctx)) {
				// 入力項目としてレンダリング
				html.append(renderInput(parts, design, ctx, ecCache, ec));
			}
			else {
				// 読取専用としてレンダリング
				html.append(renderReadonly(parts, design, ctx, ecCache, ec));
			}
			html.append("</article>");
		}
		return html.toString();
	}

	/** HIDDENとしてレンダリングする設定のテキストボックスか */
	private boolean isTextboxHidden(D design, DesignerContext ctx) {
		if (design instanceof PartsDesignTextbox) {
			// たとえHIDDENとしてレンダリングされるようになっていても、デザイン時は表示しておかないとパーツ属性を変更できなくなっちゃう
			return ((PartsDesignTextbox)design).renderAsHidden && ctx.renderMode != RenderMode.DESIGN;
		}
		return false;
	}

	/** パーツは 'input-group-feedback'の要素を内包するか（内包するなら位置解決のために親要素が CSSクラス'has-feedback'を持つ必要がある） */
	protected boolean hasFeedbackCssClass(D design) {
		return false;
	}

	/** 隠し項目としてレンダリングするか */
	protected boolean isRenderAsHidden(int dcType, DesignerContext ctx) {
		return (DcType.HIDDEN == dcType);
	}

	/** 入力項目としてレンダリングするか */
	protected boolean isRenderAsInput(int dcType, DesignerContext ctx) {
		final TrayType t = ctx.trayType;
		final int dc = (DcType.UKNOWN == dcType ? DcType.INPUTABLE : dcType);	// 表示区分が未定義なら入力扱いとする

		// HIDDENも入力可とみなす
//		if (isRenderAsHidden(dcType, ctx))
//			return false;

		switch (ctx.renderMode) {
		case RUNTIME:
			return (TrayType.FORCE == t)
					|| (isWorklist(t) && isDcInputable(dc));
		case PREVIEW:
			return (TrayType.FORCE == t)
					|| (isWorklist(t) && isDcInputable(dc));
		case DESIGN:
			return true;
		default:
			return true;
		}
	}

	/** 表示条件＝入力可か */
	private boolean isDcInputable(int dc) {
		return DcType.INPUTABLE == dc;
	}

	/** トレイタイプ＝ワークリストか */
	private boolean isWorklist(TrayType t) {
		return (TrayType.WORKLIST == t || TrayType.NEW == t || TrayType.BATCH == t);
	}

	/** 読取専用でレンダリングするか */
	protected boolean isRenderAsReadonly(int dcType, DesignerContext ctx) {
		return !isRenderAsHidden(dcType, ctx) && !isRenderAsInput(dcType, ctx);
	}

	/**
	 * 入力項目としてHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	protected abstract String renderInput(P parts, D design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec);

	/**
	 * 読取専用項目としてHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	protected abstract String renderReadonly(P parts, D design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec);

	/**
	 * CSSクラス値をHTML化
	 * @param cssClass CSSクラス値
	 * @param extCssClass パーツに定義されていないが、追加したいCSSクラス値
	 * @return
	 */
	protected HtmlStringBuilder toCssClassHtml(String cssClass, Collection<String> extCssClasses) {
		return toCssClassHtml(cssClass, extCssClasses.toArray(new String[extCssClasses.size()]));
	}

	/**
	 * CSSクラス値をHTML化
	 * @param cssClass CSSクラス値
	 * @param extCssClass パーツに定義されていないが、追加したいCSSクラス値
	 * @return
	 */
	protected HtmlStringBuilder toCssClassHtml(String cssClass, String...extCssClasses) {
		// 不正な文字が含まれている可能性があるので、サニタイズが必要だ。
		// そのためにはエントリを分割して、個別にエスケープする
		final Collection<String> list = new ArrayList<>();
		if (isNotEmpty(cssClass)) {
			for (String css : cssClass.split("\\s+")) {
				if (css != null && css.trim().length() > 0)
					list.add(css.trim());
			}
		}
		if (isNotEmpty(extCssClasses)) {
			for (String extCssClass : extCssClasses) {
				if (isNotEmpty(extCssClass)) {
					for (String css : extCssClass.split("\\s+")) {
						if (css != null && css.trim().length() > 0)
							list.add(css.trim());
					}
				}
			}
		}
		final HtmlStringBuilder html = new HtmlStringBuilder(32 * list.size());
		if (!list.isEmpty()) {
			html.appendProperty("class", ' ', list);
		}
		return html;
	}

	/**
	 * ボタン用:CSSクラス値をHTML化
	 * @param design
	 * @return
	 */
	protected CharSequence toButtonCssClassHtml(PartsDesign design, String...extCssClasses) {
		// Bootstrapのボタン関連のCSSが重複していないかチェック
		final Set<String> buttons = new LinkedHashSet<>();
		boolean hasDetail = false;
		if (isNotEmpty(design.cssClass)) {
			for (String css : design.cssClass.split("\\s+")) {
				if (css != null && css.trim().length() > 0) {
					buttons.add(css.trim());

					if (css.startsWith("btn-")) {
						hasDetail = true;
					}
				}
			}
		}
		// Bootstrapのボタン関連のCSSがなければ追記
		final Set<String> list = new LinkedHashSet<>();
		if (!buttons.contains("btn"))
			list.add("btn");
		if (!hasDetail)
			list.add("btn-default");

		// その他
		if (extCssClasses != null)
			for (String extCssClass : extCssClasses)
				list.add(extCssClass);

		return toCssClassHtml(design.cssClass, list.toArray(new String[list.size()]));
	}

	/** ボタンサイズに対するCSSクラスを返す */
	protected String getButtonSizeCssClass(int buttonSize) {
		if (buttonSize == PartsButtonSize.NORMAL)
			return "";
		if (buttonSize == PartsButtonSize.SMALL)
			return "btn-sm";
		if (buttonSize == PartsButtonSize.LARGE)
			return "btn-lg";
		return "";
	}

	/**
	 * パーツ定義プロパティからCSSスタイルを抜き出してリスト化
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	protected List<String> toCssStyleList(P parts, D design, DesignerContext ctx) {
		final List<String> styles = new ArrayList<>();

		// 文字サイズ
		if (design.fontSize != null && FontSize.Inherit != design.fontSize)
			styles.add("font-size:" + FontSize.toStyle(design.fontSize));
		// 文字色
		if (isNotEmpty(design.fontColor))
			styles.add("color:" + design.fontColor);
		// 太字
		if (design.fontBold)
			styles.add("font-weight:bold");

		// 背景色
		final int dcType = parts.dcType;
		if (design.bgTransparentFlag)
			styles.add("background-color:transparent");
		else if (isNotEmpty(design.bgColorInput) && isRenderAsInput(dcType, ctx))
			styles.add("background-color:" + design.bgColorInput);
		else if (isNotEmpty(design.bgColorRefer) && isRenderAsReadonly(dcType, ctx))
			styles.add("background-color:" + design.bgColorRefer);

		return styles;
	}

	/**
	 * CSSスタイルをHTML化
	 * @param style CSSスタイル
	 * @param extStyle パーツに定義されていないが、追加したいCSSスタイル値
	 * @return
	 */
	protected HtmlStringBuilder toCssStyleHtml(String styles, Collection<String> extStyleses) {
		return toCssStyleHtml(styles, extStyleses.toArray(new String[extStyleses.size()]));
	}

	/**
	 * CSSスタイルをHTML化
	 * @param style CSSスタイル
	 * @param extStyle パーツに定義されていないが、追加したいCSSスタイル値
	 * @return
	 */
	protected HtmlStringBuilder toCssStyleHtml(String styles, String...extStyleses) {
		// 不正な文字が含まれている可能性があるので、サニタイズが必要だ。
		// そのためにはエントリを分割して、個別にエスケープする
		final Collection<String> list = new ArrayList<>();
		if (isNotEmpty(styles)) {
			for (String style : styles.split(";\\s*")) {
				if (style != null && style.trim().length() > 0)
					list.add(style.trim());
			}
		}
		if (isNotEmpty(extStyleses)) {
			for (String extStyles : extStyleses) {
				for (String style : extStyles.split(";\\s*")) {
					if (style != null && style.trim().length() > 0)
						list.add(style.trim());
				}
			}
		}
		final HtmlStringBuilder html = new HtmlStringBuilder(32 * list.size());
		if (!list.isEmpty()) {
			html.appendProperty("style", ';', list);
		}
		return html;
	}

	/** MapをJSON文字列化 */
	protected String toJson(final Object obj) {
		if (obj == null)
			return "";

		try {
			final String json = om.writeValueAsString(obj);
			return json;
		}
		catch (JsonProcessingException e) {
			throw new BadRequestException(e);
		}
	}

	/**
	 * プロパティから幅の定義を抜き出して BootstrapのCSSクラス 'col-*-n'形式で返す
	 * @param prefix CSSプレフィックス（「xs|sm|md|lg」のいずれか）
	 * @param dcType 表示条件区分
	 * @param design パーツ定義
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	protected String getColSizeCss(String prefix, int dcType, D design, DesignerContext ctx) {
		// モバイル端末なら非表示となっていても、表示条件区分=入力可なら強制的にレンダリングする。
		// これは入力可な項目が画面解像度によって必須チェックをぬけてしまったり、
		// 端末の縦置きと横置きを変えただけで型チェックをぬけてしまったりするケースをなくすための処置である。
		// もし画面解像度によって非表示にしたければ、表示条件＝入力不可とすべし。
		if (design.mobileInvisibleFlag
				&& ViewWidth.isMobile(ctx)
				&& !isRenderAsInput(dcType, ctx)		// 表示条件区分=入力可は「モバイル端末なら非表示」でも隠せない
				&& ctx.renderMode != RenderMode.DESIGN	// デザイン時は全部表示しないと編集できなくなってしまう
		){
			return "hidden-" + prefix;
		}

		// プレフィックスに該当する幅の定義
		final Integer columnSize = "xs".equals(prefix) ? design.colXs
				: "sm".equals(prefix) ? design.colSm
				: "md".equals(prefix) ? design.colMd
				: "lg".equals(prefix) ? design.colLg
				: null;

		if (columnSize != null) {
			if (columnSize == 0)	// 0は非表示扱い
				return "hidden-" + prefix;
			else
				return "col-" + prefix + "-" + columnSize;
		}

		return null;
	}

	/** 幅に関するCSSクラスをリスト化 */
	protected List<String> toWidthCssClassList(P parts, D design, DesignerContext ctx, String...extCssClasses) {
		final List<String> list = asList(extCssClasses);
		final int dcType = parts.dcType;
		final boolean renderAsBootstrap = RenderingMethod.BOOTSTRAP_GRID == design.renderingMethod;

		// プロパティから幅の定義を抜き出して Bootstrapの 'col-*-n'の形にリスト化
		final String[] sizes = { "xs", "sm", "md", "lg" };
		boolean minSize = false;
		for (String size : sizes) {
			String sizeClassCss = getColSizeCss(size, dcType, design, ctx);
			if (isNotEmpty(sizeClassCss)) {
				list.add(sizeClassCss);
			}
			else if (!minSize && (ViewWidth.isMobile(ctx) || renderAsBootstrap)) {
				// 幅の定義が１つもなければ、幅が常に100%となるよう指定。
				// これがないと幅が不定になってしまうので、必須である
				// ※レンダリング方法＝インラインでは幅は常にautoなので不要
				// ※モバイルなら細かなレイアウトをするのは無理なので、常に幅を100%となるよう指定
				list.add("col-" + size + "-12");
				minSize = true;
			}
		}

		return list;
	}

	/** デザイン時に編集可能とマーキングすべきパーツか */
	protected boolean isEditableInDesignMode(D design, DesignerContext ctx) {
		// デザインモード＋ルートコンテナ配下のパーツだけが編集可能
		return RenderMode.DESIGN == ctx.renderMode
				&& ctx.root.childPartsIds.contains(design.partsId);
	}

	/** パーツ定義のフォントサイズをCSSクラスへ変換 */
	protected String getFontSizeCssClass(D design) {
		if (design.fontSize == null)
			return null;
		if (design instanceof PartsDesignContainer)
			return FontSize.toContainerCssClass(design.fontSize);
		else
			// これで適用されるのはBOX要素の高さでフォントそのものではないから、外しておく。例えばテキストボックスの領域の高さを変えたければ復活させるべし
//			return FontSize.toCssClass(design.fontSize);
			return null;
	}

	/**
	 * パーツの条件に関連する項目をレンダリング用のオブジェクトへ変換して返す
	 * オブジェクトの中身は、
	 * ・evaluate：有効条件の評価結果
	 * ・disabled：パーツの有効・無効の判定結果
	 * ・control ：有効条件による制御対象か
	 * ・json    ：有効条件のJSON文字列
	 * @param parts
	 * @param design
	 * @param ctx
	 * @param ecCache
	 * @return
	 */
	protected EvaluateCondition getEvaluateCondition(P parts, D design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		return partsCondUtils.getEvaluateCondition(parts, design, ctx, ecCache);
	}

	/**
	 * 有効条件定義がある場合、DATA属性[data-ec]を戻す
	 * @param ec
	 * @return
	 */
	protected HtmlStringBuilder toEnabledConditionHtml(EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder();
		if (ec != null && isNotEmpty(ec.targets)) {
			html.appendProperty("data-ec");
		}
		return html;
	}

	/**
	 * 有効条件による制御対象とする際の特別な属性値を返す
	 * @return
	 */
	protected String getEcTargetProperty() {
		return "data-ec-target";
	}

	/**
	 * 計算式を使用するか
	 * @param ctx
	 * @param design
	 * @return
	 */
	protected boolean isUseCalc(DesignerContext ctx, D design) {
		return partsCalcUtils.isUseCalc(ctx, design);
	}

	/**
	 * パーツの計算式定義に関連する項目をレンダリング用のオブジェクトへ変換して返す
	 * @param parts
	 * @param design
	 * @param ctx
	 * @param formulas 計算式
	 * @param values 計算式内で使用する判定元パーツの(レンダリング時点の)値
	 * @param targets (パーツ自身が別のパーツの計算元となっている場合の)計算先パーツID一覧
	 * @param ecFormulas 計算条件
	 * @param ecValues 計算条件内で使用する判定元パーツの(レンダリング時点の)値
	 * @param ecTargets (パーツ自身が別のパーツの計算条件の判定元となっている場合の)計算先パーツID一覧
	 * @return
	 */
	protected CalculateCondition getCalculate(P parts, D design, DesignerContext ctx) {
		if (isUseCalc(ctx, design)) {
			final CalculateCondition cc = new CalculateCondition();

			if (isNotEmpty(design.partsCalcs)) {
				final List<CalculateConditionItem> calculates = new ArrayList<>();
				for (PartsCalc calc : design.partsCalcs) {
					final CalculateConditionItem bean = new CalculateConditionItem();
					bean.formula = partsCalcUtils.createCalcFormula(calc);
					bean.values  = partsCalcUtils.getCalcTargetPartsValueMap(parts, calc, ctx);
					bean.ecFormula = partsCalcUtils.createCalcEcFormula(calc);
					bean.ecValues  = partsCalcUtils.getEcTargetPartsValueMap(parts, calc, ctx);
					bean.callbackFunction = calc.callbackFunction;
					calculates.add(bean);
				}
				cc.calculates = calculates;
			}
			if (ctx.targetCalcMap.containsKey(design.partsId)) {
				final Set<Long> targets = ctx.targetCalcMap.get(design.partsId);
				cc.targets = targets;
			}
			if (ctx.targetCalcEcMap.containsKey(design.partsId)) {
				final Set<Long> ecTargets = ctx.targetCalcEcMap.get(design.partsId);
				cc.ecTargets = ecTargets;
			}

			return cc;
		}
		return null;
	}

	/**
	 * 計算式定義がある場合、DATA属性[data-calculate]を戻す
	 * @param cc
	 * @return
	 */
	protected HtmlStringBuilder toCalculateConditionHtml(CalculateCondition cc) {
		final HtmlStringBuilder html = new HtmlStringBuilder();
		if (cc != null) {
			if (isNotEmpty(cc.calculates) || isNotEmpty(cc.targets) || isNotEmpty(cc.ecTargets)) {
				html.appendProperty("data-calculate");
			}
		}
		return html;
	}

	/** パーツイベントのJSON化 */
	protected String toPartsEventsHtml(PartsDesign d) {
		if (d.events != null && !d.events.isEmpty()) {
			// イベントに対する関数名が定義されているものだけをJSON化
			final Map<String, Map<String, String>> json = d.events
					.stream()
					.filter(ev -> isNotEmpty(ev.functionName))
					.collect(Collectors.toMap(ev -> ev.eventName, ev -> {
				final Map<String, String> map = new HashMap<>();
				map.put("functionName", ev.functionName);
				map.put("functionParameter", ev.functionParameter);
				return map;
			}));
			return (json.isEmpty() ? null : toJson(json));
		}
		return null;
	}
}
