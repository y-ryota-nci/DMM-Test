package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 取引先情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderItm extends MgExcelReader<MgExcelBookItm, MgExcelSheetItm> {

	/** 取引先マスタシートの読み込み */
	@Override
	protected MgExcelSheetItm readSheet(Sheet sheet) {
	    final MgExcelSheetItm sheetItm = new MgExcelSheetItm();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityItm entity = new MgExcelEntityItm();
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
				entity.orgnzCd = getCellStringValue(row, i++);
				entity.itmCd = getCellStringValue(row, i++);
				entity.strSqno = getCellStringValue(row, i++);
				entity.sqno = NumberUtils.isDigits(entity.strSqno) ? NumberUtils.toInt(entity.strSqno) : null;
				entity.itmNm = getCellStringValue(row, i++);
				entity.ctgryCd = getCellStringValue(row, i++);
				entity.stckTp = getCellStringValue(row, i++);
				entity.splrCd = getCellStringValue(row, i++);
				entity.splrNmKj = getCellStringValue(row, i++);
				entity.splrNmKn = getCellStringValue(row, i++);
				entity.untCd = getCellStringValue(row, i++);
				entity.amt = getCellStringValue(row, i++);
				entity.taxCd = getCellStringValue(row, i++);
				entity.makerNm = getCellStringValue(row, i++);
				entity.makerMdlNo = getCellStringValue(row, i++);
				entity.itmVrsn = getCellStringValue(row, i++);
				entity.prcFldTp = getCellStringValue(row, i++);
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
				entity.itmRmk = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				entity.cmSqno = entity.sqno;
				entity.cmVdDtS = entity.vdDtS;
				entity.cmVdDtE = entity.vdDtE;

				sheetItm.entityList.add(entity);
			}
		}
		return sheetItm;
	}

	@Override
	protected String getSheetName() {
		return "品目マスタ";
	}

}
