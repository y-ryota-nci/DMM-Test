package jp.co.dmm.customize.endpoint.mg.mg0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0010SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	public String orgnzNm;
	/** 品目コード */
	public String itmCd;
	/** 品目名称 */
	public String itmNm;

	/** 調達部門区分フラグ(人事) */
	public boolean prcFldTpHr;
	/** 調達部門区分フラグ(総務) */
	public boolean prcFldTpGa;
	/** 調達部門区分フラグ(情シス) */
	public boolean prcFldTpIs;
	/** 調達部門区分フラグ(その他) */
	public boolean prcFldTpOt;

	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
