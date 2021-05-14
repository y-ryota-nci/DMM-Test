package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.CreateProcessInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.DoActionInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetAvailableActionListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetLatestActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetPullbackActivityDefListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetPullforwardActionListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetWfmActionListInParam;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.PullBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.PullForwardActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.SendBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.SetAdditionalInfoInParam;
import jp.co.nci.integrated_workflow.api.param.input.SetVariableValueInParam;
import jp.co.nci.integrated_workflow.api.param.input.StopProcessInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.output.CreateProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.DoActionOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetAvailableActionListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetPullbackActivityDefListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.PullBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.PullForwardActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SendBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SetAdditionalInfoOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SetVariableValueOutParam;
import jp.co.nci.integrated_workflow.api.param.output.StopProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.base.WfmAction;
import jp.co.nci.integrated_workflow.model.base.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.base.impl.WfmActionImpl;
import jp.co.nci.integrated_workflow.model.custom.WfLatestHistory;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WftActivityEx;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.WfvActionDef;
import jp.co.nci.integrated_workflow.param.input.SearchWftActivityInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.PullbackInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.PullforwardInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.SendbackInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * ブロック：ボタンのサービス
 */
@BizLogic
public class Bl0000Service extends BaseService implements CodeMaster {
	/** コメントを使用可能なアクションタイプ */
	private static Set<String> commentActionTypes = asSet(
			ActionType.NORMAL, ActionType.PULLBACK, ActionType.SENDBACK,
			ActionType.CANCEL, ActionType.PULLFORWARD);

	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	protected RouteSettingService routeSettingService;

