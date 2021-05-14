package jp.co.nci.iwf.endpoint.dc.dc0200;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書トレイ選択(個人用)画面の保存リクエスト
 */
public class Dc0200SaveRequest extends BaseRequest {
	public List<Dc0200Entity> inputs;
}
