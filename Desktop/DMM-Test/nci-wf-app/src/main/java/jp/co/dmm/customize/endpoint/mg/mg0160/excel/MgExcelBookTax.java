package jp.co.dmm.customize.endpoint.mg.mg0160.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookTax extends MgExcelBook<MgExcelSheetTax> {

	/** 既存勘定科目一覧 */
	public Set<String> existAccCodes;

	/** 既存勘定科目補助一覧 */
	public Set<String> existAccBrkdwnCodes;

	/** 税処理区分 */
	public Set<String> taxTps = MiscUtils.asSet(
		"0", "1", "2"
	);

	/** 端数処理区分 */
	public Set<String> frcTps = MiscUtils.asSet(
		"1", "2", "3"
	);


	/** 正残区分 */
	public Set<String> dcTps = MiscUtils.asSet(
		"D", "C", "X"
	);


}
