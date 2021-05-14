package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 費目関連情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterItmexpsChrmst extends MgExcelWriter<MgExcelSheetItmexpsChrmst> {

	/** 費目関連マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetItmexpsChrmst sheetObj, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityItmexpsChrmst entity : sheetObj.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);

			setCellValue(row, i++, entity.companyCd);
			setCellValue(row, i++, entity.orgnzCd);
			setCellValue(row, i++, entity.itmexpsCd1);
			setCellValue(row, i++, entity.itmexpsCd2);
			setCellValue(row, i++, entity.jrnCd);
			setCellValue(row, i++, entity.accCd);
			setCellValue(row, i++, entity.accBrkdwnCd);
			setCellValue(row, i++, entity.mngaccCd);
			setCellValue(row, i++, entity.mngaccBrkdwnCd);
			setCellValue(row, i++, entity.bdgtaccCd);
			setCellValue(row, i++, entity.asstTp);
			setCellValue(row, i++, entity.taxCd);
			setCellValue(row, i++, entity.slpGrpGl);
			setCellValue(row, i++, entity.cstTp);
			setCellValue(row, i++, entity.taxSbjTp);
			setCellValue(row, i++, entity.dltFg);

			// エラー
			setCellValue(row, i++, entity.errorText);
		}
	}
}
