package jp.co.nci.iwf.endpoint.mm.mm0120;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Mm0120InitResponse extends BaseResponse {
	public java.sql.Date ymd;
	public String hhmm;
	public List<OptionItem> corporations;
}
