package jp.co.nci.iwf.endpoint.dc.dc0220;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書管理トレイ編集の保存リクエスト
 */
public class Dc0220SaveRequest extends BaseRequest {
	public Dc0220Entity entity;
	public List<Dc0220Condition> conditions;
	public List<Dc0220Result> results;
}
