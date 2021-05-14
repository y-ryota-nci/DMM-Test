package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 会計カレンダー情報アップロードの「会計カレンダーマスタ」シートのマッピング定義
 */
@XlsSheet(name="会計カレンダーマスタ")
public class MgExcelSheetAccClnd extends MgExcelSheet {
	/** 会計カレンダーマスタ */
	@XlsHorizontalRecords(tableLabel="会計カレンダーマスタ")
	public List<MgExcelEntityAccClnd> entityList = new ArrayList<>();
}
