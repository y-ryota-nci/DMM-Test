package jp.co.dmm.customize.endpoint.mg.mg0320.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookTaxexps extends MgExcelBook<MgExcelSheetTaxexps> {

	/** 消費税種類コード
	 * 1: 課税仕入
	 * 2: 非課税仕入
	 * 3: 共通仕入
	 * */
	public Set<String> taxKndCds = MiscUtils.asSet("1", "2", "3");

	/** 消費税種別
	 * 1: 10%
	 * 2: 軽減8%
	 * 3: 旧税率8%
	 * */
	public Set<String> taxSpcs = MiscUtils.asSet("1", "2", "3");
}
