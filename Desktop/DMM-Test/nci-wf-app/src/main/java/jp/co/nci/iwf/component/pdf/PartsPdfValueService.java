package jp.co.nci.iwf.component.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.PartsPrintValueService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.util.MiscUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.renderers.SimpleDataRenderer;

/**
 * パーツの値を抜き出して、JasperReport用に変換するサービス。
 * 例えばスタンプパーツや画像パーツなどの値が、PDF生成できるよう特別な変換処理を行っている。
 */
@ApplicationScoped
public class PartsPdfValueService extends MiscUtils {
	/** パーツの印刷用の値を求めるサービス */
	@Inject private PartsPrintValueService printValueService;

	/**
	 * ルートコンテナ配下のパーツの値を、JasperReports用に変換したMapを返す
	 * @param root ルートコンテナ
	 * @param ctx デザイナーコンテキスト
	 */
	public Map<String, Object> toMap(PartsRootContainer root, DesignerContext ctx) {
		// ルートコンテナは常に1行しか明細がない
		final PartsContainerRow row = root.rows.get(0);
		// パーツの値を印刷用に変換したMap。この時点ではプリミティブな型なので、JasperReports用に変換する
		final Map<String, Object> printValueMap = printValueService.toHeaderMap(root, ctx);
		return toJRValueMap(row, ctx, printValueMap);
	}

	/**
	 * コンテナの行をJasperReports用に変換したMapを返す
	 * @param row コンテナの行データ
	 * @param ctx デザイナーコンテキスト
	 * @param printValueMap パーツの値を印刷用に変換したMap。キーはデザインコード
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> toJRValueMap(
			PartsContainerRow row,
			DesignerContext ctx,
			Map<String, Object> printValueMap
	) {
		final Map<String, Object> map = new LinkedHashMap<>();
		for (String childHtmlId : row.children) {
			final PartsBase p = ctx.runtimeMap.get(childHtmlId);
			final PartsDesign d = ctx.designMap.get(p.partsId);
			final String key = d.designCode;
			final Object value = printValueMap.get(key);

			switch (p.partsType) {
			case PartsType.GRID:
			case PartsType.REPEATER:
			case PartsType.STAND_ALONE:
				// JRDataSourceに変換して返す
				final PartsContainerBase c = (PartsContainerBase)p;
				final JRDataSource ds = containerToJRDataSource(c, ctx, printValueMap);
				map.put(key, ds);
				break;
			case PartsType.IMAGE:
				// byte配列をImageに変換
				if (isNotEmpty(value)) {
					final BufferedImage image = byteToImage(value);
					map.put(key, image);
				}
				break;
			case PartsType.STAMP:
				// SVG文字列をRendererに変換
				final SimpleDataRenderer sdr = svgToSimpleDataRenderer(value);
				map.put(key, sdr);
				break;
			default:
				if (printValueMap.containsKey(key)) {
					map.put(key, value);
				}
				break;
			}
		}
		return map;
	}

	/** SVG文字列をJasperのRendererに変換 */
	private SimpleDataRenderer svgToSimpleDataRenderer(Object value) {
		if (value == null) {
			return null;
		}
		try {
			String svg = (String)value;
			return SimpleDataRenderer.getInstance(svg.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new InternalServerErrorException("スタンプパーツをPDF用に変換できませんでした。", e);
		}
	}

	/** コンテナをList<Map<String, Object>の形で JRDataSource化 */
	private JRDataSource containerToJRDataSource(
			PartsContainerBase<? extends PartsDesignContainer> container,
			DesignerContext ctx,
			Map<String, Object> printValueMap
	) {
		final List<Map<String, Object>> containerRows = container.rows
				.stream()
				.map(containerRow -> toJRValueMap(containerRow, ctx, printValueMap))
				.collect(Collectors.toList());
		return JRDataSourceConverter.toDataSource(containerRows);
	}

	/** バイト配列をJasperのImageへ変換 */
	private BufferedImage byteToImage(Object value) {
		if (value == null) {
			return null;
		}
		byte[] image = (byte[])value;
		try {
			return ImageIO.read(new ByteArrayInputStream(image));
		} catch (IOException e) {
			throw new InternalServerErrorException("パーツの画像をPDF用に変換できませんでした。", e);
		}
	}

	/**
	 * コンテナ内のパーツの値をJasperReport用に変換したMapリストを返す
	 * @param container コンテナ
	 * @param ctx デザイナーコンテキスト
	 * @return
	 */
	public List<Map<String, Object>> toMapList(PartsContainerBase<?> container, RuntimeContext ctx) {
		final List<Map<String, Object>> pdfValueMapList = new ArrayList<>();

		// プリミティブな印刷用の値のMapリスト
		final List<Map<String, Object>> printValueMapList = printValueService.containerToList(container, ctx);

		// 一行ずつJasperReport用のMapリストへ変換
		for (int i = 0; i < container.rows.size(); i++) {
			PartsContainerRow row = container.rows.get(i);
			Map<String, Object> printValueMap = printValueMapList.get(i);
			Map<String, Object> map = toJRValueMap(row, ctx, printValueMap);
			pdfValueMapList.add(map);
		}
		return pdfValueMapList;
	}
}
