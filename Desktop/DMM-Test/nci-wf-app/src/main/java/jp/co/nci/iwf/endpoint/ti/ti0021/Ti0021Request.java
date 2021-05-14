package jp.co.nci.iwf.endpoint.ti.ti0021;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Ti0021Request extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
	public List<Ti0021Category> categories;
	public List<Ti0021Table> tables;
}
