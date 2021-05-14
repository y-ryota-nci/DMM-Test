package jp.co.dmm.customize.endpoint.mg.mg0020.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 費目情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderItmexps extends MgExcelReader<MgExcelBookItmexps, MgExcelSheetItmexps> {

	/** 費目マスタシートの読み込み */
	@Override
	protected MgExcelSheetItmexps readSheet(Sheet sheet) {
	    final MgExcelSheetItmexps sheetItm = new MgExcelSheetItmexps();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityItmexps itm = new MgExcelEntityItmexps();
			int i = 0;

			// 処理区分
			itm.processType = getCellStringValue(row, i++);

			// 企業コードが未入力なら終わり
			String companyCd = getCellStringValue(row, i++);
			if (isEmpty(companyCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(itm.processType)) {
				itm.companyCd = companyCd;
				itm.itmexpsCd = getCellStringValue(row, i++);
				itm.itmexpsNm = getCellStringValue(row, i++);
				itm.itmexpsNmS = getCellStringValue(row, i++);
				itm.itmexpsLevel = getCellStringValue(row, i++);
				itm.dltFg = getCellStringValue(row, i++);

				sheetItm.entityList.add(itm);
			}
		}
		return sheetItm;
	}

	@Override
	protected String getSheetName() {
		return "費目マスタ";
	}

}
