package jp.co.dmm.customize.endpoint.mg.mg0140.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * 勘定科目補助情報アップロードの「勘定科目補助マスタ」シートのマッピング定義
 */
@XlsSheet(name="勘定科目補助マスタ")
public class MgExcelSheetAccBrkdwn extends MgExcelSheet {
	@XlsHorizontalRecords(tableLabel="勘定科目補助マスタ")
	public List<MgExcelEntityAccBrkdwn> entityList = new ArrayList<>();
}
