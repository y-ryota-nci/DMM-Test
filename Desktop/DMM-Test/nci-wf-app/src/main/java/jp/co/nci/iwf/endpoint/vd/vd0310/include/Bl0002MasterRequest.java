package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.Map;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Bl0002MasterRequest extends BaseRequest {
	public Long tableSearchId;
	public Map<String, String> initConditions;
	public String columnNameValue;
	public String columnNameLabel;
}
