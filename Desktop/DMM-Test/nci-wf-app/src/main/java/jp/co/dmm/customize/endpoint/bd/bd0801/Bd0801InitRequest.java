package jp.co.dmm.customize.endpoint.bd.bd0801;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 予算入力画面の初期化リクエスト
 */
public class Bd0801InitRequest extends BaseRequest {
	/** 本部の組織コード */
	public String organizationCodeLv2;
	/** 部・室の組織コード */
	public String organizationCodeLv3;
}
