package jp.co.dmm.customize.endpoint.md;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

/**
 * 取引先情報アップロードの「振込先マスタ」シートのマッピング定義
 */
@XlsSheet(name="振込先マスタ")
public class MdExcelSheetPayeeBnkacc implements Serializable {
	/** 振込先マスタ */
	@XlsHorizontalRecords(tableLabel="振込先マスタ")
	public List<MdExcelSplrAccEntity> accs = new ArrayList<>();
}
