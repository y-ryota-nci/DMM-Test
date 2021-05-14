package jp.co.dmm.customize.endpoint.mg.mg0160.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 消費税情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterTax extends MgExcelWriter<MgExcelSheetTax> {

	/** 消費税マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetTax excelSheet, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityTax entity : excelSheet.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);

			setCellValue(row, i++, entity.companyCd);
			setCellValue(row, i++, entity.taxCd);
			//SQNO
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strSqno) ? entity.strSqno : entity.sqno);

			setCellValue(row, i++, entity.taxNm);
			setCellValue(row, i++, entity.taxNmS);
			setCellValue(row, i++, entity.taxRto);
			setCellValue(row, i++, entity.taxTp);
			setCellValue(row, i++, entity.taxCdSs);

			setCellValue(row, i++, entity.frcUnt);
			setCellValue(row, i++, entity.frcTp);
			setCellValue(row, i++, entity.accCd);
			setCellValue(row, i++, entity.accBrkdwnCd);

			setCellValue(row, i++, entity.dcTp);

			//VD_DT_S
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strVdDtS) ? entity.strVdDtS : entity.vdDtS);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strVdDtE) ? entity.strVdDtE : entity.vdDtE);

			setCellValue(row, i++, entity.dltFg);

			// エラー
			setCellValue(row, i++, entity.errorText);
		}
	}
}
