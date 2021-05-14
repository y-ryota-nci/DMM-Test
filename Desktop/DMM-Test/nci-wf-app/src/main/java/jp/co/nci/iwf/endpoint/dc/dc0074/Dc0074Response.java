package jp.co.nci.iwf.endpoint.dc.dc0074;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaItemEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmMetaTemplateDetailEx;

public class Dc0074Response extends BaseResponse {

	public MwmMetaTemplateDetailEx entity;
	public List<OptionItem> metaItems;
	public List<OptionItem> optionItems;
	public List<OptionItem> sortOrders;
	public List<OptionItem> deleteFlags;

	public MwmMetaItemEx metaItem;
}
