package jp.co.dmm.customize.endpoint.mg.mg0251;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 部門関連マスタ更新リクエスト
 *
 */
public class Mg0251UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 部門コード */
	public String bumonCd;
	/** 組織コード */
	public String orgnzCd;
	/** 削除フラグ */
	public String dltFg;
}
