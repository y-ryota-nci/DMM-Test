package jp.co.dmm.customize.endpoint.mg.mg0310.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 郵便番号マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderZip extends MgExcelReader<MgExcelBookZip, MgExcelSheetZip> {

	/** 郵便番号マスタシートの読み込み */
	@Override
	protected MgExcelSheetZip readSheet(Sheet sheet) {
	    final MgExcelSheetZip sheetObj = new MgExcelSheetZip();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityZip entity = new MgExcelEntityZip();
			int i = 0;

			// 処理区分
			entity.processType = getCellStringValue(row, i++);

			// 郵便番号が未入力なら終わり
			String zipCd = getCellStringValue(row, i++);
			if (isEmpty(zipCd)) {
				break;
			}

			if (StringUtils.isNotEmpty(entity.processType)) {
				entity.zipCd = zipCd;
				entity.sqno = getCellStringValue(row, i++);
				entity.adrPrfCd = getCellStringValue(row, i++);
				entity.adrPrf = getCellStringValue(row, i++);
				entity.adrPrfKn = getCellStringValue(row, i++);
				entity.adr1 = getCellStringValue(row, i++);
				entity.adr1Kn = getCellStringValue(row, i++);
				entity.adr2 = getCellStringValue(row, i++);
				entity.adr2Kn = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "郵便番号マスタ";
	}

}
