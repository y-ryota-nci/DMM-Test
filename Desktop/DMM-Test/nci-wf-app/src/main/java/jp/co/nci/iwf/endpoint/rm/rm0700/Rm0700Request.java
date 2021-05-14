package jp.co.nci.iwf.endpoint.rm.rm0700;

import java.sql.Date;
import java.util.Set;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 利用者ロール一覧のリクエスト
 */
public class Rm0700Request extends BasePagingRequest {

	public String corporationCode;
	public String menuRoleCode;
	public String menuRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public String deleteFlag;

	/** ダウンロードするメニューロールコード */
	public Set<String> menuRoleCodes;
}
