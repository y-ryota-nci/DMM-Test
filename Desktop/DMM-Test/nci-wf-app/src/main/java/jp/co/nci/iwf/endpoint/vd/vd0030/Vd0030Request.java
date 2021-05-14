package jp.co.nci.iwf.endpoint.vd.vd0030;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面一覧のリクエスト
 */
public class Vd0030Request extends BaseRequest {
	public List<Long> screenIds;
}
