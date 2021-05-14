package jp.co.dmm.customize.endpoint.mg.mg0330;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 国マスタ検索リクエスト
 *
 */
public class Mg0330Request extends BasePagingRequest {

	/** */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 国コード */
	public String lndCd;
	/** 国名 */
	public String lndNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
