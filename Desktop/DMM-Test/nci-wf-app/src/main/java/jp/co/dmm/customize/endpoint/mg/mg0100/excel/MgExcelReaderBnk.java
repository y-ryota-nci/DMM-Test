package jp.co.dmm.customize.endpoint.mg.mg0100.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 銀行情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBnk extends MgExcelReader<MgExcelBookBnk, MgExcelSheetBnk> {

	/** 銀行マスタシートの読み込み */
	@Override
	protected MgExcelSheetBnk readSheet(Sheet sheet) {
	    final MgExcelSheetBnk sheetBnk = new MgExcelSheetBnk();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBnk bnk = new MgExcelEntityBnk();
			int i = 0;

			// 処理区分
			bnk.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(bnk.processType)) {
				bnk.companyCd = companyCd;
				bnk.bnkCd = getCellStringValue(row, i++);
				bnk.bnkNm = getCellStringValue(row, i++);
				bnk.bnkNmS = getCellStringValue(row, i++);
				bnk.bnkNmKn = getCellStringValue(row, i++);
				// 有効期間（開始）
				if(getCellType(row , i)== CellType.NUMERIC) {
					bnk.vdDtS = getCellDateValue(row, i++);
					//bnk.strVdDtS = new SimpleDateFormat("yyyy/M/d").format(bnk.vdDtS);
				}else{
					String strVdDtS = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtS)) {
						bnk.vdDtS = toDate(strVdDtS, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						bnk.strVdDtS = strVdDtS;
					}
				}
				// 有効期間（終了）
				if(getCellType(row , i)== CellType.NUMERIC) {
					bnk.vdDtE = getCellDateValue(row, i++);
					//bnk.strVdDtE = new SimpleDateFormat("yyyy/M/d").format(bnk.vdDtE);
				}else{
					String strVdDtE = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtE)) {
						bnk.vdDtE = toDate(strVdDtE, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						bnk.strVdDtE = strVdDtE;
					}
				}
				bnk.dltFg = getCellStringValue(row, i++);

				sheetBnk.entityList.add(bnk);
			}
		}
		return sheetBnk;
	}

	@Override
	protected String getSheetName() {
		return "銀行マスタ";
	}

}
