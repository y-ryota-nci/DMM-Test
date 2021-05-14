package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.nci.integrated_workflow.api.param.input.SetAssignedReservationInParam;
import jp.co.nci.integrated_workflow.api.param.output.SetAssignedReservationOutParam;
import jp.co.nci.integrated_workflow.model.custom.WfActivityEdit;
import jp.co.nci.integrated_workflow.model.custom.WfAssignedEdit;
import jp.co.nci.integrated_workflow.model.custom.impl.WfActivityEditImpl;
import jp.co.nci.integrated_workflow.model.custom.impl.WfAssignedEditImpl;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.component.route.AssignedUserInfo;
import jp.co.nci.iwf.component.route.ProcessRouteRequest;
import jp.co.nci.iwf.component.route.ProcessRouteService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;

/**
 * ブロック：承認者情報(承認ルート取得)サービス.
 */
@BizLogic
public class Bl0007Service extends ProcessRouteService {

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Bl0007Response init(Bl0007Request req) {
		// レスポンス生成
		final Bl0007Response res = createResponse(Bl0007Response.class, req);
		res.routeList = super.getProcessRouteInfo(req);
		res.success = true;
		return res;
	}

	/**
	 * 承認ルート更新.
	 * @param corporationCode 会社コード
	 * @param processId プロセスID
	 * @param changeRouteList
	 */
	public void update(Vd0310ExecuteRequest vd0310req, Vd0310ExecuteResponse vd0310res) {
		// 承認ルート情報の取得
		final ProcessRouteRequest req = new ProcessRouteRequest();
		req.corporationCode = vd0310req.contents.corporationCode;
		req.processId = vd0310req.contents.processId;
		req.bizInfos = vd0310res.bizInfos;
		List<ActivityEntity> routeList = super.getProcessRouteInfo(req);
		// ルート一覧をIDをキーにマップへ変換
		final Map<Long, ActivityEntity> routeMap = new HashMap<Long, ActivityEntity>();
		createRouteMap(routeList, routeMap);

		ActivityEntity previousActivity = null;
		for (ActivityEntity route : vd0310req.changeRouteList) {
			ActivityEntity org = route.id != null ? routeMap.remove(route.id) : null;
			// 追加または参加者変更があったアクティビティを処理
			if (route.addedActivity || route.changeActivity) {
				WfActivityEdit wfActivityEdit = new WfActivityEditImpl();
				if (route.addedActivity ) {
					wfActivityEdit.setActivityDefCode(previousActivity.activityDefCode);
					wfActivityEdit.setActivityDefCodeNew(route.activityDefCode);
					wfActivityEdit.setTemplateId(null);
					wfActivityEdit.setTemplateIdPrev(previousActivity.templateId);
					wfActivityEdit.setTemplateIdNext(previousActivity.templateIdNext);

				} else {
					wfActivityEdit.setActivityDefCode(org.activityDefCode);
					wfActivityEdit.setTemplateId(org.templateId);
				}
				wfActivityEdit.setWfAssignedEditList(new ArrayList<WfAssignedEdit>());
				for (AssignedUserInfo assignedUser: route.assignedUserList) {
					WfAssignedEdit assignedEdit = new WfAssignedEditImpl();
					assignedEdit.setEditType(AssignEditType.USER);
					assignedEdit.setCorporationCode(assignedUser.corporationCode);
					assignedEdit.setUserCode(assignedUser.userCode);
					wfActivityEdit.getWfAssignedEditList().add(assignedEdit);
				}

				SetAssignedReservationInParam in = new SetAssignedReservationInParam();
				in.setCorporationCode(vd0310req.contents.corporationCode);
				in.setProcessId(vd0310req.contents.processId);
				in.setWfActivityEdit(wfActivityEdit);
				// 実行者のユーザー情報
				in.setWfUserRole(sessionHolder.getWfUserRole());
				// API呼び出し
				SetAssignedReservationOutParam out = this.wf.setAssignedReservation(in);
				if (route.templateId == null) {
					route.templateId = out.getWftActivityTemplate().getActivityId();
					route.templateIdNext = out.getWftActivityTemplate().getActivityIdNext();
				}
				if (org == null) {
					org = route;
				}
			}
			if (org != null) {
				previousActivity =  org;
			}
		}

		// routeMapに残ったものは削除されたアクティビティ
		for (ActivityEntity route: routeMap.values()) {
			WfActivityEdit wfActivityEdit = new WfActivityEditImpl();
			wfActivityEdit.setWfAssignedEditList(new ArrayList<WfAssignedEdit>());
			wfActivityEdit.setActivityDefCode(route.activityDefCode);
			wfActivityEdit.setTemplateId(route.templateId);
			SetAssignedReservationInParam in = new SetAssignedReservationInParam();
			in.setCorporationCode(vd0310req.contents.corporationCode);
			in.setProcessId(vd0310req.contents.processId);
			in.setWfActivityEdit(wfActivityEdit);
			in.setTimestampUpdatedProcess(vd0310req.contents.timestampUpdated);
			// 実行者のユーザー情報
			in.setWfUserRole(sessionHolder.getWfUserRole());
			// API呼び出し
			SetAssignedReservationOutParam out = this.wf.setAssignedReservation(in);
			vd0310req.contents.timestampUpdated = out.getProcess().getTimestampUpdated();
			vd0310res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();
		}
	}

	private void createRouteMap(List<ActivityEntity> routeList, Map<Long, ActivityEntity> routeMap) {
		for (ActivityEntity route : routeList) {
			routeMap.put(route.id, route);
			if (isNotEmpty(route.children)) {
				createRouteMap(route.children, routeMap);
			}
		}
	}
}
