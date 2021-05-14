package jp.co.dmm.customize.endpoint.mg.mg0261;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ｸﾚｶ口座マスタ更新リクエスト
 *
 */
public class Mg0261UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** カード会社名称 */
	public String crdCompanyNm;
	/** ユーザコード */
	public String usrCd;
	/** 銀行口座コード*/
	public String bnkaccCd;
	/** 削除フラグ */
	public String dltFg;
	/** 取引先コード */
	public String splrCd;
	/** 口座引落日 */
	public String bnkaccChrgDt;
}
