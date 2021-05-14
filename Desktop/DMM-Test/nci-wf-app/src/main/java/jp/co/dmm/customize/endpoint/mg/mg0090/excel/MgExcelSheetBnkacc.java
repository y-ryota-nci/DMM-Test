package jp.co.dmm.customize.endpoint.mg.mg0090.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 銀行口座情報アップロードの「銀行口座マスタ」シートのマッピング定義
 */
@XlsSheet(name="銀行口座マスタ")
public class MgExcelSheetBnkacc extends MgExcelSheet {
	/** 銀行口座マスタ */
	@XlsHorizontalRecords(tableLabel="銀行口座マスタ")
	public List<MgExcelEntityBnkacc> entityList = new ArrayList<>();
}
