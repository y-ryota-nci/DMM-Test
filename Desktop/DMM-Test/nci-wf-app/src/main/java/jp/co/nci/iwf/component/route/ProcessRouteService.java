package jp.co.nci.iwf.component.route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.api.param.input.GetRouteDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetRouteInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetWfmVariableDefListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetRouteDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetRouteOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetWfmVariableDefListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.base.WfmVariableDef;
import jp.co.nci.integrated_workflow.model.base.WftVariable;
import jp.co.nci.integrated_workflow.model.base.impl.WfmVariableDefImpl;
import jp.co.nci.integrated_workflow.model.base.impl.WftVariableImpl;
import jp.co.nci.integrated_workflow.model.custom.WfAssignedTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WfvRoute;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 承認ルート情報取得サービス.
 */
@BizLogic
public class ProcessRouteService extends BaseService implements CodeMaster {

	/** 区切り文字 */
	private static final String SEPARATOR  = "/";
	/** 代理者名連結用区切り文字 */
	public static final String SEPARATOR_BR = "<BR/>";


	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 承認ルート情報の取得.
	 * @param req
	 * @return
	 */
	protected List<ActivityEntity> getProcessRouteInfo(ProcessRouteRequest req) {
		// 戻り値
		// 開始～終了アクティビティまでにつらなるメインルート上のアクティビティ一覧
		final List<ActivityEntity> routes = new ArrayList<>();

		// 並行承認用のマップ
		// 並行承認のアクティビティ定義コードをキーに、その並行承認内の直接の子アクティビティ一覧を保持
		// 最後にroutesとマージする
		final Map<String, List<ActivityEntity>> parallelMap = new HashMap<String, List<ActivityEntity>>();

		// 並行承認用アクティビティ定義コードのスタック
		final Stack<String> stack = new Stack<>();

		// 並行承認のアクティビティ定義コード
		// 並行承認内の直接の子アクティビティに設定される
		// 並行承認内においてさらに並行承認アクティビティがあった場合はいったんstackに入れられ、
		// 新たな並行承認のアクティビティ定義コードに置き換わる
		String parentKey = null;

		// プロセスIDの有無により定義orインスタンスから承認ルート情報を取得する
		final List<WfvRoute> wfvRouteList = req.processId != null ? this.getRoute(req) : this.getRouteDef(req);

		final Set<String> parallelEnds = new HashSet<>();

		int len = wfvRouteList.size();
		for (int i = 0; i < len; i++) {
			final WfvRoute wfvRoute = wfvRouteList.get(i);
			final String tempActivityType = wfvRoute.getActivityType();

			// 開始・終了は除外
			if (this.exists(tempActivityType, ActivityType.START, ActivityType.END)) {
				continue;
			}
			// 並行終了・業務フロー連携終了(従属型)アクティビティは除外
			// stackが残っていれば取り出して新たなparentKeyとする
			if (this.exists(tempActivityType, ActivityType.PARALLEL_E, ActivityType.WF_LINK_D_E)) {
				parentKey = (stack.size() == 0 ? null : stack.pop());

				// 並行承認の終了も含めるか？→継承先の承認状況サービス(Vd0170Service)では必要だ
				if (isAppendParallelEndActivity() && !parallelEnds.contains(wfvRoute.getActivityDefCode())) {
					final WfvRoute wfvRouteNext = this.getNextwfvRoute(wfvRoute, i, wfvRouteList);
					final ActivityEntity route = this.convert(wfvRoute, wfvRouteNext, parentKey);
					routes.add(route);

					// 複数回登場しないよう、ユニーク化
					parallelEnds.add(wfvRoute.getActivityDefCode());
				}
				continue;
			}

			// 次アクティビティを取得
			// TODO 鷲田さんにいってWfvRouteにtemplateIdPrev,templateIdNextを追加してもらう
			WfvRoute wfvRouteNext = this.getNextwfvRoute(wfvRoute, i, wfvRouteList);

			// 上記以外のアクティビティはここでActivityEntityへとコンバート
			final ActivityEntity route = this.convert(wfvRoute, wfvRouteNext, parentKey);

			// プロセスIDがない状態の場合、最初のActivityEntityをカンレトアクティビティにする
			if (routes.isEmpty() && req.processId == null) {
				route.currentActivity = true;
				route.assignedUserList.stream().forEach(e -> e.showArrow = true);
			}

			// 並行開始・業務フロー連携開始(従属型)アクティビティの場合、parentKeyを設定
			// ただし既にparentKeyがあれば古いparentKeyはstackへ入れ、parentKeyを再設定する
			if (this.exists(tempActivityType, ActivityType.PARALLEL_S, ActivityType.WF_LINK_D_S)) {
				if (isNotEmpty(parentKey)) {
					parallelMap.get(parentKey).add(route);
					stack.push(parentKey);
					parentKey = route.activityDefCode;
				} else {
					parentKey = route.activityDefCode;
					if (!parallelMap.containsKey(parentKey)) {
						routes.add(route);
					}
				}
			}
			// parentKeyがある＝並行承認内のアクティビティの場合、並行承認用のマップに追加
			else if (isNotEmpty(parentKey)) {
				if (!parallelMap.containsKey(parentKey)) {
					parallelMap.put(parentKey, new ArrayList<>());
				}
				parallelMap.get(parentKey).add(route);
			}
			else {
				routes.add(route);
			}
		}

		// 最後に並行承認用のマップをマージする
		merge(routes, parallelMap);

		return routes;
	}

