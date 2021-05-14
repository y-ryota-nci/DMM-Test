package jp.co.dmm.customize.endpoint.mg.mg0240.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 支払サイト情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterPaySite extends MgExcelWriter<MgExcelSheetPaySite> {

	/** 支払サイトマスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetPaySite sheetObj, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityPaySite entity : sheetObj.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);
			setCellValue(row, i++, entity.companyCd);
			setCellValue(row, i++, entity.paySiteCd);
			setCellValue(row, i++, entity.paySiteNm);
			setCellValue(row, i++, entity.paySiteM);
			setCellValue(row, i++, entity.paySiteN);
			setCellValue(row, i++, entity.sortOrder);
			setCellValue(row, i++, entity.dltFg);

			// エラー
			setCellValue(row, i++, entity.errorText);
		}
	}
}
