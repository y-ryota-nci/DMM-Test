package jp.co.nci.iwf.endpoint.mm.mm0400;

import java.sql.Timestamp;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 企業グループマスタ一覧のリクエスト
 */
public class Mm0400Request extends BasePagingRequest {
	/** 企業グループコード */
	public String corporationGroupCode;
	/** 企業グループ名 */
	public String corporationGroupName;
	/** 削除区分 */
	public String deleteFlag;

	/** (削除用)更新日時 */
	public Timestamp timestampUpdated;
}
