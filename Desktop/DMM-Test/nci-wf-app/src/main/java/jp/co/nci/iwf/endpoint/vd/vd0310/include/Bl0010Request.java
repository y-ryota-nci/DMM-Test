package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessBbsInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Bl0010Request extends BaseRequest {
	public String corporationCode;
	public Long processId;
	public Long processBbsId;
	public ProcessBbsInfo processBbsInfo;
}
