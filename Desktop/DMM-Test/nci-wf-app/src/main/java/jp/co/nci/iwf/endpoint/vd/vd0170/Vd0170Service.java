package jp.co.nci.iwf.endpoint.vd.vd0170;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.model.custom.WfvRoute;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.component.route.ProcessRouteRequest;
import jp.co.nci.iwf.component.route.ProcessRouteService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 承認状況画面サービス
 */
@BizLogic
public class Vd0170Service extends ProcessRouteService {
	@Inject private UserDataService updator;
	@Inject private ScreenLoadService loader;
	@Inject private Logger log;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0170Response init(Vd0170Request req) {
		if (req.contents == null)
			throw new BadRequestException("キーが未指定です");
		if (isEmpty(req.contents.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.contents.processId == null)
			throw new BadRequestException("プロセスIDが未指定です");

		// getRoute()して、アクティビティリストを取得
		final Vd0170Response res = createResponse(Vd0170Response.class, req);
		res.activityList = createActivityEntityList(req);
		res.success = true;
		return res;
	}

	/**
	 * ルート情報をもとに承認状況リストを生成
	 * @param req
	 * @return
	 */
	private List<Vd0170ActivityEntity> createActivityEntityList(Vd0170Request req) {
		// 現プロセスのルート情報をシミュレーションして抽出し、アクティビティ定義コードをキーにMap化
		final ProcessRouteRequest cond = createProcessRouteRequest(req.contents, req.viewWidth);
		final Map<String, List<WfvRoute>> routeMap = getRoute(cond).stream()
				.collect(Collectors.groupingBy(WfvRoute::getActivityDefCode));

		// ルート定義を抽出し、アクティビティ定義コードをキーにMap化
		final Map<String, List<WfvRoute>> transitMap = getRouteDef(cond).stream()
			.collect(Collectors.groupingBy(WfvRoute::getActivityDefCode));

		// ルート情報をもとに承認状況リストへ変換
		List<ActivityEntity> routeList = getProcessRouteInfo(cond);
		List<Vd0170ActivityEntity> vd0170List = routeList.stream()
			.map(route -> {
				if (ActivityType.PARALLEL_S.equals(route.activityType))
					return new Vd0170ActivityEntityParallel(route, routeMap, transitMap);
				else
					return new Vd0170ActivityEntity(route, routeMap, transitMap);
			})
			.collect(Collectors.toList());

		log.debug("List<Vd0170ActivityEntity> ====================");
		logVd0170ActivityEntity(vd0170List, 0);

		return vd0170List;
	}

	/** Vd0310コンテンツからGetRoute用の検索リクエストをでっち上げる */
	private ProcessRouteRequest createProcessRouteRequest(Vd0310Contents contents, ViewWidth viewWidth) {
		ProcessRouteRequest req = new ProcessRouteRequest();
		req.corporationCode = contents.corporationCode;
		req.processId = contents.processId;
		req.processDefCode = contents.processDefCode;
		req.processDefDetailCode = contents.processDefDetailCode;
		req.activityDefCode = contents.activityDefCode;
		// 入力者情報
		req.processUserInfo = contents.processUserInfo;
		// 起案担当者情報
		req.startUserInfo = contents.startUserInfo;
		// 業務管理項目Map
		req.bizInfos = getBizMap(contents, viewWidth);
		// シミュレーションか
		req.simulation = true;

		return req;
	}

	/** 業務管理項目Map取得. */
	private Map<String, String> getBizMap(Vd0310Contents contents, ViewWidth viewWidth) {
		// BizMap取得のためにDesignerContextを生成
		DesignerContext ctx = RuntimeContext.newInstance(contents, viewWidth, contents.runtimeMap);
		// パーツ定義をDBから取得
		loader.loadScreenParts(contents.screenId, ctx);

		return updator.createBusinessInfoMap(ctx);
	}

	/** 承認ルートに並行分岐の終了を含めるか */
	@Override
	protected boolean isAppendParallelEndActivity() {
		return true;
	}

	private void logVd0170ActivityEntity(List<Vd0170ActivityEntity> activityList, int indent) {
		for (Vd0170ActivityEntity activity : activityList) {
			if (activity.activityDefCodeTransits == null)
				log.debug("{}{} [{}] -> null", repeat(" ", indent), activity.activityDefCode, activity.activityDefName);
			else
				log.debug("{}{} [{}] -> {}", repeat(" ", indent), activity.activityDefCode, activity.activityDefName, Arrays.toString(activity.activityDefCodeTransits.toArray()));

			if (activity.parallels != null) {
				logVd0170ActivityEntity(activity.parallels, indent + 2);
			}
		}
	}
}