	/** 承認ルートに並行分岐の終了を含めるか */
	protected boolean isAppendParallelEndActivity() {
		return false;
	}

	protected WfvRoute getNextwfvRoute(WfvRoute wfvRoute, int idx, final List<WfvRoute> wfvRouteList) {
		// 並行開始・業務フロー連携開始(従属型)アクティビティの場合、次アクティビティは不要
		if (this.exists(wfvRoute.getActivityType(), ActivityType.PARALLEL_S, ActivityType.WF_LINK_D_S)) {
			return null;
		}
		return (idx + 1 < wfvRouteList.size() ? wfvRouteList.get(idx + 1) : null);
	}

	protected void merge(final List<ActivityEntity> routes, final Map<String, List<ActivityEntity>> parallelMap) {
		for (ActivityEntity route : routes) {
			if (parallelMap.containsKey(route.activityDefCode)) {
				route.children.addAll(parallelMap.get(route.activityDefCode));
				merge(route.children, parallelMap);
			}
		}
	}

	/**
	 * 承認ルート(定義)情報の取得.
	 * @param req
	 */
	protected List<WfvRoute> getRouteDef(ProcessRouteRequest req) {
		// 比較条件変数がない場合、ここで生成
		List<WftVariable> wftVariableList = createWftVariable(req);

		// 承認ルート情報取得条件の生成
		GetRouteDefInParam in = new GetRouteDefInParam();
		in.setUnfoldRoleFlg(true);// 承認者を取得する
		in.setCorporationCode(req.corporationCode);
		in.setProcessDefCode(req.processDefCode);
		in.setProcessDefDetailCode(req.processDefDetailCode);
		in.setSimulation(true);
		in.setWftVariableList(wftVariableList);

		// 起票者情報をセット
		in.setOrganizationCodeStart(req.startUserInfo.getOrganizationCode());
		in.setPostCodeStart(req.startUserInfo.getPostCode());
		WfUserRole userRoleStart = new WfUserRoleImpl();
		userRoleStart.setCorporationCode(req.startUserInfo.getCorporationCode());
		userRoleStart.setUserCode(req.startUserInfo.getUserCode());
		in.setWfUserRoleStart(userRoleStart);

		// ユーザー情報セット
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 実行
		GetRouteDefOutParam out = wf.getRouteDef(in);
		return out.getWfvRouteList();
	}

	/**
	 * 承認ルート(インスタンス)情報の取得.
	 * @param req
	 */
	protected List<WfvRoute> getRoute(ProcessRouteRequest req) {
		List<WftVariable> wftVariableList = null;
		if (req.simulation) {
			wftVariableList = this.createWftVariable(req);
		}
		// 承認ルート情報取得条件の生成
		GetRouteInParam in = new GetRouteInParam();
		in.setUnfoldRoleFlg(true);// 承認者を取得する
		in.setCorporationCode(req.corporationCode);
		in.setProcessId(req.processId);
		in.setSimulation(req.simulation);
		in.setWftVariableList(wftVariableList);
		// (新)起票者
		final UserInfo p = req.processUserInfo;
		final UserInfo s = req.startUserInfo;
		if (p != null && s != null) {
			if (!eq(p.getCorporationCode(), s.getCorporationCode()) || !eq(p.getUserCode(), s.getUserCode())) {
				in.setNewWfUserRoleStart(new WfUserRoleImpl(s.getCorporationCode(), s.getUserCode()));
			}
		}
		// ユーザー情報セット
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 実行
		GetRouteOutParam out = wf.getRoute(in);
		return out.getWfvRouteList();
	}

