package jp.co.nci.iwf.endpoint.vd.vd0090;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 新規申請割当可能メニュー一覧の検索リクエスト
 */
public class Vd0090Request extends BasePagingRequest {
	public Long menuId;
	public String menuName;
	public String screenProcessCode;
	public String screenProcessName;
	public String screenCode;
	public String screenName;
	public String processDefCode;

	/** 削除用 */
	public List<Long> screenProcessMenuIds;
}
