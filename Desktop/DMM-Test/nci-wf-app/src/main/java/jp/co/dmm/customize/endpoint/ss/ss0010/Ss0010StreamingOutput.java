package jp.co.dmm.customize.endpoint.ss.ss0010;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;

import jp.co.dmm.customize.endpoint.ss.ss0010.excel.Ss0010Book;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.ClassPathResource;
import jp.co.nci.iwf.util.MiscUtils;

public class Ss0010StreamingOutput extends MiscUtils implements StreamingOutput {
	private static final String SHEETNAME_SSGLSNDINF = "SS-GL送信情報";
	private static final String SHEETNAME_SSGLSNDINFHD = "SS-GL送信情報(ヘッダー)";
	private static final String SHEETNAME_SSGLSNDINFDT = "SS-GL送信情報(明細)";
	private static final String SHEETNAME_SSAPSNDINF = "SS-AP送信情報";
	private static final String SHEETNAME_SSAPSNDINFHD = "SS-AP送信情報(ヘッダー)";
	private static final String SHEETNAME_SSAPSNDINFPD = "SS-AP送信情報(支払明細)";
	private static final String SHEETNAME_SSAPSNDINFDT = "SS-AP送信情報(明細)";
	/** 処理結果Excel */
	private Ss0010Book book;

	/**
	 * コンストラクタ
	 * @param book
	 */
	public Ss0010StreamingOutput(Ss0010Book book) {
		this.book = book;
	}

	/**
	 * 書き込み
	 */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		// ダウンロードモニターへ開始を通知
		final DownloadNotifyService notify = CDI.current().select(DownloadNotifyService.class).get();
		notify.begin();

		final ClassPathResource cpr = new ClassPathResource("excel/ss0010.xlsx");
		final File template = cpr.copyFile();
		try {
			// 処理結果を書き戻してやる
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(template))) {
				if (book == null) {
					// 空EXCELテンプレートをそのまま返す
					IOUtils.copy(bis, output);
				} else {
					// 空EXCELテンプレートに処理結果を書き込んで返す
					writeWorkbook(bis, output);
				}
			}
			catch (EncryptedDocumentException | InvalidFormatException e) {
				throw new WebApplicationException(e);
			}
		}
		finally {
			// ダウンロードモニターへ終了を通知
			notify.end();

			if (template.exists())
				template.delete();
		}
	}

	/** 処理結果をEXCELファイルに書き込み */
	private void writeWorkbook(BufferedInputStream bis, OutputStream output)
			throws IOException, InvalidFormatException {

		final Ss0010ExcelWriter writer = CDI.current().select(Ss0010ExcelWriter.class).get();
		try (Workbook wb = WorkbookFactory.create(bis)) {
			/*
			 * 不要なシートを削除
			 */
			List<String> removeSheetName = new ArrayList<String>();
			if (book.sheetGLSndInf==null) removeSheetName.add(SHEETNAME_SSGLSNDINF);
			if (book.sheetGLSndInfHd==null) removeSheetName.add(SHEETNAME_SSGLSNDINFHD);
			if (book.sheetGLSndInfDt==null) removeSheetName.add(SHEETNAME_SSGLSNDINFDT);
			if (book.sheetAPSndInf==null) removeSheetName.add(SHEETNAME_SSAPSNDINF);
			if (book.sheetAPSndInfHd==null) removeSheetName.add(SHEETNAME_SSAPSNDINFHD);
			if (book.sheetAPSndInfPd==null) removeSheetName.add(SHEETNAME_SSAPSNDINFPD);
			if (book.sheetAPSndInfDt==null) removeSheetName.add(SHEETNAME_SSAPSNDINFDT);
			for (String name : removeSheetName) {
				wb.removeSheetAt(wb.getSheetIndex(name));
			}
			final Map<Integer, CellStyle> styles = new HashMap<>();
			for (Sheet sheet : wb) {
				final String name = sheet.getSheetName();
				if (eq(SHEETNAME_SSGLSNDINF, name))
					writer.writeSSGLSndInf(sheet, book.sheetGLSndInf, styles);
				else if (eq(SHEETNAME_SSGLSNDINFHD, name))
					writer.writeSSGLSndInfHd(sheet, book.sheetGLSndInfHd, styles);
				else if (eq(SHEETNAME_SSGLSNDINFDT, name))
					writer.writeSSGLSndInfDt(sheet, book.sheetGLSndInfDt, styles);
				else if (eq(SHEETNAME_SSAPSNDINF, name))
					writer.writeSSAPSndInf(sheet, book.sheetAPSndInf, styles);
				else if (eq(SHEETNAME_SSAPSNDINFHD, name))
					writer.writeSSAPSndInfHd(sheet, book.sheetAPSndInfHd, styles);
				else if (eq(SHEETNAME_SSAPSNDINFPD, name))
					writer.writeSSAPSndInfPd(sheet, book.sheetAPSndInfPd, styles);
				else if (eq(SHEETNAME_SSAPSNDINFDT, name))
					writer.writeSSAPSndInfDt(sheet, book.sheetAPSndInfDt, styles);
			}
			wb.write(output);
		}
	}
}
