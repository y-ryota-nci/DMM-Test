package jp.co.dmm.customize.endpoint.py.py0080.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.py.py0080.Py0080Entity;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * 買掛残高データのEXCELファイルのライター
 */
@ApplicationScoped
public class Py0080ExcelWriter extends PoiUtils {

	/**
	 * 買掛残高情報書き込み処理
	 * @param sheet
	 * @param sheetPayableBal
	 * @param styles
	 */
	public void write(Sheet sheet, Py0080Sheet sheetResults, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Py0080Entity dat : sheetResults.results) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.advpayNo);
			setCellValue(row, i++, dat.payNm);
			setCellValue(row, i++, dat.splrNmKj);
			setCellValue(row, i++, dat.mnyNm);
			setCellValue(row, i++, dat.payAmt);
			setCellValue(row, i++, dat.advpayAplyAmt);
			setCellValue(row, i++, dat.rmnAmt);
		}
	}
}
