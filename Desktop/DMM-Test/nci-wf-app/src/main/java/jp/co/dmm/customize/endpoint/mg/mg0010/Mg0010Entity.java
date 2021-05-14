package jp.co.dmm.customize.endpoint.mg.mg0010;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 品目マスタ一覧の検索結果エンティティ
 */

public class Mg0010Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	/** 組織名称 */
	public String orgnzNm;
	/** 品目コード */
	public String itmCd;
	/** 連番 */
	public BigDecimal sqno;
	/** 品目名称 */
	public String itmNm;
	/** カテゴリコード */
	public String ctgryCd;
	/** 在庫区分 */
	public String stckTp;
	/** 在庫区分名称 */
	public String stckTpNm;
	/** 単位 */
	public String untCd;
	/** 金額 */
	public BigDecimal amt;
	/** メーカー名称 */
	public String makerNm;
	/** メーカー型式 */
	public String makerMdlNo;
	/** 品目備考 */
	public String itmRmk;
	/** 調達部門区分フラグ */
	public String prcFldTp;
	/** 調達部門区分フラグ名称 */
	public String prcFldTpNm;
	/** 有効開始日付 */
	public Date vdDtS;
	/** 有効終了日付 */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;


	/** 有効期間（開始） */
	public String vdDtSStr;
	/** 有効期間（終了） */
	public String vdDtEStr;

}
