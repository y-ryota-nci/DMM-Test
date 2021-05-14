package jp.co.nci.iwf.endpoint.rm.rm0000;

import java.sql.Date;
import java.util.Set;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 利用者ロール一覧のリクエスト
 */
public class Rm0000Request extends BasePagingRequest {

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

	/** ダウンロード対象のメニューロールコード */
	public Set<String> menuRoleCodes;
}
