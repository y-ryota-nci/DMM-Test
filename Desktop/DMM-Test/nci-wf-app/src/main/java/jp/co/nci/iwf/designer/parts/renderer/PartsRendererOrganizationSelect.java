package jp.co.nci.iwf.designer.parts.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOrganizationSelect;
import jp.co.nci.iwf.designer.parts.design.PartsDesignUserSelect;
import jp.co.nci.iwf.designer.parts.runtime.PartsOrganizationSelect;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 組織選択パーツレンダラー
 */
@ApplicationScoped
public class PartsRendererOrganizationSelect extends PartsRenderer<PartsOrganizationSelect, PartsDesignOrganizationSelect> {

	/** ログイン者情報 */
	@Inject
	protected SessionHolder sessionHolder;

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
	protected String renderInput(PartsOrganizationSelect parts, PartsDesignOrganizationSelect design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		// 必須か
		final String required = (!isDesignMode && design.requiredFlag) ? "required" : "";

		// インライン配置用SPAN
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<span class='form-inline'>");

		// 会社コード、組織コードは常に出力対象
		html.append( this.createBaseItemHtml(parts, design, ctx.renderMode, PartsDesignOrganizationSelect.ROLE_CODES[0]) );
		html.append( this.createBaseItemHtml(parts, design, ctx.renderMode, PartsDesignOrganizationSelect.ROLE_CODES[1]) );

		// 表示項目を並び順にてソートしたマップを生成
		final Map<String, Map<String, String>> itemValueMap = this.createDiaplayItemValueMap(design);
		// 表示項目のHTMLを生成
		for (String key : itemValueMap.keySet()) {
			html.append( this.createDisplayItemHtml(parts, design, ctx, key, itemValueMap.get(key)) );
		}

		// 以下、検索ボタン・クリアボタン部の出力
		final String buttonSize = getButtonSizeCssClass(design.buttonSize);
		{
			html.append("<span class='input-group'>");
			// 検索ボタン
			{
				html.append("<a ");

				// CSSクラス
				html.append(toButtonCssClassHtml(design, "btn-selectOrganization", "ec-target", required, buttonSize));

				// タブ順を付与
				if (!design.grantTabIndexFlag)
					html.appendProperty("tabindex", "-1");

				// パーツ条件の評価結果に伴う活性／非活性の切替
				// パーツ条件によりパーツが無効ないし読取専用であれば非活性
				if (!ec.enabled || ec.readonly)
					html.appendProperty("disabled");

				// 有効条件により活性・非活性を切り替えるための属性を付与
				// これがある要素が制御対象となる
				if (ec.control)
					html.appendProperty( getEcTargetProperty() );

				// 自身がユーザ選択パーツの関連パーツであれば出力
				ctx.designMap.values().stream().filter(p -> eq(p.partsType, PartsType.USER)).forEach(p -> {
					PartsDesignUserSelect user = (PartsDesignUserSelect)p;
					if (eq(user.partsIdOrgCondition, design.partsId)) {
						// 関連パーツIDより対象の組織パーツを見つけ出し、そのパーツのHtmlIdをdataフィルードに出力する
						html.appendProperty("data-relation", user.partsCode);
						return;
					}
				});

				html.append(">");
				html.append("<i class='glyphicon glyphicon-home'></i>");
				html.append("</a>");
			}
			// クリアボタン
			{
				html.append("<a ");

				// CSSクラス
				html.append(toButtonCssClassHtml(design, "btn-clearOrganization", buttonSize));

				// タブ順を付与
				if (!design.grantTabIndexFlag)
					html.appendProperty("tabindex", "-1");

				// パーツ条件の評価結果に伴う活性／非活性の切替
				// パーツ条件によりパーツが無効ないし読取専用であれば非活性
				if (!ec.enabled || ec.readonly)
					html.appendProperty("disabled");

				// パーツ条件により活性・非活性を切り替えるための属性を付与
				// これがある要素が制御対象となる
				if (ec.control)
					html.appendProperty( getEcTargetProperty() );

				html.append(">");
				html.append("<i class='glyphicon glyphicon-remove-circle'></i>");
				html.append("</a>");
			}
			html.append("</span>");
		}

		html.append("</span>");	// end of <span class='form-inline'>

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
	protected String renderReadonly(PartsOrganizationSelect parts, PartsDesignOrganizationSelect design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		if (!isDesignMode) {
			// 組織選択パーツを囲むDIVタグを生成
			html.append("<div class='form-inline' ");
			html.appendProperty("id", parts.htmlId);
			html.append(">");

			// 会社コード、組織コード
			html.append( this.createDisplayItemHtml4ReadOnly(parts, design, ctx, "corporationCode", null, "hide") );
			html.append( this.createDisplayItemHtml4ReadOnly(parts, design, ctx, "organizationCode", null, "hide") );

			// 表示項目を並び順にてソートしたマップを生成
			final Map<String, Map<String, String>> itemValueMap = this.createDiaplayItemValueMap(design);
			// 表示項目のHTMLを生成
			itemValueMap.keySet().stream().forEach(k -> {
				html.append( this.createDisplayItemHtml4ReadOnly(parts, design, ctx, k, itemValueMap.get(k), null) );
			});
		}

		return html.toString();
	}

	private String createDisplayItemHtml4ReadOnly(PartsOrganizationSelect parts, PartsDesignOrganizationSelect design, DesignerContext ctx, String itemName, Map<String, String> values, String addCssClass) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		// 表示の有無
		// 非表示項目であれば下記処理は行わない
		String display = values == null ? "true" : values.get("Display");
		if (isNotEmpty(display) && toBool(display)) {
			html.append("<output ");
			html.appendProperty("data-role", itemName);
			// CSSクラス
			List<String> widthCssClasses = this.toWidthCssClassList(design, values);
			widthCssClasses.add("input-group form-control-static");
			widthCssClasses.add(addCssClass);
			html.append(toCssClassHtml(design.cssClass, widthCssClasses));
			// CSSスタイル
			List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			html.append(">");

			// 値
			final String val = parts.values.get(itemName);
			html.appendEscape(val);

			html.append("</output>");
		}

		return html.toString();
	}

