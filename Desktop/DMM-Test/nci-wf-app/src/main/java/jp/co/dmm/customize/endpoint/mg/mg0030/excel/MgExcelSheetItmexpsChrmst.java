package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 費目関連マスタ情報アップロードの「費目関連マスタ」シートのマッピング定義
 */
@XlsSheet(name="費目関連マスタ")
public class MgExcelSheetItmexpsChrmst extends MgExcelSheet {
	/** 費目関連マスタ */
	@XlsHorizontalRecords(tableLabel="費目関連マスタ")
	public List<MgExcelEntityItmexpsChrmst> entityList = new ArrayList<>();
}
