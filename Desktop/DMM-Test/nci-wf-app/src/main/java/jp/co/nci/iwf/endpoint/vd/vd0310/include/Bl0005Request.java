package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 決裁関連文書ブロックのリクエスト
 */
public class Bl0005Request extends BaseRequest {
	public String corporationCode;
	public Long processId;
}
