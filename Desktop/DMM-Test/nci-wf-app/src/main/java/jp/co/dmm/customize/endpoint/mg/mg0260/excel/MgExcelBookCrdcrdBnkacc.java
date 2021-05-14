package jp.co.dmm.customize.endpoint.mg.mg0260.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookCrdcrdBnkacc extends MgExcelBook<MgExcelSheetCrdcrdBnkacc> {

	/** 既存銀行口座一覧 */
	public Set<String> existBnkaccCodes;

	/** 口座引落日
	 * */
	public Set<String> bnkaccChrgDts = MiscUtils.asSet(
		"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
		"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
		"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
	);

}
