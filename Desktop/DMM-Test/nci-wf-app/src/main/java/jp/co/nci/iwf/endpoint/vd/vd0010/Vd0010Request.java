package jp.co.nci.iwf.endpoint.vd.vd0010;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * コンテナ一覧リクエスト
 */
public class Vd0010Request extends BasePagingRequest {

	public String corporationCode;
	public String containerCode;
	public String containerName;
	public String tableName;
	public String deleteFlag;
	public Date tableSyncFrom;
	public Date tableSyncTo;
	public String syncTable;
	public Date tableModifiedFrom;
	public Date tableModifiedTo;
}
