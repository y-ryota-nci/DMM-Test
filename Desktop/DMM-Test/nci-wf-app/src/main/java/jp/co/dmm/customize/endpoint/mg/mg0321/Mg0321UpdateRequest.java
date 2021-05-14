package jp.co.dmm.customize.endpoint.mg.mg0321;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 消費税関連マスタ更新リクエスト
 *
 */
public class Mg0321UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 消費税種類コード */
	public String taxKndCd;
	/** 消費税種別 */
	public String taxSpc;
	/** 消費税コード */
	public String taxCd;

	/** 削除フラグ */
	public String dltFg;
}
