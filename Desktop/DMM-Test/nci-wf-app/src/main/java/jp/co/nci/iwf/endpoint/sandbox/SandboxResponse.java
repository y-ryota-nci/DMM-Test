package jp.co.nci.iwf.endpoint.sandbox;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * Sandboxのレスポンス
 */
public class SandboxResponse extends BaseResponse {
	public List<OptionItem> localeCodes;
	public String mailEnv;
	public String dummySendTo;
	public String smtpHost;
	public String smtpPort;
}
