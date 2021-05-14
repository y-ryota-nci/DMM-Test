package jp.co.dmm.customize.endpoint.md;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

/**
 * 取引先情報アップロードの「関係先マスタ」シートのマッピング定義
 */
@XlsSheet(name="関係先マスタ")
public class MdExcelSheetRltPrt implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 関係先マスタ */
	@XlsHorizontalRecords(tableLabel="関係先マスタ")
	public List<MdExcelRltPrtEntity> rlts = new ArrayList<>();

}
