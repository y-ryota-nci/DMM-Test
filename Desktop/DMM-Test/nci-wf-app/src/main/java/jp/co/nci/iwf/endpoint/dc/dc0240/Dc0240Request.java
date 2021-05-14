package jp.co.nci.iwf.endpoint.dc.dc0240;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 文書トレイ一覧(管理者用)の検索リクエスト
 */
public class Dc0240Request extends BasePagingRequest {

	public List<Long> trayConfigIds;
	public String ownerUserCode;
	public String trayConfigCode;
	public String trayConfigName;
	public String systemFlag;

}
