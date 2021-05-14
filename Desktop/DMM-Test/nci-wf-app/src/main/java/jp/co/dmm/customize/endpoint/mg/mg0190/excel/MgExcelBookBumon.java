package jp.co.dmm.customize.endpoint.mg.mg0190.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookBumon extends MgExcelBook<MgExcelSheetBumon> {
	/** 事業一覧 */
	public Set<String> existEntrpTpCds;

	/** タブコード一覧 */
	public Set<String> existTabCds;

	/** 地域コード一覧 */
	public Set<String> existAreaCds;

	/** 分類コード */
	public Set<String> tpCds = MiscUtils.asSet(
		"0", // 共通
		"1", // 一般
		"2", // R18
		"3", // セル
		"4", // レンタル
		"5", // 配信
		"8", // セルレンタル共通
		"9"  // その他
	);

	/** サイトコード */
	public Set<String> siteCds = MiscUtils.asSet(
		"0", // 共通
		"1", // 自社
		"2", // 総販
		"3", // 他社
		"9"  // その他
	);

	/** 消費税種類コード
	 * 1: 課税仕入
	 * 2: 非課税仕入
	 * 3: 共通仕入
	 * */
	public Set<String> taxKndCds = MiscUtils.asSet("1", "2", "3");
}
