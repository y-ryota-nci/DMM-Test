package jp.co.nci.iwf.component.tray;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ワークリストなどトレイ関連の画面用リクエストの基底クラス
 */
public class BaseTrayRequest extends BasePagingRequest {
	/** プロセス定義コード(＋枝番) */
	public String processDefCode;
	/** 申請金額(下限) */
	public String amountMin;
	/** 申請金額(上限) */
	public String amountMax;
	/** 件名 */
	public String subject;
	/** 申請番号. */
	public String applicationNo;
	/** 決裁番号. */
	public String approvalNo;
	/** 業務プロセス状態.*/
	public String businessProcessStatus;
}
