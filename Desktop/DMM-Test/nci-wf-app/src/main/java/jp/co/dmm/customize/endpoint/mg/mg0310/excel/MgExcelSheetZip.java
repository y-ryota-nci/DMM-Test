package jp.co.dmm.customize.endpoint.mg.mg0310.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 郵便番号マスタ情報アップロードの「郵便番号マスタ」シートのマッピング定義
 */
@XlsSheet(name="郵便番号マスタ")
public class MgExcelSheetZip extends MgExcelSheet {
	/** 郵便番号マスタ */
	@XlsHorizontalRecords(tableLabel="郵便番号マスタ")
	public List<MgExcelEntityZip> entityList = new ArrayList<>();
}
