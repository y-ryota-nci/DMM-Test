package jp.co.nci.iwf.endpoint.mm.mm0001;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 企業編集画面の初期化リクエスト
 */
public class Mm0001InitRequest extends BaseRequest {
	public String corporationCode;
	public Long timestampUpdated;
}
