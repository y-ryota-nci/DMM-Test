package jp.co.dmm.customize.endpoint.mg.mg0240.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 支払サイトマスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderPaySite extends MgExcelReader<MgExcelBookPaySite, MgExcelSheetPaySite> {

	/** 支払サイトマスタシートの読み込み */
	@Override
	protected MgExcelSheetPaySite readSheet(Sheet sheet) {
	    final MgExcelSheetPaySite sheetObj = new MgExcelSheetPaySite();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityPaySite entity = new MgExcelEntityPaySite();
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
				entity.paySiteCd = getCellStringValue(row, i++);
				entity.paySiteNm = getCellStringValue(row, i++);
				entity.paySiteM = getCellStringValue(row, i++);
				entity.paySiteN = getCellStringValue(row, i++);
				entity.sortOrder = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "支払サイトマスタ";
	}

}
