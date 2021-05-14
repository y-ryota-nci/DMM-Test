package jp.co.dmm.customize.endpoint.mg.mg0110.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 銀行支店情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBnkbrc extends MgExcelReader<MgExcelBookBnkbrc, MgExcelSheetBnkbrc> {

	/** 銀行支店マスタシートの読み込み */
	@Override
	protected MgExcelSheetBnkbrc readSheet(Sheet sheet) {
	    final MgExcelSheetBnkbrc sheetBnk = new MgExcelSheetBnkbrc();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBnkbrc bnkbrc = new MgExcelEntityBnkbrc();
			int i = 0;

			// 処理区分
			bnkbrc.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(bnkbrc.processType)) {
				bnkbrc.companyCd = companyCd;
				bnkbrc.bnkCd = getCellStringValue(row, i++);
				bnkbrc.bnkbrcCd = getCellStringValue(row, i++);
				bnkbrc.bnkbrcNm = getCellStringValue(row, i++);
				bnkbrc.bnkbrcNmS = getCellStringValue(row, i++);
				bnkbrc.bnkbrcNmKn = getCellStringValue(row, i++);
				// 有効期間（開始）
				if(getCellType(row , i)== CellType.NUMERIC) {
					bnkbrc.vdDtS = getCellDateValue(row, i++);
				}else{
					String strVdDtS = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtS)) {
						bnkbrc.vdDtS = toDate(strVdDtS, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						bnkbrc.strVdDtS = strVdDtS;
					}
				}
				// 有効期間（終了）
				if(getCellType(row , i)== CellType.NUMERIC) {
					bnkbrc.vdDtE = getCellDateValue(row, i++);
					//bnkbrc.strVdDtE = new SimpleDateFormat("yyyy/M/d").format(bnkbrc.vdDtE);
				}else{
					String strVdDtE = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtE)) {
						bnkbrc.vdDtE = toDate(strVdDtE, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						bnkbrc.strVdDtE = strVdDtE;
					}
				}
				bnkbrc.dltFg = getCellStringValue(row, i++);

				sheetBnk.entityList.add(bnkbrc);
			}
		}
		return sheetBnk;
	}

	@Override
	protected String getSheetName() {
		return "銀行支店マスタ";
	}

}
