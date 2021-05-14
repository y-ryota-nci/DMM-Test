package jp.co.nci.iwf.endpoint.wl.wl0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * トレイ設定一覧（管理者）の検索リクエスト
 */
public class Wl0010Request extends BasePagingRequest {

	public String trayConfigCode;
	public String trayConfigName;
	public String systemFlag;
	public String deleteFlag;

	/** 削除対象のトレイ設定IDリスト */
	public List<Long> trayConfigIds;

	/** 所有者ユーザコード。WL0015トレイ設定一覧(一般)で、操作者の所有するトレイ設定だけを抽出するために使われる。 */
	public String ownerUserCode;
}
