package jp.co.nci.iwf.endpoint.dc.dc0072;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmMetaTemplateDetail;

public class Dc0072DeleteRequest extends BaseRequest {

	public Long metaTemplateId;
	public List<MwmMetaTemplateDetail> deleteList;
}
