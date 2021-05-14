package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;

/**
 * アップロードされた社内レートマスタのEXCELブック
 */
public class MgExcelBookInRto extends MgExcelBook<MgExcelSheetInRto> {

	/** 通貨コード一覧 */
	public Set<String> mnyCds;
}
