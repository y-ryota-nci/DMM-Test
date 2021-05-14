package jp.co.dmm.customize.endpoint.mg.mg0030;

import java.io.Serializable;

/**
 * 費目関連マスタ一覧の検索結果エンティティ
 */

public class Mg0030Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	/** 組織名称 */
	public String orgnzNm;
	/** 費目コード（1） */
	public String itmExpsCd1;
	public String itmExpsNm1;
	/** 費目コード（2） */
	public String itmExpsCd2;
	public String itmExpsNm2;
	/** 仕訳コード */
	public String jrnCd;
	public String jrnNm;
	/** 勘定科目コード */
	public String accCd;
	public String accNm;
	/** 勘定科目補助コード */
	public String accBrkDwnCd;
	public String accBrkDwnNm;
	/** 管理科目コード */
	public String mngAccCd;
	public String mngAccNm;
	/** 管理科目補助コード */
	public String mngAccBrkDwnCd;
	public String mngAccBrkDwnNm;
	/** 予算科目補助コード */
	public String bdgtAccCd;
	public String bdgtAccNm;
	/** 資産区分 */
	public String asstTp;
	/** 消費税コード */
	public String taxCd;
	public String taxNm;
	/** 伝票グループ（GL） */
	public String slpGrpGl;
	/** 経費区分 */
	public String cstTp;

	/** 課税対象区分 */
	public String taxSbjTp;
	public String taxSbjNm;


	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;

}
