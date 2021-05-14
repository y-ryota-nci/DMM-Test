package jp.co.dmm.customize.endpoint.mg.mg0130.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookAcc extends MgExcelBook<MgExcelSheetAcc> {

	/** 税入力区分 */
	public Set<String> taxIptTps = MiscUtils.asSet(
		"0",//対象外
		"1" //税込入力
	);

	/** 勘定科目補助区分 */
	public Set<String> accBrkdwnTps = MiscUtils.asSet(
		"0",//補助なし
		"1" //補助あり
	);


	/** 正残区分 */
	public Set<String> dcTps = MiscUtils.asSet(
		"D",//借方
		"C" //貸方
	);


}
