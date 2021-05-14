package jp.co.dmm.customize.endpoint.mg.mg0330.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 国マスタアップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderLnd extends MgExcelReader<MgExcelBookLnd, MgExcelSheetLnd> {

	/** 国マスタシートの読み込み */
	@Override
	protected MgExcelSheetLnd readSheet(Sheet sheet) {
		final MgExcelSheetLnd sheetObj = new MgExcelSheetLnd();
		int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityLnd entity = new MgExcelEntityLnd();
			int i = 0;

			// 処理区分
			entity.processType = getCellStringValue(row, i++);

			// 国コードが未入力なら終わり
			String lndCd = getCellStringValue(row, i++);
			if (isEmpty(lndCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(entity.processType)) {
				entity.lndCd = lndCd;
				entity.lndNm = getCellStringValue(row, i++);
				entity.lndCdDjii = getCellStringValue(row, i++);
				entity.sortOrder = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);
				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "国マスタ";
	}

}
