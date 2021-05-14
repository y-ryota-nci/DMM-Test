package jp.co.nci.iwf.endpoint.wl.wl0020;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 個人用トレイ選択画面の更新リクエスト
 */
public class Wl0020SaveRequest extends BaseRequest {
	public List<Wl0020Entity> inputs;
}
