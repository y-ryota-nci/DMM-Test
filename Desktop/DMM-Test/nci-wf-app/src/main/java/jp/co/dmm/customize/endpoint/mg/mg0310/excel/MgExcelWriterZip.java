package jp.co.dmm.customize.endpoint.mg.mg0310.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 郵便番号情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterZip extends MgExcelWriter<MgExcelSheetZip> {

	/** 郵便番号マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetZip sheetObj, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityZip entity : sheetObj.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);
			setCellValue(row, i++, entity.zipCd);
			setCellValue(row, i++, entity.sqno);
			setCellValue(row, i++, entity.adrPrfCd);
			setCellValue(row, i++, entity.adrPrf);
			setCellValue(row, i++, entity.adrPrfKn);
			setCellValue(row, i++, entity.adr1);
			setCellValue(row, i++, entity.adr1Kn);
			setCellValue(row, i++, entity.adr2);
			setCellValue(row, i++, entity.adr2Kn);
			setCellValue(row, i++, entity.dltFg);

			// エラー
			setCellValue(row, i++, entity.errorText);
		}
	}
}
