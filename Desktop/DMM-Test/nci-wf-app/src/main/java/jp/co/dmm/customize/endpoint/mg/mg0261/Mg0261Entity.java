package jp.co.dmm.customize.endpoint.mg.mg0261;

/**
 * 部門関連マスタ一覧の検索結果エンティティ
 */

public class Mg0261Entity{

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
	/** 取引先コード */
	public String splrCd;
	/** 取引先名称（漢字） */
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	public String splrNmKn;
	/** 口座引落日 */
	public String bnkaccChrgDt;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;
}
