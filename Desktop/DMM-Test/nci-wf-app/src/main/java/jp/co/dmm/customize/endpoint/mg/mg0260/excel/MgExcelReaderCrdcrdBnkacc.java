package jp.co.dmm.customize.endpoint.mg.mg0260.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * クレカ口座マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderCrdcrdBnkacc extends MgExcelReader<MgExcelBookCrdcrdBnkacc, MgExcelSheetCrdcrdBnkacc> {

	/** クレカ口座マスタシートの読み込み */
	@Override
	protected MgExcelSheetCrdcrdBnkacc readSheet(Sheet sheet) {
	    final MgExcelSheetCrdcrdBnkacc sheetObj = new MgExcelSheetCrdcrdBnkacc();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityCrdcrdBnkacc entity = new MgExcelEntityCrdcrdBnkacc();
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
				entity.splrCd = getCellStringValue(row, i++);
				entity.usrCd = getCellStringValue(row, i++);
				entity.crdCompanyNm = getCellStringValue(row, i++);
				entity.bnkaccCd = getCellStringValue(row, i++);
				entity.bnkaccChrgDt = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "クレカ口座マスタ";
	}

}
