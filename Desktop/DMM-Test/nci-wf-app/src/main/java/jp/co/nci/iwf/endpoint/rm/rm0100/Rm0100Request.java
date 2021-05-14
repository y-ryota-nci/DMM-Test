package jp.co.nci.iwf.endpoint.rm.rm0100;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 利用者ロール一覧のリクエスト
 */
public class Rm0100Request extends BasePagingRequest {

	/** 会社コード */
	public String corporationCode;
	/** 利用者ロールコード */
	public String menuRoleCode;
	/** 利用者ロール名 */
	public String menuRoleName;
	/** 有効期間開始年月日 */
	public Date validStartDate;
	/** 有効期間終了年月日 */
	public Date validEndDate;
	/** 削除フラグ */
	public String deleteFlag;
}
