package jp.co.dmm.customize.endpoint.mg.mg0251;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 部門関連マスタ取得リクエスト
 *
 */
public class Mg0251GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 部門コード */
	public String bumonCd;
	/** 組織コード */
	public String orgnzCd;
}
