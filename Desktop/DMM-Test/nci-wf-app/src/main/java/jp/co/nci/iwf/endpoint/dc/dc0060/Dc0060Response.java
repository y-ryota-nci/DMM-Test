package jp.co.nci.iwf.endpoint.dc.dc0060;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Dc0060Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> inputTypes;
//	public List<OptionItem> options;
	public List<OptionItem> deleteFlags;
}
