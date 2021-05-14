package jp.co.dmm.customize.endpoint.mg.mg0270.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 予算科目マスタ情報アップロードの「予算科目マスタ」シートのマッピング定義
 */
@XlsSheet(name="予算科目マスタ")
public class MgExcelSheetBgtItm extends MgExcelSheet {
	/** 予算科目マスタ */
	@XlsHorizontalRecords(tableLabel="予算科目マスタ")
	public List<MgExcelEntityBgtItm> entityList = new ArrayList<>();
}
