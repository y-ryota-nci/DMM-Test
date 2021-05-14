package jp.co.dmm.customize.endpoint.md;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gh.mygreen.xlsmapper.annotation.XlsHorizontalRecords;
import com.gh.mygreen.xlsmapper.annotation.XlsSheet;

/**
 * 取引先情報アップロードの「反社情報」シートのマッピング定義
 */
@XlsSheet(name="反社情報")
public class MdExcelSheetOrgCrm implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 反社情報 */
	@XlsHorizontalRecords(tableLabel="反社情報")
	public List<MdExcelOrgCrmEntity> orgs = new ArrayList<>();

}
