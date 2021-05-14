package jp.co.dmm.customize.endpoint.mg.mg0181;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0181Request extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 通貨コード */
	public String mnyCd;
	/** 有効期間（開始） */
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;
	/** 連番 */
	public long sqno;

	/** 入力内容 */
	public Mg0181Entity inputs;
}