	private List<WftVariable> createWftVariable(ProcessRouteRequest req) {
		final List<WftVariable> wftVariableList = new ArrayList<WftVariable>();
		// 起案担当者組織コード
		{
			WftVariable organizationCodeStart = new WftVariableImpl();
			organizationCodeStart.setCorporationCode(req.corporationCode);
			organizationCodeStart.setProcessId(req.processId);
			organizationCodeStart.setProcessDefCode(req.processDefCode);
			organizationCodeStart.setProcessDefDetailCode(req.processDefDetailCode);
			organizationCodeStart.setVariableDefCode(CodeMaster.VariableDefCode.ORGANIZATION_CODE_START);
			organizationCodeStart.setDataType(CodeMaster.DataType.STRING);
			organizationCodeStart.setValue(req.startUserInfo.getOrganizationCode());
			wftVariableList.add(organizationCodeStart);
		}
		// 起案担当者役職コード
		{
			WftVariable postCodeStart = new WftVariableImpl();
			postCodeStart.setCorporationCode(req.corporationCode);
			postCodeStart.setProcessId(req.processId);
			postCodeStart.setProcessDefCode(req.processDefCode);
			postCodeStart.setProcessDefDetailCode(req.processDefDetailCode);
			postCodeStart.setVariableDefCode(CodeMaster.VariableDefCode.POST_CODE_START);
			postCodeStart.setDataType(CodeMaster.DataType.STRING);
			postCodeStart.setValue(req.startUserInfo.getPostCode());
			wftVariableList.add(postCodeStart);
		}
		// 起案担当者ユーザコード
		{
			WftVariable userCodeProxyStart = new WftVariableImpl();
			userCodeProxyStart.setCorporationCode(req.corporationCode);
			userCodeProxyStart.setProcessId(req.processId);
			userCodeProxyStart.setProcessDefCode(req.processDefCode);
			userCodeProxyStart.setProcessDefDetailCode(req.processDefDetailCode);
			userCodeProxyStart.setVariableDefCode(CodeMaster.VariableDefCode.USER_CODE_PROXY_START);
			userCodeProxyStart.setDataType(CodeMaster.DataType.STRING);
			userCodeProxyStart.setValue(req.startUserInfo.getUserCode());
			wftVariableList.add(userCodeProxyStart);
		}
		// 入力担当者ユーザコード
		{
			WftVariable userCodeOperationStart = new WftVariableImpl();
			userCodeOperationStart.setCorporationCode(req.corporationCode);
			userCodeOperationStart.setProcessId(req.processId);
			userCodeOperationStart.setProcessDefCode(req.processDefCode);
			userCodeOperationStart.setProcessDefDetailCode(req.processDefDetailCode);
			userCodeOperationStart.setVariableDefCode(CodeMaster.VariableDefCode.USER_CODE_OPERATION_START);
			userCodeOperationStart.setDataType(CodeMaster.DataType.STRING);
			userCodeOperationStart.setValue(req.processUserInfo.getUserCode());
			wftVariableList.add(userCodeOperationStart);
		}
		// 以下、業務管理項目MapをWftVariableへ変換し、wftVariableListへ追加
		if (isNotEmpty(req.bizInfos)) {
			GetWfmVariableDefListInParam inParam = new GetWfmVariableDefListInParam();
			WfmVariableDef var = new WfmVariableDefImpl();
			var.setCorporationCode(req.corporationCode);
			var.setProcessDefCode(req.processDefCode);
			var.setProcessDefDetailCode(req.processDefDetailCode);
			inParam.setWfmVariableDef(var);
			inParam.setWfUserRole(sessionHolder.getWfUserRole());
			GetWfmVariableDefListOutParam outParam = wf.getWfmVariableDefList(inParam);
			Map<String, String> dataTypeMap = outParam.getWfmVariableDefList().stream().collect(
					 Collectors.toMap(WfmVariableDef::getVariableDefCode, WfmVariableDef::getDataType));
			for (String key: req.bizInfos.keySet()) {
				WftVariable bizInfo = new WftVariableImpl();
				bizInfo.setCorporationCode(req.corporationCode);
				bizInfo.setProcessId(req.processId);
				bizInfo.setProcessDefCode(req.processDefCode);
				bizInfo.setProcessDefDetailCode(req.processDefDetailCode);
				bizInfo.setVariableDefCode(key);
				bizInfo.setDataType(dataTypeMap.get(key));
				bizInfo.setValue(req.bizInfos.get(key));
				wftVariableList.add(bizInfo);
			}
		}

		return wftVariableList;
	}

