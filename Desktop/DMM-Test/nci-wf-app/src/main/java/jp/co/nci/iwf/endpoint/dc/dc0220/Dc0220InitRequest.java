package jp.co.nci.iwf.endpoint.dc.dc0220;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書トレイ編集の初期化リクエスト
 */
public class Dc0220InitRequest extends BaseRequest {
	public Long docTrayConfigId;
	public Long version;
	public String from;
}
