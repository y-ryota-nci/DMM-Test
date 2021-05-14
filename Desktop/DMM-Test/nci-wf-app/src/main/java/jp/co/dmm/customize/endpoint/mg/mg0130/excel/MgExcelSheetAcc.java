package jp.co.dmm.customize.endpoint.mg.mg0130.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 勘定科目情報アップロードの「勘定科目マスタ」シートのマッピング定義
 */
@XlsSheet(name="勘定科目マスタ")
public class MgExcelSheetAcc extends MgExcelSheet {
	@XlsHorizontalRecords(tableLabel="勘定科目マスタ")
	public List<MgExcelEntityAcc> entityList = new ArrayList<>();
}
