package jp.co.nci.iwf.endpoint.wl.wl0011;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * トレイ編集の保存リクエスト
 */
public class Wl0011SaveRequest extends BaseRequest {

	public Wl0011Entity entity;
	public List<Wl0011Condition> conditions;
	public List<Wl0011Result> results;
}
