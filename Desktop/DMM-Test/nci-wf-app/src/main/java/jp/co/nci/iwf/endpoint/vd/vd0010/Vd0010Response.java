package jp.co.nci.iwf.endpoint.vd.vd0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Vd0010Response extends BasePagingResponse {
	public List<OptionItem> corporations;
	public List<OptionItem> syncTableList;
}
