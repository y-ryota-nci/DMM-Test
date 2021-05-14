package jp.co.nci.iwf.endpoint.vd.vd0170;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActivityType;
import jp.co.nci.integrated_workflow.model.custom.WfvRoute;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 承認状況でのアクティビティ
 */
public class Vd0170ActivityEntity {

	/** アクティビティID */
	public Long activityId;
	/** 次アクティビティID */
	public Long activityIdNext;
	/** アクティビティ定義コード */
	public String activityDefCode;
	/** アクティビティ定義名 */
	public String activityDefName;
	/** アクティビティ状態(ACTIVITY_STATUS). */
	public String activityStatus;
	/** アクティビティ区分 */
	public String activityType;
	/** 現在のアクティビティかどうか */
	public boolean currentActivity;
	/** 終了済みアクティビティかどうか */
	public boolean closeActivity;

	/** 承認者一覧 */
	public List<Vd0170AssignedUser> assignedUsers;
	/** 並行承認内のAssignedEntity一覧 */
	public List<Vd0170ActivityEntity> parallels;
	/** 遷移先アクティビティ定義コードリスト */
	public Set<String> activityDefCodeTransits;

	/**
	 * コンストラクタ
	 * @param route 現在のアクティビティ
	 * @param routeMap プロセスインスタンスのルートMap
	 * @param transitMap プロセス定義上のルートMap
	 */
	public Vd0170ActivityEntity(ActivityEntity route,
			final Map<String, List<WfvRoute>> routeMap,
			final Map<String, List<WfvRoute>> transitMap) {
		MiscUtils.copyFields(route, this);

		// 参加者リスト
		this.assignedUsers = route.assignedUserList
				.stream()
				.map(e -> new Vd0170AssignedUser(e))
				.collect(Collectors.toList());

		// 対象アクティビティの次アクティビティID、または次アクティビティ定義コードを求める
		// 承認済みであれば次アクティビティIDが設定されているし、未来の予定ならルート定義上の次アクティビティ定義コードを設定
		if (route.activityIdNext != null && route.activityIdNext > 0L && route.closeActivity) {
			this.activityIdNext = route.activityIdNext;
		}
		else {
			// 次アクティビティIDがない＝まだ承認されていない未来のルート情報、または分岐の開始だ
			this.activityIdNext = null;

			// アクティビティから遷移可能なアクティビティ定義コードをすべて求める
			List<WfvRoute> transits = transitMap.get(route.activityDefCode);
			if (transits != null)
				this.activityDefCodeTransits = transits.stream()
					.map(e -> e.getActivityDefCodeTransit())
					.collect(Collectors.toSet());
		}

		// 並行承認があれば再帰呼び出し
		if (route.children != null) {
			this.parallels = route.children.stream()
					.map(child -> {
						if (ActivityType.PARALLEL_S.equals(child.activityType))
							return new Vd0170ActivityEntityParallel(child, routeMap, transitMap);
						else
							return new Vd0170ActivityEntity(child, routeMap, transitMap);
					})
					.collect(Collectors.toList());
		}
	}
}
