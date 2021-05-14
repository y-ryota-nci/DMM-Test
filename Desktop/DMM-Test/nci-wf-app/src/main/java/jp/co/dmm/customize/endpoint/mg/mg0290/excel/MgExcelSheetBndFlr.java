package jp.co.dmm.customize.endpoint.mg.mg0290.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 結合フロアマスタ情報アップロードの「結合フロアマスタ」シートのマッピング定義
 */
@XlsSheet(name="結合フロアマスタ")
public class MgExcelSheetBndFlr extends MgExcelSheet {
	/** 結合フロアマスタ */
	@XlsHorizontalRecords(tableLabel="結合フロアマスタ")
	public List<MgExcelEntityBndFlr> entityList = new ArrayList<>();
}
