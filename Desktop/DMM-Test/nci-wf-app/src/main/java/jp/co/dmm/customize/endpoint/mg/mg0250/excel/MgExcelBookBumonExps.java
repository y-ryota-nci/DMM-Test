package jp.co.dmm.customize.endpoint.mg.mg0250.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookBumonExps extends MgExcelBook<MgExcelSheetBumonExps> {
	/** 部門コード一覧 */
	public Set<String> existBumonCds;
}
