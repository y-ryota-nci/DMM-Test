package jp.co.nci.iwf.endpoint.vd.vd0170;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.nci.integrated_workflow.model.custom.WfvRoute;
import jp.co.nci.iwf.component.route.ActivityEntity;

/**
 * 承認状況での並行分岐開始アクティビティ
 */
public class Vd0170ActivityEntityParallel extends Vd0170ActivityEntity {

	public Long endActivityId;
	public Long endActivityIdNext;
	public String endActivityDefCode;
	public Set<String> endActivityDefCodeTransits;

	/**
	 * コンストラクタ
	 * @param route
	 * @param activityMap
	 * @param transitMap
	 */
	public Vd0170ActivityEntityParallel(ActivityEntity route,
			final Map<String, List<WfvRoute>> routeMap,
			Map<String, List<WfvRoute>> transitMap) {
		super(route, routeMap, transitMap);

		// 実績値（並行分岐開始アクティビティに対する並行分岐終了アクティビティが完了していれば）
		final String endActivityDefCode = toEndActivityDefCode(route.activityDefCode);
		this.endActivityDefCode = endActivityDefCode;
		final List<WfvRoute> routes = routeMap.get(this.endActivityDefCode);
		for (WfvRoute end : routes) {
			if (end.getActivityDate() != null && end.getActivityId() != null && end.getActivityIdNext() != null) {
				this.endActivityId = end.getActivityId();
				this.endActivityIdNext = end.getActivityIdNext();
			}
		}

		// 予定値（未来）
		final List<WfvRoute> transits = transitMap.get(this.endActivityDefCode);
		this.endActivityDefCodeTransits = transits.stream()
				.map(t -> t.getActivityDefCodeTransit())
				.collect(Collectors.toSet());
	}

	/** 「並行分岐の開始」アクティビティ定義コードから、 「並行分岐の終了」のアクティビティ定義コードを求める */
	private String toEndActivityDefCode(String startActivityDefCod) {
		return startActivityDefCod.substring(0, 9) + "E" + startActivityDefCod.substring(10);
	}

}
