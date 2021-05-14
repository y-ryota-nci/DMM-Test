package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import java.util.Set;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelBook;

/**
 * アップロードされたプロファイル情報のEXCELブック
 */
public class MgExcelBookItm extends MgExcelBook<MgExcelSheetItm> {

	/** 既存品目一覧 */
	public Set<String> existItmCodes;

}
