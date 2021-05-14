package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 品目情報アップロードの「品目マスタ」シートのマッピング定義
 */
@XlsSheet(name="品目マスタ")
public class MgExcelSheetItm extends MgExcelSheet {
	/** 品目マスタ */
	@XlsHorizontalRecords(tableLabel="品目マスタ")
	public List<MgExcelEntityItm> entityList = new ArrayList<>();
}
