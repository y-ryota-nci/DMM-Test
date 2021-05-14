package jp.co.nci.iwf.endpoint.up.up0010;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Up0010Entity implements Serializable {
	public String corporationCode;
	public String screenCode;
	public String screenName;
	public List<Map<String, String>> containerCodes;
}
