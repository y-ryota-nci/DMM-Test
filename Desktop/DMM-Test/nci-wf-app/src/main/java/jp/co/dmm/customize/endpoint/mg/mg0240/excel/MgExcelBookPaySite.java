package jp.co.dmm.customize.endpoint.mg.mg0240.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookPaySite extends MgExcelBook<MgExcelSheetPaySite> {
	/** 支払サイト（月）
	 * 1:翌月
	 * 2:翌々月
	 * 3:前月
	 * 4:当月
	 * 9:その他
	 * */
	public Set<String> paySieMonths = MiscUtils.asSet("1", "2", "3", "4", "9");

	/** 支払サイト（日）
	 * */
	public Set<String> paySieDayNumbers = MiscUtils.asSet("5", "10", "15", "20", "25", "88", "99");
}
