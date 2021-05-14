package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 社内レートマスタアップロードの「社内レートマスタ」シートのマッピング定義
 */
@XlsSheet(name="社内レートマスタ")
public class MgExcelSheetInRto extends MgExcelSheet {
	/** 社内レートマスタ */
	@XlsHorizontalRecords(tableLabel="社内レートマスタ")
	public List<MgExcelEntityInRto> entityList = new ArrayList<>();
}
