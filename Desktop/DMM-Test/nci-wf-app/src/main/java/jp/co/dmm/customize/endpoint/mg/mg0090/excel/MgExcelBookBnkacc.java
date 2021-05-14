package jp.co.dmm.customize.endpoint.mg.mg0090.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookBnkacc extends MgExcelBook<MgExcelSheetBnkacc> {

	/** 既存銀行口座一覧 */
	public Set<String> existBnkaccCodes;

	/** 銀行口座種別
	 * 01:普通預金
	 * 02:当座預金
	 * 03:貯蓄預金
	 * 04:通知預金
	 * 05:その他
	 * */
	public Set<String> bnkaccTps = MiscUtils.asSet("01", "02", "03", "04", "05");

}
