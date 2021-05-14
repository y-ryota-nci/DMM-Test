package jp.co.dmm.customize.endpoint.mg.mg0290.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 結合フロアマスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBndFlr extends MgExcelReader<MgExcelBookBndFlr, MgExcelSheetBndFlr> {

	/** 結合フロアマスタシートの読み込み */
	@Override
	protected MgExcelSheetBndFlr readSheet(Sheet sheet) {
	    final MgExcelSheetBndFlr sheetObj = new MgExcelSheetBndFlr();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBndFlr entity = new MgExcelEntityBndFlr();
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
				entity.bndFlrCd = getCellStringValue(row, i++);
				entity.bndFlrNm = getCellStringValue(row, i++);
				entity.sortOrder = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "結合フロアマスタ";
	}

}
