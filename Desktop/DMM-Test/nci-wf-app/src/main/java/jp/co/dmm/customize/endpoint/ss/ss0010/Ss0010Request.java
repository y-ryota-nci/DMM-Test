package jp.co.dmm.customize.endpoint.ss.ss0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * SS連携データRequest
 */
public class Ss0010Request extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 作成日（開始） */
	public String makDtS;
	/** 作成日（終了） */
	public String makDtE;
	/** 送信状況(未送信) */
	public boolean sndStsNon;
	/** 送信状況(送信済) */
	public boolean sndStsSnd;
	/** 送信状況(保留) */
	public boolean sndStsHld;
	/** 出力対象(GL) */
	public boolean datTpGL;
	/** 出力対象(AP) */
	public boolean datTpAP;
}
