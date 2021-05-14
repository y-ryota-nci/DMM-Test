package jp.co.dmm.customize.endpoint.mg.mg0160.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 消費税情報アップロードの「消費税マスタ」シートのマッピング定義
 */
@XlsSheet(name="消費税マスタ")
public class MgExcelSheetTax extends MgExcelSheet {
	/** 消費税マスタ */
	@XlsHorizontalRecords(tableLabel="消費税マスタ")
	public List<MgExcelEntityTax> entityList = new ArrayList<>();
}
