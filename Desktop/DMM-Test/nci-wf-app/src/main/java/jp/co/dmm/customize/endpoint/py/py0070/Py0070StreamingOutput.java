package jp.co.dmm.customize.endpoint.py.py0070;

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

import jp.co.dmm.customize.endpoint.py.py0070.excel.Py0070Book;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.ClassPathResource;
import jp.co.nci.iwf.util.MiscUtils;

public class Py0070StreamingOutput extends MiscUtils implements StreamingOutput {
	private static final String SHEETNAME_PAYABLE_BALANCE = "買掛残高";
	/** 処理結果Excel */
	private Py0070Book book;

	public Py0070StreamingOutput(Py0070Book book) {
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

		final ClassPathResource cpr = new ClassPathResource("excel/py0070.xlsx");
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

		final Py0070ExcelWriter writer = CDI.current().select(Py0070ExcelWriter.class).get();
		try (Workbook wb = WorkbookFactory.create(bis)) {
			List<String> removeSheetName = new ArrayList<String>();
			if (book.sheetPayableBal==null) removeSheetName.add(SHEETNAME_PAYABLE_BALANCE);
			final Map<Integer, CellStyle> styles = new HashMap<>();
			for (Sheet sheet : wb) {
				final String name = sheet.getSheetName();
				if (eq(SHEETNAME_PAYABLE_BALANCE, name))
					writer.writePayableBal(sheet, book.sheetPayableBal, styles);
			}
			wb.write(output);
		}
	}
}
