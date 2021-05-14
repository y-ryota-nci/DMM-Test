package jp.co.dmm.customize.endpoint.mg.mg0031;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0031UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	/** 費目コード１ */
	public String itmExpsCd1;
	/** 費目コード２ */
	public String itmExpsCd2;
	/** 仕訳コード */
	public String jrnCd;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目補助コード */
	public String accBrkDwnCd;
	/** 管理科目コード */
	public String mngAccCd;
	/** 管理科目補助コード */
	public String mngAccBrkDwnCd;
	/** 予算科目補助コード */
	public String bdgtAccCd;
	/** 資産区分 */
	public String asstTp;
	/** 消費税コード */
	public String taxCd;
	/** 伝票グループ（GL） */
	public String slpGrpGl;
	/** 経費区分 */
	public String cstTp;
	/** 課税対象区分 */
	public String taxSbjTp;
	/** 削除フラグ */
	public String dltFg;
}
