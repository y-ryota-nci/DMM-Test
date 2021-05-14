package jp.co.dmm.customize.endpoint.mg.mg0250.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 部門関連マスタ情報アップロードの「部門関連マスタ」シートのマッピング定義
 */
@XlsSheet(name="部門関連マスタ")
public class MgExcelSheetBumonExps extends MgExcelSheet {
	/** 部門関連マスタ */
	@XlsHorizontalRecords(tableLabel="部門関連マスタ")
	public List<MgExcelEntityBumonExps> entityList = new ArrayList<>();
}
