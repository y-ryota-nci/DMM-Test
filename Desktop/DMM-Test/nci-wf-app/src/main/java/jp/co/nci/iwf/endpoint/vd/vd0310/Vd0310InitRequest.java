package jp.co.nci.iwf.endpoint.vd.vd0310;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 申請・承認画面の初期化リクエスト
 */
public class Vd0310InitRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public Long processId;
	public Long activityId;
	public Long screenProcessId;
	public Long timestampUpdated;
	public TrayType trayType;
	public String proxyUser;

	/** (コピー起票)コピー元プロセスID */
	public Long copyProcessId;

	/** 汎用パラメータ１ */
	public String param1;
	/** 汎用パラメータ２ */
	public String param2;
	/** 汎用パラメータ３ */
	public String param3;
}