	/**
	 * WfvRouteをActivityEntityにコンバート
	 * @param wfvRoute
	 * @param wfvRouteNext
	 * @param parentKey
	 */
	private ActivityEntity convert(WfvRoute wfvRoute, WfvRoute wfvRouteNext, String parentKey){
		ActivityEntity activityEntity = new ActivityEntity();

		activityEntity.id                   = wfvRoute.getId();
		activityEntity.corporationCode      = wfvRoute.getCorporationCode();
		activityEntity.processId            = wfvRoute.getProcessId();
		activityEntity.activityId           = wfvRoute.getActivityId();
		activityEntity.activityIdNext       = (wfvRouteNext != null ? wfvRouteNext.getActivityId() : null);
		activityEntity.templateId           = wfvRoute.getTemplateId();
		activityEntity.templateIdNext       = (wfvRouteNext != null ? wfvRouteNext.getTemplateId() : null);
		activityEntity.processDefCode       = wfvRoute.getProcessDefCode();
		activityEntity.processDefDetailCode = wfvRoute.getProcessDefDetailCode();
		activityEntity.activityDefCode      = wfvRoute.getActivityDefCode();
		activityEntity.activityDefName      = wfvRoute.getActivityDefName();
		activityEntity.activityStatus       = wfvRoute.getActivityStatus();
		activityEntity.activityType         = wfvRoute.getActivityType();
		activityEntity.parentKey            = parentKey;
		activityEntity.currentActivity      = this.isCurrentActivity(wfvRoute);
		activityEntity.existChangeDefFlag   = eq(ExistChangeDefFlag.ON, wfvRoute.getExistChangeDefFlag());
		activityEntity.closeActivity        = (wfvRoute.getActivityDate() != null);
		activityEntity.branchStartActivity  = this.isBranchStartActivity(wfvRoute);
		activityEntity.deletableActivity    = this.isDeletableActivity(wfvRoute);
		activityEntity.activityDate         = wfvRoute.getActivityDate();
		// TODO 鷲田さんにいってWfvRouteに加えてもらう
//		activityEntity.changeableActivity   = wfvRoute.getChangeAssignedFlag();
		// 参加者情報をセット
		// 参加者情報は内部にその参加者の代理者一覧も保持
		// なお既に終了済みの参加者の場合、代理者が処理を行っていればその代理者のみを表示します
		// 自身が処理していれば代理設定があっても代理者は表示されません
		activityEntity.assignedUserList     = this.getAssignedUserList(wfvRoute.getWfAssignedTemplateList(), activityEntity.currentActivity);

		return activityEntity;
	}

	private boolean isCurrentActivity(WfvRoute wfvRoute) {
		if (wfvRoute.getActivityId() == null || wfvRoute.getActivityId() < 0L)
			return false;
		return exists(wfvRoute.getActivityStatus(), ActivityStatus.START, ActivityStatus.RUN, ActivityStatus.WAIT);
	}

	private boolean isBranchStartActivity(WfvRoute wfvRoute) {
		return exists(wfvRoute.getActivityType(), ActivityType.PARALLEL_S, ActivityType.WF_LINK_D_S);
	}

	private boolean isDeletableActivity(WfvRoute wfvRoute) {
		if (!StringUtils.equals(ActivityType.NORMAL, wfvRoute.getActivityType())) {
			return false;
		}
		return StringUtils.equals(DeletableActivityFlag.ON, wfvRoute.getDeletableActivityFlag());
	}

	/**
	 * 対象"t"が配列"array"の中に合致するものがあるかを判定.
	 * @param t 比較対象
	 * @param array 比較先対象値(可変長配列)
	 * @return 判定結果 trueなら比較先に含まれている
	 */
	@SuppressWarnings("unchecked")
	private <T extends Comparable<T>> boolean exists(T t, T...array) {
		return Arrays.asList(array).stream().anyMatch(o -> compareTo(t, o) == 0);
	}