	public void init(Vd0310InitRequest req, Vd0310InitResponse res) {
		res.contents.actionList = new ArrayList<>();
		res.contents.sendbackList = new HashMap<>();

		Set<String> latestHistoryList =
				isEmpty(res.contents.latestHistoryList) ? new HashSet<>()
						: res.contents.latestHistoryList.stream().map(l -> l.activityDefCode).collect(Collectors.toSet());

		// アクション情報取得
		Set<Long> doubleCheck = new HashSet<>();
		getAvailableActionList(res.contents).forEach(a -> {
			boolean isAdd = false;

			// アクティビティにアクションが定義されていても、最終的にはトレイ種別やプロセスの状態でアクションの使用可否が決まる
			switch (a.getActionType()) {
			case ActionType.NORMAL:			// 通常
			case ActionType.SAVE:			// 仮保存／一時保存
				isAdd = in(req.trayType, TrayType.NEW, TrayType.WORKLIST, TrayType.FORCE, TrayType.BATCH)
						&& in(res.contents.processStatus, null, ProcessStatus.START, ProcessStatus.RUN);
				break;
			case ActionType.CANCEL:			// 取消
				isAdd = in(req.trayType, TrayType.NEW, TrayType.WORKLIST, TrayType.FORCE, TrayType.BATCH)	// 新規も含んでいるのは仮保存後にすぐ取消をする場合を考慮して。
						&& in(res.contents.processStatus, ProcessStatus.START, ProcessStatus.RUN)
						&& isNotEmpty(req.processId);
				break;
			case ActionType.SENDBACK:		// 差戻し
			case ActionType.SENDBACK_NC:	// 差戻し（チェックなし）
				isAdd = in(req.trayType, TrayType.WORKLIST, TrayType.FORCE, TrayType.BATCH)
						&& in(res.contents.processStatus, ProcessStatus.START, ProcessStatus.RUN)
						&& latestHistoryList.contains(a.getActivityDefCodeTransit());
				break;
			case ActionType.PULLBACK:		// 引戻し
				isAdd = eq(TrayType.OWN, req.trayType)
						&& in(res.contents.processStatus, ProcessStatus.START, ProcessStatus.RUN)	// 生きてる案件
						&& isStartUserOrProcessUser(sessionHolder.getLoginInfo(), res.contents)		// 操作者＝起案者 or 入力者
						&& eq(CommonFlag.ON, res.contents.activityDef.getPullbackFlag());			// アクティビティ定義.引き戻し対象フラグ＝ON
				break;
			case ActionType.PULLFORWARD:	// 引上げ
				isAdd = eq(TrayType.ALL, req.trayType)
						&& in(res.contents.processStatus, ProcessStatus.START, ProcessStatus.RUN)
						&& !getPullforwardActionList(res.contents).isEmpty();
				break;
			case ActionType.DOACTION:		// アクション機能実行（印刷とか）
//				isAdd = in(req.trayType, TrayType.OWN, TrayType.ALL, TrayType.FORCE)
				isAdd = in(req.trayType, TrayType.NEW, TrayType.WORKLIST, TrayType.OWN, TrayType.ALL, TrayType.FORCE)
						&& isNotEmpty(req.processId);
				break;
			}

			if (!isAdd) {
				return;
			}

			if (eq(ActionType.SENDBACK, a.getActionType()) || eq(ActionType.SENDBACK_NC, a.getActionType())) {
				if (!res.contents.sendbackList.containsKey(a.getSeqNoActionDef())) {
					res.contents.sendbackList.put(a.getSeqNoActionDef(), new ArrayList<>());
				}
				res.contents.sendbackList.get(a.getSeqNoActionDef()).add(new SendbackInfo(a));
			}
			if (!doubleCheck.contains(a.getSeqNoActionDef())) {
				res.contents.actionList.add(new ActionInfo(a));
				doubleCheck.add(a.getSeqNoActionDef());
			}
		});

		if (eq(TrayType.OWN, req.trayType)) {
			List<WfmActivityDef> pullbackActivityDefList = getPullbackActivityDefList(res.contents);
			if (isNotEmpty(pullbackActivityDefList)) {
				if (res.contents.actionList.stream().filter(a -> eq(ActionType.PULLBACK, a.actionType)).count() == 0L) {
					ActionInfo e = createDummyAction(ActionType.PULLBACK);
					e.isDummy = true;
					res.contents.actionList.add(e);
				}
				res.contents.pullbackList = pullbackActivityDefList
						.stream()
						.map(a -> new PullbackInfo(a))
						.collect(Collectors.toList());
			} else {
				res.contents.actionList.clear();
			}
		} else if (eq(TrayType.ALL, req.trayType)) {
			// 引上げ条件：プロセスが生きており、生きてるアクティビティが引上元アクティビティであり、現アクティビティが引上先アクティビティである
			Optional<WftActivityEx> opt = getCurrentWftActivityList(res.contents)
					.stream()
					.filter(r -> eq(r.getPullforwardControlType(), CommonFlag.ON))	// ここから引上げ
					.findFirst();
			if (opt.isPresent()
					&& in(res.contents.processStatus, ProcessStatus.START, ProcessStatus.RUN)
					&& eq(res.contents.activityDef.getPullforwardFlag(), CommonFlag.ON)	// ここへ引上げ
				) {
				WftActivityEx current = opt.get();
						res.contents.pullforwardInfo = new PullforwardInfo();
				res.contents.pullforwardInfo.activityId = current.getActivityId();	// 現アクティビティのいずれかのアクティビティID
						res.contents.pullforwardInfo.corporationCode = res.contents.corporationCode;
						res.contents.pullforwardInfo.processDefCode = res.contents.processDefCode;
						res.contents.pullforwardInfo.processDefDetailCode = res.contents.processDefDetailCode;
						res.contents.pullforwardInfo.activityDefCode = res.contents.activityDefCode;	// 引上げ先のアクティビティ定義コード

				// アクティビティ定義に引上げアクションがなければでっち上げる。
				// もし存在していたら、アクティビティ定義側を優先させる（コールバックファンクションがあるかもしれないから）
				if (res.contents.actionList.stream().filter(a -> eq(ActionType.PULLFORWARD, a.actionType)).count() == 0L) {
					List<ActionInfo> pullForwardActionList = getPullforwardActionList(res.contents);
					res.contents.actionList.addAll(pullForwardActionList);
					}
			} else {
				// 引上げが有効でないなら、引上げボタンを削除
				for (Iterator<ActionInfo> it = res.contents.actionList.iterator(); it.hasNext(); ) {
					ActionInfo action = it.next();
					if (eq(action.actionType, ActionType.PULLFORWARD))
						it.remove();
				}
			}
		} else if (eq(TrayType.FORCE, req.trayType)) {
			// 強制変更用にアクションを補正
			modifyActionsForceChange(res.contents);
		}

		// コメント欄の使用可否
		// ＝コメント利用可能なアクションあり
		res.useActionComment = res.contents.actionList.stream().anyMatch(a ->  commentActionTypes.contains(a.actionType));
	}

