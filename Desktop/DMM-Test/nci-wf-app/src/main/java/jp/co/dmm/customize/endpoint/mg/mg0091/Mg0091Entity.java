package jp.co.dmm.customize.endpoint.mg.mg0091;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 銀行口座マスタ設定画面エンティティ
 */

public class Mg0091Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 銀行口座コード */
	public String bnkaccCd;
	/** 連番 */
	public Long sqno;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行名称 */
	public String bnkNm;
	/** 銀行支店コード */
	public String bnkbrcCd;
	/** 銀行支店名称 */
	public String bnkbrcNm;
	/** 銀行口座種別 */
	public String bnkaccTp;
	/** 銀行口座種別名称 */
	public String bnkaccTpNm;
	/** 銀行口座番号 */
	public String bnkaccNo;
	/** 銀行口座名称 */
	public String bnkaccNm;
	/** 銀行口座名称(カタカナ) */
	public String bnkaccNmKn;
	/** 有効期間（開始） */
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;

	public String corporationCodeCreated;

	public String corporationCodeUpdated;

	public String ipCreated;

	public String ipUpdated;

	public Timestamp timestampCreated;

	public Timestamp timestampUpdated;

	public String userCodeCreated;

	public String userCodeUpdated;

}
