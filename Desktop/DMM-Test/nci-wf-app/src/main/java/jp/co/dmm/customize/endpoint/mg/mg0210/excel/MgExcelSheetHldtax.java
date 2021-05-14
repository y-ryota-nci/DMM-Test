package jp.co.dmm.customize.endpoint.mg.mg0210.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 源泉税マスタ情報アップロードの「源泉税マスタ」シートのマッピング定義
 */
@XlsSheet(name="源泉税マスタ")
public class MgExcelSheetHldtax extends MgExcelSheet {
	/** 源泉税マスタ */
	@XlsHorizontalRecords(tableLabel="源泉税マスタ")
	public List<MgExcelEntityHldtax> entityList = new ArrayList<>();
}
