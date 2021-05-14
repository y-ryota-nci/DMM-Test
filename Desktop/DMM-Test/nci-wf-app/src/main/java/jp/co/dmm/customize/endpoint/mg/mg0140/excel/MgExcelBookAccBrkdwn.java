package jp.co.dmm.customize.endpoint.mg.mg0140.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookAccBrkdwn extends MgExcelBook<MgExcelSheetAccBrkdwn> {

	/** 既存勘定科目一覧 */
	public Set<String> existAccCodes;

}
