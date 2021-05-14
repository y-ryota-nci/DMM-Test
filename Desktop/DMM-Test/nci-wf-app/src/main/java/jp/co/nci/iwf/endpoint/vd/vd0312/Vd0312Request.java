package jp.co.nci.iwf.endpoint.vd.vd0312;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.component.route.ProcessRouteRequest;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

public class Vd0312Request extends ProcessRouteRequest {

	/** VD0310コンテンツ */
	public Vd0310Contents contents;
	/** HtmlIdをキーとした実行時パーツMap */
	public Map<String, PartsBase<?>> runtimeMap = new LinkedHashMap<>();
	/** 承認ルート情報 */
	public ActivityEntity target;
	/** 新しい承認者一覧 */
	public List<UserInfo> addAssignedUsers;
	/** ステップ追加か */
	public boolean addStep;
}
