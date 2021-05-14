package jp.co.nci.iwf.endpoint.cm.cm0090;

import java.sql.Date;
import java.util.Set;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 参加者ロール検索のリクエスト
 */
public class Cm0090Request extends BasePagingRequest {
	public String corporationCode;
	public String assignRoleCode;
	public String assignRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public Set<String> belongTypes;
	// 文書管理用参加者ロール検索フラグ
	public String docFlag;
}