	private List<AssignedUserInfo> getAssignedUserList(List<WfAssignedTemplate> wfAssignedTemplateList, boolean isCurrentActivity) {
		// 参加者Map
		// 重複を除くため"会社コード/ユーザコード"をキーにしたMap
		final Map<String, AssignedUserInfo> assignedUserMap = new LinkedHashMap<>();

		// [memo]参加者0人の場合は、階層削除されたアクティビティ
		if (wfAssignedTemplateList != null && wfAssignedTemplateList.size() > 0) {
			for (WfAssignedTemplate tempWfAssignedTemplate : wfAssignedTemplateList) {
				AssignedUserInfo assginedUserInfo = this.createAssignedUserInfo(tempWfAssignedTemplate, isCurrentActivity);
				// マップにつめるためのキーを生成
				String key = String.join(SEPARATOR, assginedUserInfo.corporationCode, assginedUserInfo.userCode);
				if (!assignedUserMap.containsKey(key)) {
					assignedUserMap.put(key, assginedUserInfo);
				}
			}
		}

		return new ArrayList<>(assignedUserMap.values());
	}

	private AssignedUserInfo createAssignedUserInfo(WfAssignedTemplate tempWfAssignedTemplate, boolean isCurrentActivity) {
		// 参加者情報beanを生成
		AssignedUserInfo assignedUserInfo = new AssignedUserInfo();

		assignedUserInfo.assignRoleCode = tempWfAssignedTemplate.getAssignRoleCode();
		assignedUserInfo.assignRoleName = tempWfAssignedTemplate.getAssignRoleName();
		assignedUserInfo.assignedStatus = tempWfAssignedTemplate.getAssignedStatus();
		assignedUserInfo.displayFlag    = !eq(DisplayFlag.OFF, tempWfAssignedTemplate.getAssignmentConstant3());
		assignedUserInfo.belongType     = tempWfAssignedTemplate.getBelongType();
		assignedUserInfo.executionDate  = tempWfAssignedTemplate.getExecutionDate();
		assignedUserInfo.actionName     = tempWfAssignedTemplate.getActionName();
		// 矢印表示
		assignedUserInfo.showArrow      = (isCurrentActivity && assignedUserInfo.executionDate != null);
		// 処理日時が入っていれば承認後
		if (assignedUserInfo.executionDate != null) {
			assignedUserInfo.corporationCode  = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationCodeProxy());
//			assignedUserInfo.corporationName  = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationNameProxy());
//			assignedUserInfo.corporationAddedInfo = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationAddedInfoProxy());
			assignedUserInfo.organizationCode = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationCodeProxy());
			assignedUserInfo.organizationName = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationNameProxy());
//			assignedUserInfo.organizationAddedInfo = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationAddedProxy());
			assignedUserInfo.postCode         = StringUtils.defaultString(tempWfAssignedTemplate.getPostCodeProxy());
			assignedUserInfo.postName         = StringUtils.defaultString(tempWfAssignedTemplate.getPostNameProxy());
//			assignedUserInfo.postAddedInfo    = StringUtils.defaultString(tempWfAssignedTemplate.getPostAddedInfoProxy());
			assignedUserInfo.userCode         = StringUtils.defaultString(tempWfAssignedTemplate.getUserCodeProxy());
			assignedUserInfo.userName         = StringUtils.defaultString(tempWfAssignedTemplate.getUserNameProxy());
			assignedUserInfo.userAddedInfo    = StringUtils.defaultString(tempWfAssignedTemplate.getUserAddedInfoProxy());
			assignedUserInfo.showArrow        = false;
		} else {
			assignedUserInfo.corporationCode  = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationCodeAssigned());
//			assignedUserInfo.corporationName  = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationNameProxy());
//			assignedUserInfo.corporationAddedInfo = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationAddedInfoProxy());
			assignedUserInfo.organizationCode = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationCodeAssigned());
			assignedUserInfo.organizationName = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationNameAssigned());
//			assignedUserInfo.organizationAddedInfo = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationAddedProxy());
			assignedUserInfo.postCode         = StringUtils.defaultString(tempWfAssignedTemplate.getPostCodeAssigned());
			assignedUserInfo.postName         = StringUtils.defaultString(tempWfAssignedTemplate.getPostNameAssigned());
