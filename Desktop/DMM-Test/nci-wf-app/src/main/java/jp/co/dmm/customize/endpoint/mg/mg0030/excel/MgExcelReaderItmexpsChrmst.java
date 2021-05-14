package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 費目関連マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderItmexpsChrmst extends MgExcelReader<MgExcelBookItmexpsChrmst, MgExcelSheetItmexpsChrmst> {

	/** 費目関連マスタシートの読み込み */
	@Override
	protected MgExcelSheetItmexpsChrmst readSheet(Sheet sheet) {
	    final MgExcelSheetItmexpsChrmst sheetObj = new MgExcelSheetItmexpsChrmst();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityItmexpsChrmst entity = new MgExcelEntityItmexpsChrmst();
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
				entity.orgnzCd = getCellStringValue(row, i++);
				entity.itmexpsCd1 = getCellStringValue(row, i++);
				entity.itmexpsCd2 = getCellStringValue(row, i++);
				entity.jrnCd = getCellStringValue(row, i++);
				entity.accCd = getCellStringValue(row, i++);
				entity.accBrkdwnCd = getCellStringValue(row, i++);
				entity.mngaccCd = getCellStringValue(row, i++);
				entity.mngaccBrkdwnCd = getCellStringValue(row, i++);
				entity.bdgtaccCd = getCellStringValue(row, i++);
				entity.asstTp = getCellStringValue(row, i++);
				entity.taxCd = getCellStringValue(row, i++);
				entity.slpGrpGl = getCellStringValue(row, i++);
				entity.cstTp = getCellStringValue(row, i++);
				entity.taxSbjTp = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "費目関連マスタ";
	}

}
