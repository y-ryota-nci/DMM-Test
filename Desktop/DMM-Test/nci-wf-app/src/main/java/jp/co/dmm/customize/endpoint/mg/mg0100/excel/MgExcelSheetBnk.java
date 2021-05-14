package jp.co.dmm.customize.endpoint.mg.mg0100.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 銀行情報アップロードの「銀行マスタ」シートのマッピング定義
 */
@XlsSheet(name="銀行マスタ")
public class MgExcelSheetBnk extends MgExcelSheet {
	/** 銀行マスタ */
	@XlsHorizontalRecords(tableLabel="銀行マスタ")
	public List<MgExcelEntityBnk> entityList = new ArrayList<>();
}
