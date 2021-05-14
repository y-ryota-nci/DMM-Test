package jp.co.nci.iwf.endpoint.vd.vd0312;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.view.WfvAuthTransfer;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActivityDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvAuthTransferInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmActivityDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfvAuthTransferOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.component.route.AssignedUserInfo;
import jp.co.nci.iwf.component.route.ProcessRouteService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 承認ルート取得サービス.
 * 画面の入力内容をもとに承認ルートのシミュレーション結果を取得します。
 */
@BizLogic
public class Vd0312Service extends ProcessRouteService {

	@Inject private ScreenLoadService loader;
	@Inject private UserDataService updator;

	/**
	 * 初期化処理.
	 */
	public Vd0312Response init(Vd0312Request req) {
		// ここでは常にシミュレーションモードにて承認ルートを取得する
		req.simulation = true;
		// シミュレーションのための業務管理項目Mapを設定
		req.bizInfos = this.getBizMap(req);
		// 承認ルート情報を取得
		List<ActivityEntity> routeList = super.getProcessRouteInfo(req);

		// レスポンス生成
		final Vd0312Response res = createResponse(Vd0312Response.class, req);
		res.routeList = routeList;
		res.success = true;
		return res;
	}

	/** 業務管理項目Map取得. */
	private Map<String, String> getBizMap(Vd0312Request req) {
		// contentsは画面ロード時の内容なので、最新の画面内容を反映
		if (req.startUserInfo != null)
			req.contents.startUserInfo = req.startUserInfo;
		if (req.runtimeMap != null)
			req.contents.runtimeMap = req.runtimeMap;

		// BizMap取得のためにDesignerContextを生成
		DesignerContext ctx = RuntimeContext.newInstance(req.contents, req.viewWidth, req.runtimeMap);
		// パーツ定義をDBから取得
		loader.loadScreenParts(req.contents.screenId, ctx);

		return updator.createBusinessInfoMap(ctx);
	}

	/**
	 * ステップ追加処理.
	 */
	public Vd0312Response add(Vd0312Request req) {
		// ステップ追加用のアクティビティ定義を取得
		WfmActivityDef activityDef = this.getAddActivityDef(req.corporationCode, req.processDefCode, req.processDefDetailCode, req.target.activityDefCode);

		// 追加用のActivityEntityを生成
		ActivityEntity addActivity = new ActivityEntity();
		addActivity.corporationCode      = activityDef.getCorporationCode();
		addActivity.processId            = null;
		addActivity.activityId           = null;
		addActivity.templateId           = null;
		addActivity.processDefCode       = activityDef.getProcessDefCode();
		addActivity.processDefDetailCode = activityDef.getProcessDefDetailCode();
		addActivity.activityDefCode      = activityDef.getActivityDefCode();
		addActivity.activityDefName      = activityDef.getActivityDefName();
		addActivity.activityStatus       = null;
		addActivity.activityType         = ActivityType.NORMAL_E;
		addActivity.currentActivity      = false;
		addActivity.existChangeDefFlag   = false;
		addActivity.closeActivity        = false;
		addActivity.branchStartActivity  = false;
		addActivity.deletableActivity    = StringUtils.equals(DeletableActivityFlag.ON, activityDef.getDeletableActivityFlag());
		addActivity.parentKey            = req.target != null ? req.target.parentKey : null;

		// 新しい承認者一覧情報を生成
		List<AssignedUserInfo> newAssignedUserList = this.createAssigneUserList(req.addAssignedUsers);
		// 新しい承認者毎に代理者を設定
		for (AssignedUserInfo assigned : newAssignedUserList) {
			assigned.proxyUserList = this.createProxyUserList(req.corporationCode, req.processDefCode, req.processDefDetailCode, assigned.userCode);
			// 代理者のユーザ名を<br/>タグで連結する
			// 画面上にはこれが表示される
			List<String> names = assigned.proxyUserList.stream().map(u -> u.getUserName()).collect(Collectors.toList());
			assigned.proxyUsers = String.join(SEPARATOR_BR, names);
		}
		addActivity.assignedUserList = newAssignedUserList;

		// ステップ追加されたので下記フラグをONへ
		addActivity.addedActivity        = true;

		// レスポンス生成
		final Vd0312Response res = createResponse(Vd0312Response.class, req);
		res.route = addActivity;
		res.success = true;
		return res;
	}

	/**
	 * 承認者変更処理.
	 * @param req
	 * @return
	 */
	public Vd0312Response change(Vd0312Request req) {
		ActivityEntity changeActivity = req.target;

		// 新しい承認者一覧情報を生成
		List<AssignedUserInfo> newAssignedUserList = this.createAssigneUserList(req.addAssignedUsers);
		// 新しい承認者毎に代理者を設定
		for (AssignedUserInfo assigned : newAssignedUserList) {
			assigned.proxyUserList = this.createProxyUserList(req.corporationCode, req.processDefCode, req.processDefDetailCode, assigned.userCode);
			// 代理者のユーザ名を<br/>タグで連結する
			// 画面上にはこれが表示される
			List<String> names = assigned.proxyUserList.stream().map(u -> u.getUserName()).collect(Collectors.toList());
			assigned.proxyUsers = String.join(SEPARATOR_BR, names);
		}
		changeActivity.assignedUserList = newAssignedUserList;

		// 承認者が変更されたので下記フラグをONへ
		changeActivity.changeActivity = true;

		// レスポンス生成
		final Vd0312Response res = createResponse(Vd0312Response.class, req);
		res.route = changeActivity;
		res.success = true;
		return res;
	}


