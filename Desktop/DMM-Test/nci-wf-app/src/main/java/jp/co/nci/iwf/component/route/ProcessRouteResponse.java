package jp.co.nci.iwf.component.route;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

public class ProcessRouteResponse extends BaseResponse {

	/** 承認ルート情報 */
	public List<ActivityEntity> routeList;
}
