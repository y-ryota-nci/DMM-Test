package jp.co.dmm.customize.endpoint.mg.mg0170.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 通貨マスタ情報アップロードの「通貨マスタ」シートのマッピング定義
 */
@XlsSheet(name="通貨マスタ")
public class MgExcelSheetMny extends MgExcelSheet {
	/** 通貨マスタ */
	@XlsHorizontalRecords(tableLabel="通貨マスタ")
	public List<MgExcelEntityMny> entityList = new ArrayList<>();
}
