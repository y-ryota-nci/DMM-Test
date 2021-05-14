package jp.co.nci.iwf.endpoint.dc.dc0062;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaItem;

public class Dc0062Response extends BaseResponse {

	public MwmMetaItem entity;

	public List<OptionItem> inputTypes;
	public List<OptionItem> options;
	public List<OptionItem> items;
	public List<OptionItem> deleteFlags;
}
