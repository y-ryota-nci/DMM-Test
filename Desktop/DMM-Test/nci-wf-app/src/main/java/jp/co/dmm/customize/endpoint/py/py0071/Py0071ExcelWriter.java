package jp.co.dmm.customize.endpoint.py.py0071;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.py.py0071.excel.Py0071SheetPayableBalDtl;
import jp.co.nci.iwf.util.PoiUtils;

/**
 * 買掛残高詳細データのEXCELファイルのライター
 */
@ApplicationScoped
public class Py0071ExcelWriter extends PoiUtils {

	public void writePayableBalDtl(Sheet sheet, Py0071SheetPayableBalDtl sheetPayableBalDtl, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (Py0071Entity dat : sheetPayableBalDtl.payableBalDtls) {
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
			setCellValue(row, i++, dat.addDt);
			setCellValue(row, i++, dat.jrnslpNo);
			setCellValue(row, i++, dat.accCd);
			setCellValue(row, i++, dat.accNm);
			setCellValue(row, i++, dat.accBrkdwnCd);
			setCellValue(row, i++, dat.accBrkdwnMm);
			setCellValue(row, i++, dat.dbtAmtJpy);
			setCellValue(row, i++, dat.cdtAmtJpy);
			setCellValue(row, i++, dat.abst);
		}
	}
}
