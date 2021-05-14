package jp.co.dmm.customize.endpoint.mg.mg0270.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 予算科目マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBgtItm extends MgExcelReader<MgExcelBookBgtItm, MgExcelSheetBgtItm> {

	/** 予算科目マスタシートの読み込み */
	@Override
	protected MgExcelSheetBgtItm readSheet(Sheet sheet) {
	    final MgExcelSheetBgtItm sheetObj = new MgExcelSheetBgtItm();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBgtItm entity = new MgExcelEntityBgtItm();
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
				entity.bgtItmCd = getCellStringValue(row, i++);
				entity.bgtItmNm = getCellStringValue(row, i++);
				entity.bsPlTp = getCellStringValue(row, i++);
				entity.sortOrder = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "予算科目マスタ";
	}

}
