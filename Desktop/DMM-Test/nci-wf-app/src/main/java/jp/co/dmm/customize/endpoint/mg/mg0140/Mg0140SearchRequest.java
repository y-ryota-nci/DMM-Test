package jp.co.dmm.customize.endpoint.mg.mg0140;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0140SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目名称 */
	public String accNm;
	/** 勘定科目補助コード */
	public String accBrkdwnCd;
	/** 勘定科目補助名称 */
	public String accBrkdwnNm;
	/** 連番 */
	public String sqno;
	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
