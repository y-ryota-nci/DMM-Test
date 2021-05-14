package jp.co.dmm.customize.endpoint.mg.mg0220.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 支払業務マスタ情報アップロードの「支払業務マスタ」シートのマッピング定義
 */
@XlsSheet(name="支払業務マスタ")
public class MgExcelSheetPayAppl extends MgExcelSheet {
	/** 支払業務マスタ */
	@XlsHorizontalRecords(tableLabel="支払業務マスタ")
	public List<MgExcelEntityPayAppl> entityList = new ArrayList<>();
}
