package jp.co.dmm.customize.endpoint.md;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

/**
 * 取引先情報アップロードの「取引先マスタ」シートのマッピング定義
 */
@XlsSheet(name="取引先マスタ")
public class MdExcelSheetSplr implements Serializable {
	/** 取引先マスタ */
	@XlsHorizontalRecords(tableLabel="取引先マスタ")
	public List<MdExcelSplrEntity> splrs = new ArrayList<>();
}
