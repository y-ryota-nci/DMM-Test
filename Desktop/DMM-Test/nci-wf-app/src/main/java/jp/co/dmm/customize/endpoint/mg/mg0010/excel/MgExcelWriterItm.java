package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelWriter;

/**
 * 品目情報アップロードのEXCELファイルのライター
 */
@ApplicationScoped
public class MgExcelWriterItm extends MgExcelWriter<MgExcelSheetItm> {

	/** 品目マスタシートの書き込み */
	@Override
	public void writeMaster(Sheet sheet, MgExcelSheetItm sheetItm, Map<Integer, CellStyle> styles) {
		final int START = 2;
		int r = START;
		for (MgExcelEntityItm entity : sheetItm.entityList) {
			// 次行を生成＆現行行の書式をコピー
			final Row row = sheet.getRow(r);
			final Row nextRow = sheet.createRow(++r);
			copyRow(row, nextRow, styles, false);

			// 現行行へ書き込み
			int i = 0;
			// 処理区分
			setCellValue(row, i++, entity.processType);
			// 会社コード
			setCellValue(row, i++, entity.companyCd);
			//ORGNZ_CD
			setCellValue(row, i++, entity.orgnzCd);
			//ITM_CD
			setCellValue(row, i++, entity.itmCd);
			//SQNO
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strSqno) ? entity.strSqno : entity.sqno);
			//ITM_NM
			setCellValue(row, i++, entity.itmNm);
			//CTGRY_CD
			setCellValue(row, i++, entity.ctgryCd);
			//STCK_TP
			setCellValue(row, i++, entity.stckTp);
			//SPLR_CD
			setCellValue(row, i++, entity.splrCd);
			//SPLR_NM_KJ
			setCellValue(row, i++, entity.splrNmKj);
			//SPLR_NM_KN
			setCellValue(row, i++, entity.splrNmKn);
			//UNT_CD
			setCellValue(row, i++, entity.untCd);
			//AMT
			setCellValue(row, i++, entity.amt);
			//TAX_CD
			setCellValue(row, i++, entity.taxCd);
			//MAKER_NM
			setCellValue(row, i++, entity.makerNm);
			//MAKER_MDL_NO
			setCellValue(row, i++, entity.makerMdlNo);
			//ITM_VRSN
			setCellValue(row, i++, entity.itmVrsn);
			//PRC_FLD_TP
			setCellValue(row, i++, entity.prcFldTp);
			//VD_DT_S
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strVdDtS) ? entity.strVdDtS : entity.vdDtS);
			//VD_DT_E
			// 2019/11/05 Excelダウンロード時の対応
			// エラー内容出力時はString型、問題ない場合は日付型で出力
			setCellValue(row, i++, StringUtils.isNotEmpty(entity.strVdDtE) ? entity.strVdDtE : entity.vdDtE);
			//ITM_RMK
			setCellValue(row, i++, entity.itmRmk);
			//DLT_FG
			setCellValue(row, i++, entity.dltFg);

			// エラー
			setCellValue(row, i++, entity.errorText);
		}
	}
}
