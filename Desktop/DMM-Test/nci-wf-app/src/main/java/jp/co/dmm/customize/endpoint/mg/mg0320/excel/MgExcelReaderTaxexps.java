package jp.co.dmm.customize.endpoint.mg.mg0320.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 消費税関連マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderTaxexps extends MgExcelReader<MgExcelBookTaxexps, MgExcelSheetTaxexps> {

	/** 消費税関連マスタシートの読み込み */
	@Override
	protected MgExcelSheetTaxexps readSheet(Sheet sheet) {
	    final MgExcelSheetTaxexps sheetObj = new MgExcelSheetTaxexps();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityTaxexps entity = new MgExcelEntityTaxexps();
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
				entity.taxKndCd = getCellStringValue(row, i++);
				entity.taxSpc = getCellStringValue(row, i++);
				entity.taxCd = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "消費税関連マスタ";
	}

}
