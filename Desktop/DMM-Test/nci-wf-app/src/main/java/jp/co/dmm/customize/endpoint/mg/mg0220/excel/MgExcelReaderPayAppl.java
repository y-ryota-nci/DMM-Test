package jp.co.dmm.customize.endpoint.mg.mg0220.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 支払業務マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderPayAppl extends MgExcelReader<MgExcelBookPayAppl, MgExcelSheetPayAppl> {

	/** 支払業務マスタシートの読み込み */
	@Override
	protected MgExcelSheetPayAppl readSheet(Sheet sheet) {
	    final MgExcelSheetPayAppl sheetObj = new MgExcelSheetPayAppl();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityPayAppl entity = new MgExcelEntityPayAppl();
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
				entity.payApplCd = getCellStringValue(row, i++);
				entity.payApplNm = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "支払業務マスタ";
	}

}