	/**
	 * ステップ追加用のアクティビティ定義(※)取得.
	 * 追加用のアクティビティがない場合は引数でわたってきたアクティビティ定義コードを持つアクティビティ定義を返す
	 * ※アクティビティ定義コードが「8888888888」とする
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義コード枝番
	 * @return
	 */
	private WfmActivityDef getAddActivityDef(String corporationCode, String processDefCode, String processDefDetailCode, String activityDefCode) {
		SearchWfmActivityDefInParam in = new SearchWfmActivityDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索
		SearchWfmActivityDefOutParam out = wf.searchWfmActivityDef(in);
		List<WfmActivityDef> list = out.getActivityDefs();
		// 戻り値
		WfmActivityDef target = list.stream().filter(e -> StringUtils.equals("8888888888", e.getActivityDefCode())).findFirst().orElse(null);
		if (target == null && isNotEmpty(activityDefCode)) {
			target = list.stream().filter(e -> StringUtils.equals(activityDefCode, e.getActivityDefCode())).findFirst().orElse(null);
		}
		return target;
	}

	/**
	 * 参加者一覧情報の生成.
	 * @param users 画面で選択されたユーザ一覧
	 * @return
	 */
	private List<AssignedUserInfo> createAssigneUserList(List<UserInfo> users) {
		List<AssignedUserInfo> assignedUserList = new ArrayList<AssignedUserInfo>();

		for (UserInfo user: users) {
			// 参加者情報beanを生成
			AssignedUserInfo assignedUserInfo = new AssignedUserInfo();
			assignedUserInfo.assignRoleCode   = null;
			assignedUserInfo.assignRoleName   = null;
			assignedUserInfo.assignedStatus   = null;
			assignedUserInfo.displayFlag      = true;
			assignedUserInfo.belongType       = null;
			assignedUserInfo.executionDate    = null;
			assignedUserInfo.actionName       = null;
			assignedUserInfo.showArrow        = false;
			assignedUserInfo.corporationCode  = StringUtils.defaultString(user.getCorporationCode());
			assignedUserInfo.userCode         = StringUtils.defaultString(user.getUserCode());
			assignedUserInfo.userName         = StringUtils.defaultString(user.getUserName());
			assignedUserInfo.userAddedInfo    = StringUtils.defaultString(user.getUserAddedInfo());
//			assignedUserInfo.corporationName  = StringUtils.defaultString(tempWfAssignedTemplate.getCorporationNameProxy());
//			assignedUserInfo.organizationCode = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationCodeProxy());
//			assignedUserInfo.organizationName = StringUtils.defaultString(tempWfAssignedTemplate.getOrganizationNameProxy());
//			assignedUserInfo.postCode         = StringUtils.defaultString(tempWfAssignedTemplate.getPostCodeProxy());
//			assignedUserInfo.postName         = StringUtils.defaultString(tempWfAssignedTemplate.getPostNameProxy());
//			assignedUserInfo.postAddedInfo    = StringUtils.defaultString(tempWfAssignedTemplate.getPostAddedInfoProxy());
			assignedUserList.add(assignedUserInfo);
		}

		return assignedUserList;
	}

	/**
	 * 代理者一覧を取得.
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義コード枝番
	 * @param userCode 代理元ユーザコード
	 * @return
	 */
	private List<UserInfo> createProxyUserList(String corporationCode, String processDefCode, String processDefDetailCode, String userCode) {
		List<UserInfo> proxyUserList = new ArrayList<UserInfo>();

		// 代理者情報一覧を取得
		List<WfvAuthTransfer> proxyUsers = this.getProxyUsers(corporationCode, processDefCode, processDefDetailCode, userCode);
		for (WfvAuthTransfer proxy: proxyUsers) {
			UserInfo proxyUserInfo = new UserInfo();
			proxyUserInfo.setCorporationCode(StringUtils.defaultString(proxy.getCorporationCode()));
			proxyUserInfo.setUserCode(StringUtils.defaultString(proxy.getUserCodeTransfer()));
			proxyUserInfo.setUserName(StringUtils.defaultString(proxy.getUserNameTransfer()));
			proxyUserInfo.setUserAddedInfo(StringUtils.defaultString(proxy.getUserAddedInfoTransfer()));
			proxyUserList.add(proxyUserInfo);
		}

		return proxyUserList;
	}

	/**
	 * 引数のユーザコードの代理者一覧取得.
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義コード枝番
	 * @param userCode ユーザコード(代理元ユーザコード)
	 * @return
	 */
	private List<WfvAuthTransfer> getProxyUsers(String corporationCode, String processDefCode, String processDefDetailCode, String userCode) {
		SearchWfvAuthTransferInParam in = new SearchWfvAuthTransferInParam();
		in.setCorporationCodeP(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setUserCodeFrom(userCode);
		in.setValid(true);
		in.setValidDate(MiscUtils.today());
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索
		SearchWfvAuthTransferOutParam out = wf.searchWfvAuthTransfer(in);
		return out.getAuthTransferList();
	}
}
