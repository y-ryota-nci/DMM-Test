package jp.co.dmm.customize.endpoint.mg.mg0110.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 銀行支店情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterBnkbrc extends MgExcelWriter<MgExcelSheetBnkbrc> {

	/** 銀行支店マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetBnkbrc sheetItm, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityBnkbrc bnkbrc : sheetItm.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, bnkbrc.processType);
			// 会社コード
			setCellValue(row, i++, bnkbrc.companyCd);

			setCellValue(row, i++, bnkbrc.bnkCd);
			setCellValue(row, i++, bnkbrc.bnkbrcCd);
			setCellValue(row, i++, bnkbrc.bnkbrcNm);
			setCellValue(row, i++, bnkbrc.bnkbrcNmS);
			setCellValue(row, i++, bnkbrc.bnkbrcNmKn);
			//VD_DT_S
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(bnkbrc.strVdDtS) ? bnkbrc.strVdDtS : bnkbrc.vdDtS);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(bnkbrc.strVdDtE) ? bnkbrc.strVdDtE : bnkbrc.vdDtE);
			setCellValue(row, i++, bnkbrc.dltFg);

			// エラー
			setCellValue(row, i++, bnkbrc.errorText);
		}
	}
}
