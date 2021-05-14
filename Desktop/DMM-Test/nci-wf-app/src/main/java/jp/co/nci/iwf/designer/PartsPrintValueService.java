package jp.co.nci.iwf.designer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.pdf.PartsPdfValueService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.renderer.IPartsRenderer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;

/**
 * パーツの印刷用の値を求めるサービス。
 * 通常の文字を求めるだけならこれだけで十分なのだが、例えばスタンプとか画像パーツとかを印刷対象にしようとすると
 * 印刷プラットフォーム用に追加で変換が必要になる。 * その場合は、例えばPDFなら PartsPdfValueService を使うべし。
 * @see PartsPdfValueService
 */
@ApplicationScoped
public class PartsPrintValueService {
	/** パーツレンダラーのファクトリー */
	@Inject private PartsRenderFactory factory;

	/**
	 * ルートコンテナの直下のパーツをMap化
	 * @param root 画面のルートコンテナ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public Map<String, Object> toHeaderMap(PartsRootContainer root, DesignerContext ctx) {
		final PartsContainerRow row = (PartsContainerRow)root.rows.get(0);
		return rowToMap(row, ctx);
	}

	/**
	 * リピーターやグリッドなどのコンテナパーツから明細行Mapを抜き出してList化。明細行Mapのキーはデザインコード
	 * @param c リピーターやグリッドなどのコンテナパーツ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public List<Map<String, Object>> containerToList(PartsContainerBase<?> c, DesignerContext ctx) {
		if (c == null || c.rows == null)
			return new ArrayList<>();

		return c.rows
				.stream()
				.map(row -> rowToMap(row, ctx))
				.collect(Collectors.toList());
	}

	/**
	 * コンテナパーツの明細行をMap化。明細行Mapのキーはデザインコード
	 * @param row コンテナパーツの明細行
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> rowToMap(PartsContainerRow row, DesignerContext ctx) {
		Map<String, Object> map = new LinkedHashMap<>();
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			final IPartsRenderer r = factory.get(d.partsType);
			final Object val = r.getPrintValue(p, d, ctx);
			if (val != null)
				map.put(d.designCode, val);	// デザインコードは行情報を含まないHTML_IDみたいなもの
		}
		return map;
	}

}
