package jp.co.nci.iwf.endpoint.vd.vd0030.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import jp.co.nci.iwf.component.CodeBook.ViewWidth;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.endpoint.vd.vd0010.excel.Vd0010CalcParts;
import jp.co.nci.iwf.endpoint.vd.vd0010.excel.Vd0010CondParts;
import jp.co.nci.iwf.util.ClassPathResource;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * 画面一覧のEXCELダウンロード用StreamingOutput
 */
public class Vd0030OutputStreamExcel extends PoiUtils implements StreamingOutput {
	/** ダウンロード通知サービス */
	private DownloadNotifyService notify;
	/** 画面ロードサービス */
	private ScreenLoadService screenLoadService;
	/** 対象コンテナID */
	private long screenId;
	/** ルックアップサービス */
	private MwmLookupService lookup;

	/**
	 * コンストラクタ
	 */
	public Vd0030OutputStreamExcel(long screenId) {
		this.screenId = screenId;
		this.notify = CDI.current().select(DownloadNotifyService.class).get();
		this.screenLoadService = CDI.current().select(ScreenLoadService.class).get();
		this.lookup = CDI.current().select(MwmLookupService.class).get();
	}

	/**
	 * コンテンツ出力
	 */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		// ダウンロードモニターへ開始を通知
		notify.begin();

