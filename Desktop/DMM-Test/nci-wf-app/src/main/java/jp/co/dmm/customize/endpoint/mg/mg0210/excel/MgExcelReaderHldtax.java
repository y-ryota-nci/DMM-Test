package jp.co.dmm.customize.endpoint.mg.mg0210.excel;

import java.math.BigDecimal;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 源泉税マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderHldtax extends MgExcelReader<MgExcelBookHldtax, MgExcelSheetHldtax> {

	/** 源泉税マスタシートの読み込み */
	@Override
	protected MgExcelSheetHldtax readSheet(Sheet sheet) {
	    final MgExcelSheetHldtax sheetObj = new MgExcelSheetHldtax();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityHldtax entity = new MgExcelEntityHldtax();
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
				entity.hldtaxTp = getCellStringValue(row, i++);
				entity.hldtaxNm = getCellStringValue(row, i++);

				String hldtaxRto1 = getCellStringValue(row, i++);
				if (NumberUtils.isNumber(hldtaxRto1)) {
					entity.hldtaxRto1 = new BigDecimal(hldtaxRto1);
				}

				String hldtaxRto2 = getCellStringValue(row, i++);
				if (NumberUtils.isNumber(hldtaxRto2)) {
					entity.hldtaxRto2 = new BigDecimal(hldtaxRto2);
				}

				entity.accCd = getCellStringValue(row, i++);
				entity.accBrkdwnCd = getCellStringValue(row, i++);

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

				entity.sortOrder = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "源泉税マスタ";
	}

}
