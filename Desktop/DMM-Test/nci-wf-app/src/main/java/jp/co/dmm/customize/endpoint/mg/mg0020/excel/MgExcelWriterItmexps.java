package jp.co.dmm.customize.endpoint.mg.mg0020.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 費目情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterItmexps extends MgExcelWriter<MgExcelSheetItmexps> {

	/** 費目マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetItmexps sheetItm, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityItmexps itm : sheetItm.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, itm.processType);
			// 会社コード
			setCellValue(row, i++, itm.companyCd);

			setCellValue(row, i++, itm.itmexpsCd);
			setCellValue(row, i++, itm.itmexpsNm);
			setCellValue(row, i++, itm.itmexpsNmS);
			setCellValue(row, i++, itm.itmexpsLevel);
			setCellValue(row, i++, itm.dltFg);

			// エラー
			setCellValue(row, i++, itm.errorText);
		}
	}
}