//			assignedUserInfo.postAddedInfo    = StringUtils.defaultString(tempWfAssignedTemplate.getPostAddedInfoProxy());
			assignedUserInfo.userCode         = StringUtils.defaultString(tempWfAssignedTemplate.getUserCode());
			assignedUserInfo.userName         = StringUtils.defaultString(tempWfAssignedTemplate.getUserName());
			assignedUserInfo.userAddedInfo    = StringUtils.defaultString(tempWfAssignedTemplate.getUserAddedInfo());
			assignedUserInfo.showArrow        = true;
		}
		// 代理者をセット
		assignedUserInfo.proxyUserList = this.getProxyUserList(tempWfAssignedTemplate);
		// 代理者のユーザ名を<br/>タグで連結する
		// 画面上にはこれが表示される
		List<String> names = assignedUserInfo.proxyUserList.stream().map(u -> u.getUserName()).collect(Collectors.toList());
		assignedUserInfo.proxyUsers = String.join(SEPARATOR_BR, names);

		return assignedUserInfo;
	}

	private List<UserInfo> getProxyUserList(WfAssignedTemplate tempWfAssignedTemplate) {
		// 代理者一覧Map
		// 重複を除くため"会社コード/ユーザコード"をキーにしたMap
		final Map<String, UserInfo> proxyUserMap = new LinkedHashMap<>();

		// 既に処理が完了している場合、(本来の)承認者と処理者が異なれば処理者のみを代理者として設定
		if (eq(AssignedStatus.END, tempWfAssignedTemplate.getAssignedStatus())) {
			if (!eq(tempWfAssignedTemplate.getCorporationCodeProxy(), tempWfAssignedTemplate.getCorporationCodeOperation())
				|| !eq(tempWfAssignedTemplate.getUserCodeProxy(), tempWfAssignedTemplate.getUserCodeOperation()))
			{
				UserInfo proxyUserInfo = new UserInfo();
				proxyUserInfo.setCorporationCode(StringUtils.defaultString(tempWfAssignedTemplate.getCorporationCodeOperation()));
				proxyUserInfo.setOrganizationCode(StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationCodeOperation()));
				proxyUserInfo.setOrganizationName(StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationNameOperation()));
				proxyUserInfo.setPostCode(StringUtils.defaultString(tempWfAssignedTemplate.getPostCodeOperation()));
				proxyUserInfo.setPostName(StringUtils.defaultString(tempWfAssignedTemplate.getPostNameOperation()));
				proxyUserInfo.setUserCode(StringUtils.defaultString(tempWfAssignedTemplate.getUserCodeOperation()));
				proxyUserInfo.setUserName(StringUtils.defaultString(tempWfAssignedTemplate.getUserNameOperation()));
				proxyUserInfo.setUserAddedInfo(StringUtils.defaultString(tempWfAssignedTemplate.getUserAddedInfoOperation()));

				String key = String.join(SEPARATOR, proxyUserInfo.getCorporationCode(), proxyUserInfo.getUserCode());
				proxyUserMap.put(key, proxyUserInfo);
			}
		} else {
			List<WfAssignedTemplate> proxyList = tempWfAssignedTemplate.getProxyWfAssignedTemplateList();
			if (proxyList != null && proxyList.size() > 0) {
				for (WfAssignedTemplate tempProxy : proxyList) {
					String key = String.join(SEPARATOR,
											StringUtils.defaultString(tempProxy.getCorporationCode()), StringUtils.defaultString(tempProxy.getUserCode()));
					if (!proxyUserMap.containsKey(key)) {
						UserInfo proxyUserInfo = new UserInfo();
						proxyUserInfo.setCorporationCode(StringUtils.defaultString(tempProxy.getCorporationCode()));
						proxyUserInfo.setOrganizationCode(StringUtils.defaultString(tempProxy.getOrganizationCodeOperation()));
						proxyUserInfo.setOrganizationName(StringUtils.defaultString(tempProxy.getOrganizationNameOperation()));
						proxyUserInfo.setPostCode(StringUtils.defaultString(tempProxy.getPostCodeOperation()));
						proxyUserInfo.setPostName(StringUtils.defaultString(tempProxy.getPostNameOperation()));
						proxyUserInfo.setUserCode(StringUtils.defaultString(tempProxy.getUserCode()));
						proxyUserInfo.setUserName(StringUtils.defaultString(tempProxy.getUserName()));
						proxyUserInfo.setUserAddedInfo(StringUtils.defaultString(tempProxy.getUserAddedInfo()));
						proxyUserMap.put(key, proxyUserInfo);
					}
				}
			}
		}

		return new ArrayList<>(proxyUserMap.values());
	}
}