	/**
	 * 会社コード、組織コードは常に出力対象
	 */
	private String createBaseItemHtml(PartsOrganizationSelect parts, PartsDesignOrganizationSelect design, RenderMode renderMode, String itemName) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<input type='hidden' ");
		html.appendProperty("data-role", itemName);
		html.appendProperty("value", parts.values.get(itemName));
		html.append("/>");
		return html.toString();
	}

	/**
	 * 表示項目のHTML生成.
	 * @param parts
	 * @param design
	 * @param itemName
	 * @param values
	 * @param renderMode
	 * @return
	 */
	private String createDisplayItemHtml(PartsOrganizationSelect parts, PartsDesignOrganizationSelect design, DesignerContext ctx, String itemName, Map<String, String> values) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		// 表示の有無
		// 非表示項目であれば下記処理は行わない
		String display = values.get("Display");
		if (isNotEmpty(display) && toBool(display)) {

			html.append("<span ");
			// 幅に関するCSSクラスを取得
			List<String> widthCssClasses = this.toWidthCssClassList(design, values);
			html.append(toCssClassHtml("input-group", widthCssClasses));
			html.append(">");

			// 項目表示用のテキストボックスを設定
			html.append("<input ");
			// 役割コード
			html.appendProperty("data-role", itemName);
			// CSSクラス
			html.append(toCssClassHtml(design.cssClass, "form-control"));
			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			// タブ順を付与（常に-1を設定しタブ移動させない）
			html.appendProperty("tabindex", "-1");
			// 読取専用
			html.appendProperty("readonly");
			// 表示項目は有効条件による判定結果による制御対象外としています
//			// 有効条件の評価結果に伴う活性／非活性の切替
//			if (!evaluate) {
//				html.appendProperty("disabled");
//			}
			// 値
			html.appendProperty("value", parts.values.get(itemName));
			html.append(" />");

			html.append("</span>");
		}
		return html.toString();
	}

	/**
	 * プロパティから幅の定義を抜き出して BootstrapのCSSクラス 'col-*-n'形式で返す
	 * @param prefix CSSプレフィックス（「xs|sm|md|lg」のいずれか）
	 * @param size カラム幅
	 * @return
	 */
	private String getColSizeCss(String prefix, String size) {
		final Integer columnSize = toInt(size);
		if (columnSize != null) {
			return "col-" + prefix + "-" + columnSize;
		}
		return null;
	}

	/** 幅に関するCSSクラスをリスト化 */
	private List<String> toWidthCssClassList(PartsDesignOrganizationSelect design, Map<String, String> valueMap) {
		final List<String> list = new ArrayList<>();

		// プロパティから幅の定義を抜き出して Bootstrapの 'col-*-n'の形にリスト化
		if (valueMap != null) {
			final String xs = getColSizeCss("xs", valueMap.get("ColXs"));
			if (isNotEmpty(xs)) list.add(xs);

			final String sm = getColSizeCss("sm", valueMap.get("ColSm"));
			if (isNotEmpty(sm)) list.add(sm);

			final String md = getColSizeCss("md", valueMap.get("ColMd"));
			if (isNotEmpty(md)) list.add(md);

			final String lg = getColSizeCss("lg", valueMap.get("ColLg"));
			if (isNotEmpty(lg)) list.add(lg);

			// 幅の定義が１つもなければ、幅が常に100%となるよう指定。
			// これがないと幅が不定になってしまうので、必須である
			// ※レンダリング方法＝インラインでは幅は常にautoなので不要
			if (RenderingMethod.BOOTSTRAP_GRID == design.renderingMethod && list.isEmpty()) {
				list.add("col-xs-12");
			}
		}

		return list;
	}

	/**
	 * 表示項目を並び順にて並び替えたマップを生成.
	 * @param design
	 * @return
	 */
	private Map<String, Map<String, String>> createDiaplayItemValueMap(PartsDesignOrganizationSelect design) {
		Map<Integer, String> sortMap = new TreeMap<Integer, String>();
		sortMap.put(design.organizationNameSortOrder,      PartsDesignOrganizationSelect.ROLE_CODES[2]);
		sortMap.put(design.organizationAddedInfoSortOrder, PartsDesignOrganizationSelect.ROLE_CODES[3]);
		sortMap.put(design.organizationNameAbbrSortOrder,  PartsDesignOrganizationSelect.ROLE_CODES[4]);
		sortMap.put(design.telNumSortOrder,        PartsDesignOrganizationSelect.ROLE_CODES[5]);
		sortMap.put(design.faxNumSortOrder,     PartsDesignOrganizationSelect.ROLE_CODES[6]);
		sortMap.put(design.addressSortOrder,   PartsDesignOrganizationSelect.ROLE_CODES[7]);

		Map<String, Map<String, String>> itemValueMap = new LinkedHashMap<>();
		sortMap.forEach((k, v) -> {
			itemValueMap.put(v, this.createItemMap(design, v));
		});
		return itemValueMap;
	}

	private Map<String, String> createItemMap(PartsDesignOrganizationSelect design, String preffix) {
		try {
			Map<String, String> map = new HashMap<String, String>() {
				{ put("Display", null); }
				{ put("SortOrder", null); }
				{ put("ColLg", null); }
				{ put("ColMd", null); }
				{ put("ColSm", null); }
				{ put("ColXs", null); }
			};
			for (String key : map.keySet()) {
				map.put(key, MiscUtils.toStr(design.getClass().getField(preffix + key).get(design)));
			}
			return map;
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new InternalServerErrorException(e);
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
	public Object getPrintValue(PartsOrganizationSelect p, PartsDesignOrganizationSelect d, DesignerContext ctx) {
		return defaults(p.values.get(RoleOrganizationSelect.ORGANIZATION_NAME), "");
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsOrganizationSelect p, PartsDesignOrganizationSelect d, DesignerContext ctx) {
		return defaults(p.values.get(RoleOrganizationSelect.ORGANIZATION_NAME), "");
	}
}
