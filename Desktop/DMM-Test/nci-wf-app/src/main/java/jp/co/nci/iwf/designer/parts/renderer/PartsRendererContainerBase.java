package jp.co.nci.iwf.designer.parts.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.service.EvaluateCondition;

/**
 * コンテナのレンダラー抽象基底クラス
 *
 * @param <P>
 * @param <D>
 */
public abstract class PartsRendererContainerBase<P extends PartsContainerBase<D>, D  extends PartsDesignContainer>
		extends PartsRenderer<P, D>
		implements CodeBook {

	@Inject
	protected PartsRenderFactory factory;

	/**
	 * 背景HTMLにパーツのHTMLを展開
	 * @param row コンテナの行データ
	 * @param container パーツ定義（コンテナ）
	 * @param ctx デザイナーコンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String renderWithBgHtml(PartsContainerRow row, D container, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final boolean isDesignMode = ctx.renderMode == RenderMode.DESIGN;
		// パーツの背景HTML連番をキーとして、HTMLをMap化
		final Map<Integer, StringBuilder> map = new HashMap<>(row.children.size());
		final int NO_BG_HTML = Integer.MIN_VALUE;
		for (String htmlId : row.children) {
			// パーツ単独のHTML
			final PartsBase p = ctx.runtimeMap.get(htmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			final IPartsRenderer r = factory.get(p.partsType);
			final String partsHtml = r.render(p, d, ctx, ecCache);

			// 背景HTMLに含まれるパーツは、背景HTMLセル番号をキーにMap化してHTMLを退避
			// 背景HTMLに含まれないものは、それだけを集約してHTMLを退避
			final Integer cellNo = defaults(d.bgHtmlCellNo, NO_BG_HTML);
			StringBuilder cellHtml = map.get(cellNo);
			if (cellHtml == null) {
				cellHtml = new StringBuilder(256);
				map.put(cellNo, cellHtml);
			}
			cellHtml.append(partsHtml);
		}

		final StringBuilder html = new StringBuilder(4096);
		if (isNotEmpty(container.bgHtml))
			html.append(container.bgHtml);

		// 背景HTMLにはセル番号を含んだ置換パターンが埋め込まれているので、
		// それを探し出して直後にパーツのHTMLを挿入
		final Integer[] cellNos = map.keySet().toArray(new Integer[map.size()]);
		for (Integer cellNo : cellNos) {
			// 背景HTMLに含まれないやつ
			if (cellNo == NO_BG_HTML)
				continue;

			final String pattern = toPattern(cellNo);
			final int pos = html.indexOf(pattern);
			if (pos >= 0 && map.containsKey(cellNo)) {
				// デザイン時はパーツ移動時にセル番号を取得するために <aside>が必要なので insert
				// プレビューや実行時はセル番号は確定しているので <aside>が不要だから replace
				final StringBuilder cell = map.remove(cellNo);
				if (isDesignMode)
					html.insert(pos + pattern.length(), cell.toString());
				else
					html.replace(pos,  pos + pattern.length(), cell.toString());
			}
		}

		// 背景HTMLに含まれないパーツのHTMLを追記
		for (StringBuilder otherHtml : map.values())
			html.append(otherHtml);

		// デザインモードならパーツのドラッグ＆ドロップによる移動用マーカーとして <aside>を残しておくが、
		// プレビューや実行時はもうドラッグ＆ドロップすることがないので <aside>をリプレースしてしまう
		if (isDesignMode) {
			return html.toString();
		}
		return html.toString().replaceAll(quotePattern(toPattern(null)), "");
	}

	/**
	 * 背景HTMLなしのパーツのHTMLを展開
	 * @param row コンテナの行データ
	 * @param container パーツ定義（コンテナ）
	 * @param ctx デザイナーコンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String renderWithoutBgHtml(PartsContainerRow row, D container, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final StringBuilder html = new StringBuilder(4096);
		for (String htmlId : row.children) {
			// パーツ単独のHTML
			final PartsBase p = ctx.runtimeMap.get(htmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			final IPartsRenderer r = factory.get(p.partsType);
			final String partsHtml = r.render(p, d, ctx, ecCache);
			html.append(partsHtml);
			html.append(CRLF).append(CRLF);
		}
		return html.toString();
	}

	/** セル番号の検索パターン */
	private String toPattern(Integer cellNo) {
		if (cellNo == null)
			return "<aside class=\"forBgHtml\">\\d+</aside>";
		else
			return "<aside class=\"forBgHtml\">" + cellNo + "</aside>";
	}

	/** 幅に関するCSSクラスをリスト化 */
	protected List<String> toWidthCssClassList(P parts, D design, DesignerContext ctx, String...extCssClasses) {
		final List<String> list = super.toWidthCssClassList(parts, design, ctx, extCssClasses);
		list.add("parts-container");
		return list;
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(P p, D d, DesignerContext ctx) {
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
	public String getDisplayValue(P p, D d, DesignerContext ctx) {
		return null;
	}
}
