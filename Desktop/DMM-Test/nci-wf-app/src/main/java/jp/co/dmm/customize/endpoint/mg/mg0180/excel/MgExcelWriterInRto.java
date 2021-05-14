package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 社内レートマスタアップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterInRto extends MgExcelWriter<MgExcelSheetInRto> {

	/** 社内レートマスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetInRto sheetObj, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityInRto entity : sheetObj.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);
			setCellValue(row, i++, entity.companyCd);
			setCellValue(row, i++, entity.mnyCd);
			//SQNO
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strSqno) ? entity.strSqno : entity.sqno);
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strInRto) ? entity.strInRto : entity.inRto);
			setCellValue(row, i++, entity.rtoTp);
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