	/** 現在のアクティビティを抽出 */
	private List<WftActivityEx> getCurrentWftActivityList(Vd0310Contents contents) {
		SearchWftActivityInParam cond = new SearchWftActivityInParam();
		cond.setCorporationCode(contents.corporationCode);
		cond.setProcessDefCode(contents.processDefCode);
		cond.setProcessDefDetailCode(contents.processDefDetailCode);
		cond.setProcessId(contents.processId);
		cond.setWfUserRole(sessionHolder.getWfUserRole());
		return wf.searchWftActivity(cond).getResultList().stream()
				.filter(a -> in(a.getActivityStatus(), ActivityStatus.START, ActivityStatus.RUN))
				.filter(a -> a.getActivityId() != null && a.getActivityId() > 0L)
				.collect(Collectors.toList());
	}

	/** 操作者が起案担当者または入力担当者か？ */
	private boolean isStartUserOrProcessUser(LoginInfo u, Vd0310Contents contents) {
		final String corporationCode = u.getCorporationCode();
		final String userCode = u.getUserCode();
		final UserInfo p = contents.processUserInfo;
		if (eq(corporationCode, p.getCorporationCode()) && eq(userCode, p.getUserCode()))
			return true;
		final UserInfo s = contents.startUserInfo;
		if (eq(corporationCode, s.getCorporationCode()) && eq(userCode, s.getUserCode()))
			return true;
		return false;
	}

	/** 強制変更用にアクションを補正 */
	private void modifyActionsForceChange(Vd0310Contents contents) {
		if (in(contents.processStatus, ProcessStatus.START, ProcessStatus.RUN, ProcessStatus.WAIT)) {
			final List<ActionInfo> actionList = contents.actionList;
			final String force = i18n.getText(MessageCd.force);
			final Set<String> exists = new HashSet<>();

			// 既存のアクション名称が「強制○○」となるように変更
			for (ActionInfo action : actionList) {
				action.actionName = force + action.actionName;
				exists.add(action.actionType);
			}

			// 強制廃案／強制一時保存アクションがなければ追加
			final String[] actionTypes = { ActionType.CANCEL, ActionType.SAVE };
			for (String actionType : actionTypes) {
				if (!exists.contains(actionType)) {
					ActionInfo ai = createDummyAction(actionType);
					ai.actionName = force + ai.actionName;
					actionList.add(ai);
				}
			}
		}
		else {
			// 完了した案件に対して、出来ることは何もない
			for (Iterator<ActionInfo> it = contents.actionList.iterator(); it.hasNext(); ) {
				ActionInfo action = it.next();
				if (!eq(ActionType.DOACTION, action.actionType))
					it.remove();
			};
		}
	}

	/** アクションタイプを元にアクション定義を求めて、アクションをでっち上げる */
	private ActionInfo createDummyAction(String actionType) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final WfmAction cond = new WfmActionImpl();
		cond.setCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		cond.setActionType(actionType);
		cond.setDeleteFlag(DeleteFlag.OFF);

