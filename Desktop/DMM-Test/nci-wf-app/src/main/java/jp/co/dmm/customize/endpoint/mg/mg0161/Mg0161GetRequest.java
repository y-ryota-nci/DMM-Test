package jp.co.dmm.customize.endpoint.mg.mg0161;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 消費税マスタ取得リクエスト
 *
 */
public class Mg0161GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 消費税コード */
	public String taxCd;
	/** 連番 */
	public Long sqno;
}
