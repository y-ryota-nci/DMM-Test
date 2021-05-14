package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.component.tableSearch.TableSearchEntity;
import jp.co.nci.iwf.component.tableSearch.TableSearchService;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleMasterParts;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignMaster;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsMaster;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 汎用マスタ選択パーツのレンダラー
 */
@ApplicationScoped
public class PartsRendererMaster extends PartsRendererAjax<PartsMaster, PartsDesignMaster> implements RoleMasterParts {
	/** 絞り込み条件に該当するI/O区分 */
	private static final Set<String> IO_TYPES_IN =
			new HashSet<>(Arrays.asList(PartsIoType.IN, PartsIoType.BOTH));

	@Inject private TableSearchService tableSearchService;

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
	protected String renderInput(PartsMaster parts, PartsDesignMaster design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		if (isDesignMode) {
			html.append("<button class='btn btn-default dropdown-toggle' style='width:100%; text-align:right;'>");
			html.append("<span class='caret'></span>");
			html.append("</button>");
		}
		else {
			html.append("<select ");

			// CSSクラス
			final String required = design.requiredFlag ? "required" : "";
			// 読取条件により読取専用となった場合、下記のclass属性を付与
			final String readonly = design.readonly ? "readonly" : "";	// パーツ属性のreadonly
			final String ecReadonly = ec.readonly ? "ecReadonly" : "";	// パーツ条件設定の判定結果である読取専用
			html.append(toCssClassHtml(design.cssClass, "form-control", required, readonly, ecReadonly));

			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));

			// タブ順を付与
			if (!design.grantTabIndexFlag)
				html.appendProperty("tabindex", "-1");

			// 活性・非活性の設定
			// パーツ条件によりパーツが無効であれば非活性
			if (!ec.enabled)
				html.appendProperty("disabled");

			// 役割コード
			html.appendProperty("data-role", CODE);

			// 有効条件により活性・非活性を切り替えるための属性を付与
			// これがある要素が制御対象となる
			if (ec.control)
				html.appendProperty( getEcTargetProperty() );

			// Ajaxのトリガーとなれるパーツなら属性を付与
			// これを設定しておくことでクライアント側で汎用マスタ検索パーツのトリガーパーツと認識してスクリプトが動く
			if (isNotEmpty(parts.ajaxTriggers))
				html.appendProperty("data-ajax-triggers");

			// マスタ選択用の属性を出力
			if (design.tableSearchId != null) {
				// 属性を付与（これを設定しておくことでクライアント側で汎用マスタ検索パーツと認識してスクリプトが動く）
				html.appendProperty("data-table-search-id");
			}

			html.append(">");
			html.append(getOptionItems(parts, design, ctx, ec));
			html.append("</select>");
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
	protected String renderReadonly(PartsMaster parts, PartsDesignMaster design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {

		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<output ");
		// CSSクラス
		html.append(toCssClassHtml(design.cssClass, "form-control-static"));
		// CSSスタイル
		List<String> styles = toCssStyleList(parts, design, ctx);
		html.append(toCssStyleHtml(design.cssStyle, styles));

		html.append(">");

		String label = parts.values.get(LABEL);
		if (isNotEmpty(label)) {
			html.appendEscape(label);
		}
		html.append("</output>");
		return html.toString();
	}

	/** 選択肢(OPTIONタグ)のHTMLを生成 */
	private CharSequence getOptionItems(PartsMaster p, PartsDesignMaster d, DesignerContext ctx, EvaluateCondition ec) {

		//  パーツ関連に定義された「絞込条件に使用されるカラム名」と、それ対応したパーツの値を抜き出してMap化
		// ※ここではマスタ選択パーツ自身の値は渡していないため、選択肢だけが抽出される
		final Map<String, String> partsValues = toConditionPartsValueMap(p, d, ctx);

		// 汎用テーブル検索条件に従って検索処理を実行
		final List<TableSearchEntity> results = tableSearchService.search(d.tableSearchId, partsValues);

		// 選択肢：空行
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		if (d.emptyLineType == EmptyLineType.USE_ALWAYS
				|| (d.emptyLineType == EmptyLineType.USE_MULTI && results.size() > 1)
		) {
			html.append("<option value=''");
			if (isEmpty(p.values.get(CODE))) {
				html.appendProperty("selected");
			}
			else if (d.readonly || ec.readonly) {
				// 読取専用条件が"true"であれば非活性・非表示にする
				html.appendProperty("disabled");
				html.append(toCssClassHtml(null, "hide"));
			}
			html.append(">--");
		}
		// 選択肢
		for (TableSearchEntity r : results) {
			String value = r.get(d.columnNameValue);
			String label = r.get(d.columnNameLabel);
			html.append("<option ");
			html.appendProperty("value", value);
			if (same(value, p.values.get(CODE)) && ec.enabled) {
				html.appendProperty("selected");
			} else if (d.readonly || ec.readonly) {
				// 読取専用条件が"true"であれば非活性・非表示にする
				html.appendProperty("disabled");
				html.append(toCssClassHtml(null, "hide"));
			}
			html.append(">");
			html.appendEscape(label);
		}
		return html;
	}

	/** パーツ関連に定義された「絞込条件に使用されるカラム名」と、それ対応したパーツの値を抜き出してMap化 */
	private Map<String, String> toConditionPartsValueMap(PartsMaster p, PartsDesignMaster d, DesignerContext ctx) {
		// パーツ関連に定義された「絞込条件に使用されるパーツIDとカラム名」をMap化
		final Map<String, Long> partsIdsByColumn = d.relations.stream()
			.filter(r -> IO_TYPES_IN.contains(r.partsIoType))
			.collect(Collectors.toMap(r -> r.columnName, r -> r.targetPartsId));

		// パーツ関連から、絞り込み条件として使うパーツの値を抜き出してMap化
		final Map<String, String> inits = new HashMap<>();
		for (String columnName : partsIdsByColumn.keySet()) {
			final Long partsId = partsIdsByColumn.get(columnName);
			final PartsBase<?> target = PartsUtils.findParts(partsId, p.htmlId, ctx);
			if (target == null)
				throw new InternalServerErrorException("対象パーツが見つかりません partsId=" + partsId + " htmlId=" + p.htmlId);
			inits.put(columnName, target.getValue());
		}
		return inits;
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsMaster p, PartsDesignMaster d, DesignerContext ctx) {
		return defaults(p.values.get(LABEL), "");
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsMaster p, PartsDesignMaster d, DesignerContext ctx) {
		return defaults(p.values.get(LABEL), "");
	}
}