		final GetWfmActionListInParam in = new GetWfmActionListInParam();
		in.setWfmAction(cond);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		return wf.getWfmActionList(in).getWfmActionList().stream()
				.map(a -> new ActionInfo(a, localeCode))
				.sorted((a1, a2) -> MiscUtils.compareTo(a1.actionCode, a2.actionCode))
				.findFirst()
				.orElse(null);
	}

	/** 引上げボタンリストを抽出 */
	private List<ActionInfo> getPullforwardActionList(Vd0310Contents contents) {
		// 起案前は不要
		if (isEmpty(contents.corporationCode) || isEmpty(contents.processId))
			return new ArrayList<>();
		// 起案後でも、終了してたり決裁後であったら不要
		if (in(contents.processStatus, ProcessStatus.END, ProcessStatus.END_R, ProcessStatus.ABORT, ProcessStatus.F_ABORT)
				|| eq(ApprovalStatus.APPROVED, contents.approvalStatus))
			return new ArrayList<>();

		// 引上げ対象外アクティビティ
		final List<String> exceptActivityDefCodeList = contents.latestHistoryList.stream()
				.filter(l -> !eq(l.activityDefCode, contents.activityDefCode))
				.map(l -> l.activityDefCode)
				.collect(Collectors.toList());

		final GetPullforwardActionListInParam in = new GetPullforwardActionListInParam();
		in.setCorporationCode(contents.corporationCode);
		in.setProcessId(contents.processId);
		in.setProcessDefCode(contents.processDefCode);
		in.setProcessDefDetailCode(contents.processDefDetailCode);
		if (isNotEmpty(exceptActivityDefCodeList)) {
			in.setActivityDefCodeOfExclusionList(exceptActivityDefCodeList); //検索対象外とするアクティビティ定義コードのリスト
		}
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// 引上げボタンリスト（アクティビティに適切なアクションがなければ、こっそりAPI側で引上げボタンを追加する）
		return wf.getPullforwardActionList(in).getWfvActionDefList().stream()
				.filter(ad -> eq(ad.getActivityDefCode(), contents.activityDefCode))
				.map(ad -> new ActionInfo(ad))
				.peek(ai -> {
					// 対象アクティビティに引上げ用のアクションが定義されていないとAPI側ででっち上げるが、
					// このときのアクション名が「引上」固定なので、多言語対応された名称となるよう補正
					if (eq(ai.actionType, ActionType.PULLFORWARD)
							&& eq(ai.actionName, "引上")
					) {
						ai.actionName = i18n.getText(MessageCd.pullforward);
						ai.isDummy = true;
					}
				})
				.collect(Collectors.toList());
	}

	private List<WfvActionDef> getAvailableActionList(Vd0310Contents contents) {
		final GetAvailableActionListInParam in = new GetAvailableActionListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());

		in.setCorporationCode(contents.corporationCode);
		in.setProcessDefCode(contents.processDefCode);
		in.setProcessDefDetailCode(contents.processDefDetailCode);
		in.setActivityDefCode(contents.activityDefCode);

		GetAvailableActionListOutParam out = wf.getAvailableActionList(in);
		if (out != null && ReturnCode.SUCCESS.compareTo(out.getReturnCode()) != 0) {
			throw new WfException(out);
		}
		return out.getWfvActionDefList();
	}

	private List<WfmActivityDef> getPullbackActivityDefList(Vd0310Contents contents) {
		final GetPullbackActivityDefListInParam in = new GetPullbackActivityDefListInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());
		in.setCorporationCode(contents.corporationCode);
		in.setProcessId(contents.processId);

		GetPullbackActivityDefListOutParam out = wf.getPullbackActivityDefList(in);
		if (out != null && ReturnCode.SUCCESS.compareTo(out.getReturnCode()) != 0) {
			throw new WfException(out);
		}
		return out.getWfmActivityDefList();
	}

	/** WF起票 */
	public void create(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		CreateProcessInstanceInParam in = new CreateProcessInstanceInParam();

		// Ver5までは入力者の代理先として起票担当者を設定して起票していたが、
		// Ver6では常に入力者＝起案担当者で起票し、それをsetAdditionalInfo()で
		// 正しい起案担当者に書き換えている。
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole()));

		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessDefCode(req.contents.processDefCode);
		in.setProcessDefDetailCode(req.contents.processDefDetailCode);
		in.setScreenProcessId(req.contents.screenProcessId);
		in.setOrganizationCodeStart(sessionHolder.getLoginInfo().getOrganizationCode());
		in.setPostCodeStart(sessionHolder.getLoginInfo().getPostCode());
		in.setSkip(false);

		CreateProcessInstanceOutParam out = wf.createProcessInstance(in);
		res.corporationCode = out.getProcess().getCorporationCode();
		res.processId = out.getProcess().getProcessId();
		res.activityId = out.getActivity().getActivityId();
		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();
		req.contents.timestampUpdated = out.getProcess().getTimestampUpdated();
	}

	/** WF状態遷移 */
	public void move(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		MoveActivityInstanceInParam in = new MoveActivityInstanceInParam();

		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessId(req.contents.processId);
		in.setActivityId(req.contents.activityId);
		in.setActionCode(req.actionInfo.actionCode);
		in.setWfUserRoleTransfer(null);
		in.setSkip(true);
		in.setActionComment(req.actionComment);
		in.setHandOverParam(handOverParam);
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		MoveActivityInstanceOutParam out = eq(TrayType.FORCE, req.contents.trayType)
				? wf.moveActivityInstanceByForce(in)
				: wf.moveActivityInstance(in);

		res.subject = out.getProcess().getSubject();
		res.applicationNo = out.getProcess().getApplicationNo();
		res.approvalNo = out.getProcess().getApprovalNo();
		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();

		handOverParam.put(InParamCallbackBase.class.getSimpleName(), in);
		handOverParam.put(OutParamCallbackBase.class.getSimpleName(), out);
	}

	/** WF引き戻し */
	public void pullback(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		PullBackActivityInstanceInParam in = new PullBackActivityInstanceInParam();
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessId(req.contents.processId);
		in.setActivityId(req.contents.activityId);
		if (!req.actionInfo.isDummy) {
			in.setActionCode(req.actionInfo.actionCode);
		}
		in.setActionComment(req.actionComment);
		in.setHandOverParam(handOverParam);
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		// 引戻し先用
		in.setCorporationCodePullback(req.pullbackInfo.corporationCode);
		in.setProcessDefCodePullback(req.pullbackInfo.processDefCode);
		in.setProcessDefDetailCodePullback(req.pullbackInfo.processDefDetailCode);
		in.setActivityDefCodePullback(req.pullbackInfo.activityDefCode);

		PullBackActivityInstanceOutParam out = wf.pullBackActivityInstance(in);
		res.subject = out.getProcess().getSubject();
		res.applicationNo = out.getProcess().getApplicationNo();
		res.approvalNo = out.getProcess().getApprovalNo();
		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();

		handOverParam.put(InParamCallbackBase.class.getSimpleName(), in);
		handOverParam.put(OutParamCallbackBase.class.getSimpleName(), out);
	}

	/** WF差戻し */
	public void sendback(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		SendBackActivityInstanceInParam in = new SendBackActivityInstanceInParam();
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessId(req.contents.processId);
		in.setActivityId(req.contents.activityId);
		in.setActionCode(req.actionInfo.actionCode);
		in.setSeqNoActionDef(req.actionInfo.seqNoActionDef);
		if (isNotEmpty(req.sendbackInfo)) {
			in.setSeqNoConditionDef(req.sendbackInfo.seqNoConditionDef);
		}
		in.setActionComment(req.actionComment);
		in.setHandOverParam(handOverParam);
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		SendBackActivityInstanceOutParam out = eq(TrayType.FORCE, req.contents.trayType)
				? wf.sendBackActivityInstanceByForce(in)
				: wf.sendBackActivityInstance(in);

		res.subject = out.getProcess().getSubject();
		res.applicationNo = out.getProcess().getApplicationNo();
		res.approvalNo = out.getProcess().getApprovalNo();
		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();

		handOverParam.put(InParamCallbackBase.class.getSimpleName(), in);
		handOverParam.put(OutParamCallbackBase.class.getSimpleName(), out);
	}

	/** WF取消 */
	public void cancel(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		// 未申請の取消はWFT_PROCESSを物理削除
		String actionType = req.actionInfo.actionType;
		String appStatus = req.contents.applicationStatus;
		String stopProcessType = eq(ActionType.CANCEL, actionType) && eq(ApplicationStatus.NOT_APPLIED, appStatus)
				? StopProcessType.PHYSICAL : StopProcessType.LOGICAL;

		StopProcessInstanceInParam in = new StopProcessInstanceInParam();
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessId(req.contents.processId);
		in.setActivityId(req.contents.activityId);
		// 強制変更はアクションをでっち上げていることがあるので、アクションコードを渡さない
		// （でっち上げているとAPIのアクション定義存在チェックに引っかかってエラーになる）
		if (!eq(TrayType.FORCE, req.contents.trayType)) {
			in.setActionCode(req.actionInfo.actionCode);
		}
		in.setDeleteType(stopProcessType);	// 物理削除するか？
		in.setActionComment(req.actionComment);
		in.setHandOverParam(handOverParam);
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		StopProcessInstanceOutParam out = eq(TrayType.FORCE, req.contents.trayType)
				? wf.stopProcessInstanceByForce(in)
				: wf.stopProcessInstance(in);

		res.subject = req.contents.subject;
		res.applicationNo = req.contents.applicationNo;
		res.approvalNo = req.contents.approvalNo;
		res.timestampUpdated = (req.contents.timestampUpdated == null ? -1L : req.contents.timestampUpdated.getTime());

		handOverParam.put(InParamCallbackBase.class.getSimpleName(), in);
		handOverParam.put(OutParamCallbackBase.class.getSimpleName(), out);
	}

	/** WF特定アクション実行（＝コールバック・ファンクションのみ呼び出す） */
	public void doaction(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		DoActionInParam in = new DoActionInParam();
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessId(req.contents.processId);
		in.setActivityId(req.contents.activityId);
		in.setActionCode(req.actionInfo.actionCode);
		in.setHandOverParam(handOverParam);
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		DoActionOutParam out = eq(TrayType.FORCE, req.contents.trayType)
				? wf.doActionByForce(in)
				: wf.doAction(in);
		res.subject = out.getProcess().getSubject();
		res.applicationNo = out.getProcess().getApplicationNo();
		res.approvalNo = out.getProcess().getApprovalNo();
//		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();	WfInstance.doAction()ではPKG_CORE_DOACTIONを呼び出さないようにしたので、もう不要である

		handOverParam.put(InParamCallbackBase.class.getSimpleName(), in);
		handOverParam.put(OutParamCallbackBase.class.getSimpleName(), out);
	}

	/** WF引上げ */
	public void pullforward(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, Map<String, Object> handOverParam) {
		PullForwardActivityInstanceInParam in = new PullForwardActivityInstanceInParam();
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessId(req.contents.processId);
		if (!req.actionInfo.isDummy) {
			in.setActionCode(req.actionInfo.actionCode);
		}
		in.setActivityId(req.pullforwardInfo.activityId);	// 現アクティビティならどこのアクティビティでも良い
		in.setCorporationCodePullforward(req.pullforwardInfo.corporationCode);
		in.setProcessDefCodePullforward(req.pullforwardInfo.processDefCode);
		in.setProcessDefDetailCodePullforward(req.pullforwardInfo.processDefDetailCode);
		in.setActivityDefCodePullforward(req.pullforwardInfo.activityDefCode);	// 引上げ先アクティビティ定義コード
		in.setActionComment(req.actionComment);
		in.setHandOverParam(handOverParam);
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		PullForwardActivityInstanceOutParam out = wf.pullForwardActivityInstance(in);
		res.subject = out.getProcess().getSubject();
		res.applicationNo = out.getProcess().getApplicationNo();
		res.approvalNo = out.getProcess().getApprovalNo();
		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();

		handOverParam.put(InParamCallbackBase.class.getSimpleName(), in);
		handOverParam.put(OutParamCallbackBase.class.getSimpleName(), out);
	}

	/** WF比較条件変数の登録 */
	public void setVariable(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		if (res.bizInfos != null) {
			res.bizInfos.forEach((key, value) -> {
				SetVariableValueInParam in = new SetVariableValueInParam();
				in.setCorporationCode(req.contents.corporationCode);
				in.setProcessId(req.contents.processId);
				in.setProcessDefCode(req.contents.processDefCode);
				in.setProcessDefDetailCode(req.contents.processDefDetailCode);
				in.setVariableDefCode(key);
				in.setValue(value);
				in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
				in.setTimestampUpdatedProcess(req.contents.timestampUpdated);
				SetVariableValueOutParam out = wf.setVariableValue(in);
				req.contents.timestampUpdated = out.getProcess().getTimestampUpdated();
				res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();
			});
		}
	}

	/** WF伝票付加情報の登録 */
	public void setAdditionalInfo(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		SetAdditionalInfoInParam in = new SetAdditionalInfoInParam();
		if (isNotEmpty(res.bizInfos)) {
			res.bizInfos.forEach((key, value) -> {
				if (eq(key, "AMOUNT"))
					in.setAmount(toBD(value));
				else if (key.startsWith("PROCESS_BUSINESS_INFO"))
					setProperty(in, toCamelCase(key.substring(8)), value);
				else
					setProperty(in, toCamelCase(key), value);
			});
		}
		in.setProcessId(req.contents.processId);
		in.setCorporationCode(req.contents.corporationCode);
		in.setProcessRefUnitType(ProcessRefUnitType.PROCESS);
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.contents.proxyUser));
		if (isNotEmpty(req.startUserInfo)) {
			in.setCorporationCodeProxyStart(req.startUserInfo.getCorporationCode());
			in.setUserCodeProxyStart(req.startUserInfo.getUserCode());
			in.setOrganizationCodeStart(req.startUserInfo.getOrganizationCode());
			in.setPostCodeStart(req.startUserInfo.getPostCode());
		}
		in.setTimestampUpdatedProcess(req.contents.timestampUpdated);

		SetAdditionalInfoOutParam out = eq(TrayType.FORCE, req.contents.trayType)
				? wf.setAdditionalInfoByForce(in)
				: wf.setAdditionalInfo(in);
		req.contents.timestampUpdated = out.getProcess().getTimestampUpdated();
		res.timestampUpdated = out.getProcess().getTimestampUpdated().getTime();
	}

	/** 対象プロセスの最新アクティビティ抽出 */
	public List<WfLatestHistory> getLatestActivityList(Vd0310InitRequest req) {
		GetLatestActivityListInParam in = new GetLatestActivityListInParam();
		in.setCorporationCode(req.corporationCode);
		in.setProcessId(req.processId);
		in.setMode(GetLatestActivityListInParam.Mode.ASSIGNED);
		in.setWfUserRole(cloneUserRole(sessionHolder.getWfUserRole(), req.proxyUser));

		return wf.getLatestActivityList(in).getWfLatestHistoryList();
	}

	/** 代理情報なしでWF操作者情報インスタンスを生成 */
	private WfUserRole cloneUserRole(WfUserRole src) {
		return cloneUserRole(src, null);
	}

	/** 代理情報ありでWF操作者情報インスタンスを生成 */
	public WfUserRole cloneUserRole(WfUserRole src, String proxyUser) {
		final WfUserRole dest = src.clone(true);
		if (isEmpty(proxyUser)) {
			dest.setProxyUserRole(null);
			dest.setProxy(false);
		}
		else {
			final String[] astr = proxyUser.split("_");
			dest.setProxyUserRole(new WfUserRoleImpl(astr[0], astr[1]));
			dest.setProxy(true);
		}
		return dest;
	}
}
