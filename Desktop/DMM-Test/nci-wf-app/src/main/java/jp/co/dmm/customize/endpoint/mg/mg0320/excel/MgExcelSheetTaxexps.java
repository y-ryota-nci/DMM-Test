package jp.co.dmm.customize.endpoint.mg.mg0320.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 消費税関連マスタ情報アップロードの「消費税関連マスタ」シートのマッピング定義
 */
@XlsSheet(name="消費税関連マスタ")
public class MgExcelSheetTaxexps extends MgExcelSheet {
	/** 消費税関連マスタ */
	@XlsHorizontalRecords(tableLabel="消費税関連マスタ")
	public List<MgExcelEntityTaxexps> entityList = new ArrayList<>();
}