		final ClassPathResource cpr = new ClassPathResource("excel/vd0030.xlsx");
		final File template = cpr.copyFile();
		try {
			// コンテナ定義をロード
			final ViewWidth viewWidth = ViewWidth.XS;	// ダミー値なので何でもいい
			final DesignerContext ctx = DesignerContext.designInstance(viewWidth);
			screenLoadService.loadScreenParts(screenId, ctx);

			// 空EXCELテンプレートへシート単位でコンテナ情報を書き込む
			try (Workbook wb = WorkbookFactory.create(template)) {
				final Map<Integer, CellStyle> styles = new HashMap<>();
				writeSheetScreen(wb, ctx, styles);
				writeSheetEc(wb, ctx, styles);
				writeSheetCalc(wb, ctx, styles);
				wb.write(output);
			}
			catch (EncryptedDocumentException | InvalidFormatException e) {
				throw new WebApplicationException(e);
			}
		}
		finally {
			// ダウンロードモニターへ終了を通知
			notify.end();

			// 作業ファイルは不要になったので削除
			if (template.exists())
				template.delete();
		}
	}

	/** 計算式シートの書き込み */
	private void writeSheetCalc(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("計算式");

		final Map<Long, List<PartsCalc>> calcMap = screenLoadService.getScreenCalcMap(ctx.screenId);
		final Map<Long, PartsDesign> designMap = ctx.designMap;
		final Vd0030CalcSheet bean = new Vd0030CalcSheet();
		bean.screenCode = ctx.screenCode;
		bean.screenName = ctx.screenName;
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.outputDate = now();
		bean.partsList = designMap.values().stream()
				.filter(d -> calcMap.containsKey(d.partsId))
				.flatMap(d -> toRow(d, ctx.designMap))
				.collect(Collectors.toList());

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.screenCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.screenName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);

		// 本文
		int r = 7;
		for (Vd0010CalcParts calc : bean.partsList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = getOrCreateRow(sheet, r);
			final Row nextRow = getOrCreateRow(sheet, ++r);
			copyRow(row, nextRow, styles, false);

			int c = 1;
			setCellValue(row, c++, calc.designCode);
			setCellValue(row, c++, calc.labelText);
			setCellValue(row, c++, calc.calcName);
			setCellValue(row, c++, calc.calcExpression);
			setCellValue(row, c++, calc.callbackFunction);
			setCellValue(row, c++, calc.sortOrder);
			setCellValue(row, c++, calc.defaultFlag);
			setCellValue(row, c++, calc.ecExpression);
		}
	}

	/** 計算式の行データに変換 */
	private Stream<Vd0010CalcParts> toRow(PartsDesign design, Map<Long, PartsDesign> designMap) {
		// 論理演算子一覧
		final Map<String, String> logiclOperators = lookup.getNameMap(LookupGroupId.LOGICAL_OPERATOR);
		// 括弧一覧
		final Map<String, String> parentheses = lookup.getNameMap(LookupGroupId.PARENTHESIS);
		// 比較演算子一覧
		final Map<String, String> comparisonOperators = lookup.getNameMap(LookupGroupId.COMPARISON_OPERATOR);
		// 算術演算子一覧
		final Map<String, String> arithmeticOperators = lookup.getNameMap(LookupGroupId.ARITHMETIC_OPERATOR);

		return design.partsCalcs.stream().map(pc -> new Vd0010CalcParts(
				design, pc, designMap, logiclOperators, parentheses, comparisonOperators, arithmeticOperators));
	}

	/** 条件シートの書き込み */
	private void writeSheetEc(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("条件設定");

		final Map<Long, List<PartsCond>> ecMap = screenLoadService.getScreenCondMap(ctx.screenId);
		final Map<Long, PartsDesign> designMap = ctx.designMap;
		final Vd0030EcSheet bean = new Vd0030EcSheet();
		bean.screenCode = ctx.screenCode;
		bean.screenName = ctx.screenName;
		bean.containerCode = ctx.root.containerCode;
		bean.containerName = ctx.root.containerName;
		bean.outputDate = now();
		bean.partsList = designMap.values().stream()
				.filter(d -> ecMap.containsKey(d.partsId))
				.sorted((d1, p2) -> compareTo(d1.sortOrder, p2.sortOrder))
				.flatMap(p -> toCondRow(p, ctx.designMap))
				.collect(Collectors.toList());

		// ヘッダ部
		getOrCreateCell(sheet, 1, 2).setCellValue(bean.screenCode);
		getOrCreateCell(sheet, 2, 2).setCellValue(bean.screenName);
		getOrCreateCell(sheet, 3, 2).setCellValue(bean.outputDate);

		// 本文
		int r = 7;
		for (Vd0010CondParts parts : bean.partsList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = getOrCreateRow(sheet, r);
			final Row nextRow = getOrCreateRow(sheet, ++r);
			copyRow(row, nextRow, styles, false);

			int c = 1;
			setCellValue(row, c++, parts.designCode);
			setCellValue(row, c++, parts.labelText);
			setCellValue(row, c++, parts.partsConditionTypeName);
			setCellValue(row, c++, parts.expression);
			setCellValue(row, c++, parts.callbackFunction);
		}
	}

	/** 条件式の行データに変換 */
	private Stream<Vd0010CondParts> toCondRow(PartsDesign design, Map<Long, PartsDesign> designMap) {
		// 論理演算子一覧
		final Map<String, String> logiclOperators = lookup.getNameMap(LookupGroupId.LOGICAL_OPERATOR);
		// 括弧一覧
		final Map<String, String> parentheses = lookup.getNameMap(LookupGroupId.PARENTHESIS);
		// 比較演算子一覧
		final Map<String, String> comparisonOperators = lookup.getNameMap(LookupGroupId.COMPARISON_OPERATOR);

		return design.partsConds.stream().map(pc -> new Vd0010CondParts(
				design, pc, designMap, logiclOperators, parentheses, comparisonOperators));
	}

	/** 画面シートへの書き込み */
	private void writeSheetScreen(Workbook wb, DesignerContext ctx, Map<Integer, CellStyle> styles) {
		final Sheet sheet = wb.getSheet("画面情報");

		// ヘッダ部
		int i = 1;
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screenCode);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screenName);
		getOrCreateCell(sheet, i++, 2).setCellValue(now());
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.root.containerCode);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.root.containerName);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screenCustomClass);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screen.submitFuncName);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screen.submitFuncParam);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screen.loadFuncName);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screen.loadFuncParam);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screen.changeStartUserFuncName);
		getOrCreateCell(sheet, i++, 2).setCellValue(ctx.screen.changeStartUserFuncParam);
	}

}
