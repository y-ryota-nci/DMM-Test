package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 会計カレンダー情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterAccClnd extends MgExcelWriter<MgExcelSheetAccClnd> {

	/** 会計カレンダーマスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetAccClnd sheetObj, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityAccClnd entity : sheetObj.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);
			setCellValue(row, i++, entity.companyCd);
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strClndDt) ? entity.strClndDt : entity.clndDt);
			setCellValue(row, i++, entity.clndDay);
			setCellValue(row, i++, entity.hldayTp);
			setCellValue(row, i++, entity.bnkHldayTp);
			setCellValue(row, i++, entity.stlTpPur);
			setCellValue(row, i++, entity.stlTpFncobl);
			setCellValue(row, i++, entity.stlTpFncaff);
			setCellValue(row, i++, entity.mlClsTm);

			// エラー
			setCellValue(row, i++, entity.errorText);
		}
	}
}
