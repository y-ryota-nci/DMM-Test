package jp.co.dmm.customize.endpoint.mg.mg0130.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 勘定科目情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderAcc extends MgExcelReader<MgExcelBookAcc, MgExcelSheetAcc> {

	/** 勘定科目マスタシートの読み込み */
	@Override
	protected MgExcelSheetAcc readSheet(Sheet sheet) {
	    final MgExcelSheetAcc excelSheet = new MgExcelSheetAcc();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityAcc entity = new MgExcelEntityAcc();
			int i = 0;

			// 処理区分
			entity.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(entity.processType)) {
				entity.companyCd = companyCd;
				entity.accCd = getCellStringValue(row, i++);
				entity.strSqno = getCellStringValue(row, i++);
				entity.sqno = NumberUtils.isDigits(entity.strSqno) ? NumberUtils.toInt(entity.strSqno) : null;
				entity.accNm = getCellStringValue(row, i++);
				entity.accNmS = getCellStringValue(row, i++);
				entity.dcTp = getCellStringValue(row, i++);
				entity.accBrkDwnTp = getCellStringValue(row, i++);
				entity.taxCdSs = getCellStringValue(row, i++);
				entity.taxIptTp = getCellStringValue(row, i++);
				// 有効期間（開始）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					entity.vdDtS = getCellDateValue(row, i++);
				}else{
					String strVdDtS = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtS)) {
						entity.vdDtS = toDate(strVdDtS, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						entity.strVdDtS = strVdDtS;
					}
				}
				// 有効期間（終了）
				// 2019/11/05 Excelアップロードバグの対応
				if(getCellType(row , i) == CellType.NUMERIC) {
					entity.vdDtE = getCellDateValue(row, i++);
				}else{
					String strVdDtE = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strVdDtE)) {
						entity.vdDtE = toDate(strVdDtE, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						entity.strVdDtE = strVdDtE;
					}
				}
				entity.dltFg = getCellStringValue(row, i++);

				entity.cmSqno = entity.sqno;
				entity.cmVdDtS = entity.vdDtS;
				entity.cmVdDtE = entity.vdDtE;

				excelSheet.entityList.add(entity);
			}
		}
		return excelSheet;
	}

	@Override
	protected String getSheetName() {
		return "勘定科目マスタ";
	}

}
