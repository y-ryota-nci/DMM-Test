package jp.co.dmm.customize.endpoint.mg.mg0210.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookHldtax extends MgExcelBook<MgExcelSheetHldtax> {
	/** 既存勘定科目一覧 */
	public Set<String> existAccCodes;

	/** 既存勘定科目補助一覧 */
	public Set<String> existAccBrkdwnCodes;
}
