package jp.co.dmm.customize.endpoint.mg.mg0190.excel;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelReader;

/**
 * 部門マスタ情報アップロードのEXCELファイルのリーダー
 */
@ApplicationScoped
public class MgExcelReaderBumon extends MgExcelReader<MgExcelBookBumon, MgExcelSheetBumon> {

	/** 部門マスタシートの読み込み */
	@Override
	protected MgExcelSheetBumon readSheet(Sheet sheet) {
	    final MgExcelSheetBumon sheetObj = new MgExcelSheetBumon();
	    int r = 0;
		for (Row row : sheet) {
			if (r++ < 2)
				continue;
			if (row == null)
				continue;

			final MgExcelEntityBumon entity = new MgExcelEntityBumon();
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
				entity.bumonNm = getCellStringValue(row, i++);
				entity.entrpTpCd = entity.bumonCd.length() >= 3 ? entity.bumonCd.substring(0, 3) : "";
				entity.entrpCd = entity.bumonCd.length() >= 6 ? entity.bumonCd.substring(3, 6) : "";
				entity.tabCd = entity.bumonCd.length() >= 9 ? entity.bumonCd.substring(6, 9) : "";
				entity.siteCd = entity.bumonCd.length() >= 10 ? entity.bumonCd.substring(9, 10) : "";
				entity.tpCd = entity.bumonCd.length() >= 11 ? entity.bumonCd.substring(10, 11) : "";
				entity.areaCd = entity.bumonCd.length() >= 14 ? entity.bumonCd.substring(11, 14) : "";

				entity.taxKndCd = getCellStringValue(row, i++);
				entity.dltFg = getCellStringValue(row, i++);

				sheetObj.entityList.add(entity);
			}
		}
		return sheetObj;
	}

	@Override
	protected String getSheetName() {
		return "部門マスタ";
	}

}
