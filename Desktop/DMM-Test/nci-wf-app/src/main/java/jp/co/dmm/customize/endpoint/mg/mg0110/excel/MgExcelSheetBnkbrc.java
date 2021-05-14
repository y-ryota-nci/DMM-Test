package jp.co.dmm.customize.endpoint.mg.mg0110.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 銀行支店情報アップロードの「銀行支店マスタ」シートのマッピング定義
 */
@XlsSheet(name="銀行支店マスタ")
public class MgExcelSheetBnkbrc extends MgExcelSheet {
	/** 銀行支店マスタ */
	@XlsHorizontalRecords(tableLabel="銀行支店マスタ")
	public List<MgExcelEntityBnkbrc> entityList = new ArrayList<>();
}
