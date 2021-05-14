package jp.co.dmm.customize.endpoint.mg.mg0260;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ｸﾚｶ口座マスタ検索リクエスト
 *
 */
public class Mg0260SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** カード会社名称 */
	public String crdCompanyNm;
	/** ユーザコード */
	public String usrCd;
	public String usrNm;
	/** 銀行口座コード*/
	public String bnkaccCd;
	public String bnkaccNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
