package jp.co.nci.iwf.endpoint.au.au0011;

import java.io.Serializable;

/**
 * パスワード変更履歴
 */
public class Au0011History implements Serializable {
	/** 連番 */
	public int rowNo;
	/** 有効期限開始日 */
	public String validStartDate;
	/** ログイン失敗回数 */
	public Long loginNgCount;
	/** パスワード状況 */
	public String passwordStatus;
	/** パスワード変更要求 */
	public String passwordChangeRequest;
}
