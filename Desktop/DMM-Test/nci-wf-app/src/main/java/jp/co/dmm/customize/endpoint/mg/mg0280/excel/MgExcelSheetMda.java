package jp.co.dmm.customize.endpoint.mg.mg0280.excel;

import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelSheet;

/**
 * メディアマスタ情報アップロードの「メディアマスタ」シートのマッピング定義
 */
@XlsSheet(name="メディアマスタ")
public class MgExcelSheetMda extends MgExcelSheet {
	/** メディアマスタ */
	@XlsHorizontalRecords(tableLabel="メディアマスタ")
	public List<MgExcelEntityMda> entityList = new ArrayList<>();
}
