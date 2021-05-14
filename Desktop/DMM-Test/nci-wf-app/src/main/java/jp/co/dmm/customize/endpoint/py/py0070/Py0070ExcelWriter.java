package jp.co.dmm.customize.endpoint.py.py0070;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.py.py0070.excel.Py0070SheetPayableBal;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * 買掛残高データのEXCELファイルのライター
 */
@ApplicationScoped
public class Py0070ExcelWriter extends PoiUtils {

	/**
	 * 買掛残高情報書き込み処理
	 * @param sheet
	 * @param sheetPayableBal
	 * @param styles
	 */
	public void writePayableBal(Sheet sheet, Py0070SheetPayableBal sheetPayableBal, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Py0070Entity dat : sheetPayableBal.payableBals) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			setCellValue(row, i++, dat.companyCd);
			setCellValue(row, i++, dat.companyNm);
			setCellValue(row, i++, dat.splrCd);
			setCellValue(row, i++, dat.splrNmKj);
			setCellValue(row, i++, dat.accCd);
			setCellValue(row, i++, dat.accNm);
			setCellValue(row, i++, dat.prvBalAmtJpy);
			setCellValue(row, i++, dat.dbtAmtJpy);
			setCellValue(row, i++, dat.cdtAmtJpy);
			setCellValue(row, i++, dat.nxtBalAmtJpy);
		}
	}
}
