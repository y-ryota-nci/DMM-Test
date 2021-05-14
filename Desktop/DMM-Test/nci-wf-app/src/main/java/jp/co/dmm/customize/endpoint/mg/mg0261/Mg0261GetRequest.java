package jp.co.dmm.customize.endpoint.mg.mg0261;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ｸﾚｶ口座マスタ取得リクエスト
 *
 */
public class Mg0261GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 取引先コード */
	public String splrCd;
	/** ユーザコード */
	public String usrCd;
}
