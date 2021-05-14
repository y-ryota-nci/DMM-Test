package jp.co.dmm.customize.endpoint.mg.mg0190.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 部門マスタ情報アップロードの「部門マスタ」シートのマッピング定義
 */
@XlsSheet(name="部門マスタ")
public class MgExcelSheetBumon extends MgExcelSheet {
	/** 部門マスタ */
	@XlsHorizontalRecords(tableLabel="部門マスタ")
	public List<MgExcelEntityBumon> entityList = new ArrayList<>();
}
