package jp.co.dmm.customize.endpoint.mg.mg0170.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 通貨マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderMny extends MgExcelReader<MgExcelBookMny, MgExcelSheetMny> {

	/** 通貨マスタシートの読み込み */
	@Override
	protected MgExcelSheetMny readSheet(Sheet sheet) {
	    final MgExcelSheetMny sheetObj = new MgExcelSheetMny();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityMny entity = new MgExcelEntityMny();
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
				entity.mnyCd = getCellStringValue(row, i++);
				entity.mnyNm = getCellStringValue(row, i++);
				entity.mnyMrk = getCellStringValue(row, i++);
				entity.rdxpntGdt = getCellStringValue(row, i++);
				entity.sortOrder = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "通貨マスタ";
	}

}
