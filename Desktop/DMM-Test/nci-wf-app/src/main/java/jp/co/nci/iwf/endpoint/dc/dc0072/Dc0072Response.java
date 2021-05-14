package jp.co.nci.iwf.endpoint.dc.dc0072;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDef;

public class Dc0072Response extends BasePagingResponse {

	public MwmMetaTemplateDef entity;
	public List<OptionItem> deleteFlags;
}
