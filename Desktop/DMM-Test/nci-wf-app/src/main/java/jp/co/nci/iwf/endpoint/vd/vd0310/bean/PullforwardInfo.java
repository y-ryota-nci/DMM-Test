package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

public class PullforwardInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 遷移元アクティビティID */
	public Long activityId;

	/** 会社コード */
	public String corporationCode;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義コード枝番 */
	public String processDefDetailCode;
	/** アクティビティ定義コード */
	public String activityDefCode;

}
