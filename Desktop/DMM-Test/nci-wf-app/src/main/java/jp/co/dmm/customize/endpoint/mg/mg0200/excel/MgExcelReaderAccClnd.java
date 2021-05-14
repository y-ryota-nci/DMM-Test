package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 会計カレンダーマスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderAccClnd extends MgExcelReader<MgExcelBookAccClnd, MgExcelSheetAccClnd> {

	/** 会計カレンダーマスタシートの読み込み */
	@Override
	protected MgExcelSheetAccClnd readSheet(Sheet sheet) {
	    final MgExcelSheetAccClnd sheetObj = new MgExcelSheetAccClnd();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityAccClnd entity = new MgExcelEntityAccClnd();
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
				// 日付
				if(getCellType(row , i)== CellType.NUMERIC) {
					entity.clndDt = getCellDateValue(row, i++);
				}else{
					entity.strClndDt = getCellStringValue(row, i++);
					String strClndDt = getCellStringValue(row, i++);
					if (ValidatorUtils.isYMD(strClndDt)) {
						entity.clndDt = toDate(strClndDt, "yyyy/MM/dd");
					} else {
						// エラー出力時は文字列で取得する
						entity.strClndDt = strClndDt;
					}
				}
				entity.clndDay = getCellStringValue(row, i++);
				entity.hldayTp = getCellStringValue(row, i++);
				entity.bnkHldayTp = getCellStringValue(row, i++);
				entity.stlTpPur = getCellStringValue(row, i++);
				entity.stlTpFncobl = getCellStringValue(row, i++);
				entity.stlTpFncaff = getCellStringValue(row, i++);
				entity.mlClsTm = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "会計カレンダーマスタ";
	}

}
