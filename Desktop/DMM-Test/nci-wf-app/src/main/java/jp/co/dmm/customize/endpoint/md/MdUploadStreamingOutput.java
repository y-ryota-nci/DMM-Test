package jp.co.dmm.customize.endpoint.md;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
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

import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.util.ClassPathResource;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロード結果をEXCELとして書き込み、ダウンロード処理を行う
 */
public class MdUploadStreamingOutput extends MiscUtils implements StreamingOutput {

	/** 処理結果エクセルBook */
	private MdExcelBook book;

	/** コンストラクタ */
	public MdUploadStreamingOutput(MdExcelBook book) {
		this.book = book;
	}

	/** 書き込み */
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		// ダウンロードモニターへ開始を通知
		final DownloadNotifyService notify = CDI.current().select(DownloadNotifyService.class).get();
		notify.begin();

		final ClassPathResource cpr = new ClassPathResource("excel/md0010.xlsx");
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

		final MdExcelWriter writer = CDI.current().select(MdExcelWriter.class).get();
		try (Workbook wb = WorkbookFactory.create(bis)) {
			final Map<Integer, CellStyle> styles = new HashMap<>();
			for (Sheet sheet : wb) {
				final String name = sheet.getSheetName();
				if (eq("取引先マスタ", name))
					writer.writeSplr(sheet, book.sheetSplr, styles);
				else if (eq("振込先マスタ", name))
					writer.writePayeeBnkacc(sheet, book.sheetAcc, styles);
				else if (eq("関係先マスタ", name))
					writer.writeRltPrt(sheet, book.sheetRlt, styles);
				else if (eq("反社情報", name))
					writer.writeOrgCrm(sheet, book.sheetOrg, styles);
			}
			wb.write(output);
		}
	}
}
