package jp.co.dmm.customize.endpoint.mg.mg0250.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 部門関連マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBumonExps extends MgExcelReader<MgExcelBookBumonExps, MgExcelSheetBumonExps> {

	/** 部門関連マスタシートの読み込み */
	@Override
	protected MgExcelSheetBumonExps readSheet(Sheet sheet) {
	    final MgExcelSheetBumonExps sheetObj = new MgExcelSheetBumonExps();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBumonExps entity = new MgExcelEntityBumonExps();
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
				entity.bumonCd = getCellStringValue(row, i++);
				entity.orgnzCd = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "部門関連マスタ";
	}

}
