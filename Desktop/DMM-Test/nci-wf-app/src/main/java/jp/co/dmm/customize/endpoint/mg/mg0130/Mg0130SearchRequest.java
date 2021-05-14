package jp.co.dmm.customize.endpoint.mg.mg0130;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0130SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 連番 */
	public String sqno;
	/** 勘定科目名称 */
	public String accNm;
	/** 貸借 */
	public String dcTp;
	/** 税入力 */
	public String taxIptTp;

	/** 税処理コード(SuperStream) */
	public String taxCdSs;

	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
