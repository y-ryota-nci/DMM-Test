package jp.co.dmm.customize.endpoint.mg.mg0100.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 銀行情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterBnk extends MgExcelWriter<MgExcelSheetBnk> {

	/** 銀行マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetBnk sheetItm, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityBnk bnk : sheetItm.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, bnk.processType);
			// 会社コード
			setCellValue(row, i++, bnk.companyCd);

			setCellValue(row, i++, bnk.bnkCd);
			setCellValue(row, i++, bnk.bnkNm);
			setCellValue(row, i++, bnk.bnkNmS);
			setCellValue(row, i++, bnk.bnkNmKn);
			//VD_DT_S
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(bnk.strVdDtS) ? bnk.strVdDtS : bnk.vdDtS);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(bnk.strVdDtE) ? bnk.strVdDtE : bnk.vdDtE);
			setCellValue(row, i++, bnk.dltFg);

			// エラー
			setCellValue(row, i++, bnk.errorText);
		}
	}
}
