package jp.co.nci.iwf.endpoint.sandbox;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * Sandboxのリクエスト
 */
public class SandboxRequest extends BaseRequest {
	public String sendTo;
	public String localeCode;
	public Long timeoutSec;
}
