package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookItmexpsChrmst extends MgExcelBook<MgExcelSheetItmexpsChrmst> {
	/** 費目コード一覧 */
	public Set<String> existItmexpsCds;

	/** 仕訳コード一覧 */
	public Set<String> existJrnCds;

	/** 勘定科目コード一覧 */
	public Set<String> existAccCds;

	/** 勘定科目補助コード一覧 */
	public Set<String> existAccBrkdwnCds;

	/** 予算科目コード一覧 */
	public Set<String> existBdgtaccCds;

	/** 資産区分
	 * 0:対象外
	 * 1:資産
	 * */
	public Set<String> asstTps = MiscUtils.asSet("0", "1");

	/** 経費区分
	 * 1:通常
	 * 2:経費
	 * */
	public Set<String> cstTps = MiscUtils.asSet("1", "2");

	/** 課税対象区分
	 * 1:対象
	 * 2:対象外
	 * 3:対象 or 対象外
	 * */
	public Set<String> taxSbjTps = MiscUtils.asSet("1", "2", "3");
}
