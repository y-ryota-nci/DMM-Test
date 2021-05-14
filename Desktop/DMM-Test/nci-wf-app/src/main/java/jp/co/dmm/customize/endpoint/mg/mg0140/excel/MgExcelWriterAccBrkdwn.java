package jp.co.dmm.customize.endpoint.mg.mg0140.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 勘定科目補助情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterAccBrkdwn extends MgExcelWriter<MgExcelSheetAccBrkdwn> {

	/** 勘定科目補助マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetAccBrkdwn excelSheet, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityAccBrkdwn entity : excelSheet.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);

			setCellValue(row, i++, entity.companyCd);
			setCellValue(row, i++, entity.accCd);
			setCellValue(row, i++, entity.accBrkdwnCd);
			//SQNO
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strSqno) ? entity.strSqno : entity.sqno);
			setCellValue(row, i++, entity.accBrkdwnNm);
			setCellValue(row, i++, entity.accBrkdwnNmS);
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
