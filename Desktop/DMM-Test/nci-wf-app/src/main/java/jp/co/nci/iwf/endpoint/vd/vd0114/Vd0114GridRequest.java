package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Vd0114GridRequest extends BaseRequest {
	public Long childContainerId;
	public Set<Integer> targetPartsTypes;
}
