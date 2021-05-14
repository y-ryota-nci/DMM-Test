package jp.co.dmm.customize.endpoint.mg.mg0260.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * クレカ口座マスタ情報アップロードの「クレカ口座マスタ」シートのマッピング定義
 */
@XlsSheet(name="クレカ口座マスタ")
public class MgExcelSheetCrdcrdBnkacc extends MgExcelSheet {
	/** クレカ口座マスタ */
	@XlsHorizontalRecords(tableLabel="クレカ口座マスタ")
	public List<MgExcelEntityCrdcrdBnkacc> entityList = new ArrayList<>();
}
