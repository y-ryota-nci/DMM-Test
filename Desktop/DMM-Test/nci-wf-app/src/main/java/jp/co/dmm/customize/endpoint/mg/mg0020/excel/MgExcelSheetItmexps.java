package jp.co.dmm.customize.endpoint.mg.mg0020.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 費目情報アップロードの「費目マスタ」シートのマッピング定義
 */
@XlsSheet(name="費目マスタ")
public class MgExcelSheetItmexps extends MgExcelSheet {
	/** 費目マスタ */
	@XlsHorizontalRecords(tableLabel="費目マスタ")
	public List<MgExcelEntityItmexps> entityList = new ArrayList<>();
}
