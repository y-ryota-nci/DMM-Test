package jp.co.dmm.customize.endpoint.py.py0011;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Py0011CreateProcessRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String screenCode;
	public List<Py0011InputEntity> inputs;

}
