package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookAccClnd extends MgExcelBook<MgExcelSheetAccClnd> {

	/** 曜日
	 * */
	public Set<String> clndDayNos = MiscUtils.asSet(
		"1", "2", "3", "4", "5", "6", "7"
	);
	public Set<String> clndDayStrings = MiscUtils.asSet(
		"日", "月", "火", "水", "木", "金", "土"
	);

	/**
	 * 休日区分
	 */
	public Set<String> hldayTps = MiscUtils.asSet(
		"0", "1"
	);

	/**
	 * 決済区分
	 */
	public Set<String> stlTps = MiscUtils.asSet(
		"1", "2"
	);

}
