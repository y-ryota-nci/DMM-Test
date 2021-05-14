package jp.co.dmm.customize.endpoint.mg.mg0240.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 支払サイトマスタ情報アップロードの「支払サイトマスタ」シートのマッピング定義
 */
@XlsSheet(name="支払サイトマスタ")
public class MgExcelSheetPaySite extends MgExcelSheet {
	/** 支払サイトマスタ */
	@XlsHorizontalRecords(tableLabel="支払サイトマスタ")
	public List<MgExcelEntityPaySite> entityList = new ArrayList<>();
}
