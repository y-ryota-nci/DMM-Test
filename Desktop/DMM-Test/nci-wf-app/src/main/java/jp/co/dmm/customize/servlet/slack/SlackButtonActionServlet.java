package jp.co.dmm.customize.servlet.slack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.dmm.customize.servlet.slack.bean.SlackBean;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetWfmActionListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActivityListOutParam;
import jp.co.nci.integrated_workflow.api.param.output.GetWfmActionListOutParam;
import jp.co.nci.integrated_workflow.common.WfSystemException;
import jp.co.nci.integrated_workflow.model.base.impl.WfmActionImpl;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.authenticate.AuthenticateService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.authenticate.LoginResult;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Repository;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.TrayEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.util.MiscUtils;

@WebServlet("/Slack/VD0310.htmlCallbackFunctionSlackButtonAction")
public class SlackButtonActionServlet extends HttpServlet {

	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;

	/** Vd0310Repository */
	@Inject
	protected Vd0310Repository vd0310Repository;

	/** (画面デザイナー)ユーザデータサービス */
	@Inject
	private UserDataService userDataService;

	/** MwmLookupServiceサービス */
	@Inject
	private MwmLookupService lookupService;

	/** Log出力 */
	@Inject
	private Logger log;

	/** Vd0310Serviceサービス */
	@Inject
	private Vd0310Service vd0310Service;
	/**
	 * Slackからボタンが押下された際の実行処理
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Slackボタン押下パラメータ情報取得
		String payload = request.getParameter("payload");
        HashMap<String, Object> payloadMap = null;
		ObjectMapper mapper = new ObjectMapper();
        payloadMap = mapper.readValue(payload, HashMap.class);
        HashMap<String, String> payloadMapUser = (HashMap<String, String>)payloadMap.get("user");

        log.debug("Slack Log(payload)：" + payload);

        // ボタン押下Slackユーザ名称
        String userChannel = payloadMapUser.get("name");

		SlackBean slackBean = new SlackBean();
        slackBean = mapper.readValue(payload, SlackBean.class);

        // ボタン押下Slackユーザ情報
        String[] param = slackBean.getCallback_id().split("\\|");
		Long processId = Long.valueOf(param[0]);
		String corporationCode = param[1];
		String userCode = param[2];
		String userAddedInfo = param[3];

        // ボタン押下アクション情報
		String[] buttonTypeObj = slackBean.getActions().get(0).getValue().split("\\|");
		String actionName = buttonTypeObj[0];
		String actionType = buttonTypeObj[1];
		String actionCode = buttonTypeObj[2];
		log.debug("Slack Log(actionName)：" + actionName);

		/**
		 * payloadのボタン名が文字化けするので、マスタから再取得する。
		 */
		// 検索条件生成
		jp.co.nci.integrated_workflow.model.base.WfmAction actionCond = new WfmActionImpl();
		actionCond.setCorporationCode(corporationCode);
		actionCond.setActionCode(actionCode);
		actionCond.setActionType(actionType);
		actionCond.setDeleteFlag("0");

		// ユーザロール生成
		WfUserRole actionUserRole = new WfUserRoleImpl();
		actionUserRole.setCorporationCode(corporationCode);
		actionUserRole.setUserCode(userCode);
		actionUserRole.setIpAddress("0:0:0:0:0:0:0:1");

		GetWfmActionListInParam actionIn = new GetWfmActionListInParam();
		actionIn.setWfmAction(actionCond);
		actionIn.setWfUserRole(actionUserRole);
		// 処理呼出
		GetWfmActionListOutParam actionOut = wf.getWfmActionList(actionIn);
		jp.co.nci.integrated_workflow.model.base.WfmAction actionResult = actionOut.getWfmActionList().get(0);
		actionName = actionResult.getActionName();

		// MWM_LOOKUPよりSlackプロパティ取得
		List<MwmLookup> lookupList = lookupService.get("ja", corporationCode, LookupGroupId.SLACK_PROPERTIES);
		String debugMode = lookupList.get(0).getLookupName2();
		String token = lookupList.get(1).getLookupName2();
		String debugTo = lookupList.get(2).getLookupName2();

		// 完了メール送信先指定　debugModeが1の場合は、固定の送信先へ送信する。
		String channel = "";
		if ("1".equals(debugMode)){
			channel = debugTo;
		}
		else{
			channel = "@" + userChannel;
		}

		// 押下されたボタンにより、処理を振り分け、承認処理APIを呼び出す。
		WfvTray tray = null;
		String title = actionName + "ボタンが押下されました。";
		String text = "";
		log.debug("Slack Log(title)：" + title);

		final AuthenticateService auth = AuthenticateService.get();
		final LoginInfo loginInfo = auth.updateSessionIfSuccess(
				LoginResult.AlreadyLoggedIn, "ja", corporationCode, userAddedInfo);

		WfUserRole userRole = new WfUserRoleImpl();
		userRole.setCorporationCode(corporationCode);
		userRole.setUserCode(userCode);
		userRole.setIpAddress("0:0:0:0:0:0:0:1");

		// ---------------------------
		// トレイ照会
		// ---------------------------
		// 引数生成
		GetActivityListInParam in2 = new GetActivityListInParam();
		in2.setMode(GetActivityListInParam.Mode.USER_TRAY);
		in2.setWfUserRole(userRole);
		in2.setProcessId(processId);
		// 処理呼出
		GetActivityListOutParam out2 = wf.getActivityList(in2);
		for (WfvTray obj : out2.getTrayList()){
			if(obj.getProcessId().equals(processId)){
				tray = obj;
				break;
			}
		}
		// トレイが存在しない場合は処理済み扱いとする。
		if(tray == null){
			text = "\"既に処理済みです。" + "\"";
		}
		else{

			//コールバックパラメタ生成（他コールバックが利用するため）
			WfmAction wfmAction = new WfmAction();
			wfmAction.setActionCode(actionCode);
			wfmAction.setActionName(actionName);
			wfmAction.setActionType(actionType);
			ActionInfo saveActionInfo = new ActionInfo(wfmAction,"ja");

			final Vd0310InitResponse res = new Vd0310InitResponse();
			res.contents = new Vd0310Contents();

			// トレイ情報を取得
			final TrayEntity trayEntity = vd0310Repository.getTrayEntity(
					tray.getCorporationCode(),
					tray.getProcessId(),
					tray.getActivityId(),
					tray.getScreenProcessId(),
					null,
					"ja");

			MiscUtils.copyFields(trayEntity, res.contents);

			// エラーフラグ
			boolean errFlg = false;

			EntityManager em = CDI.current().select(EntityManager.class).get();
			Connection conn = em.unwrap(Connection.class);

			try {

				// 初期処理(vd0310の初期処理を呼び出す)
				Vd0310InitRequest initReq = new Vd0310InitRequest();
				initReq.corporationCode = tray.getCorporationCode();
				initReq.processId = tray.getProcessId();
				initReq.activityId = tray.getActivityId();
				initReq.timestampUpdated = tray.getTimestampUpdatedProcessLong();
				initReq.trayType = TrayType.WORKLIST;
				initReq.proxyUser = "";
				Vd0310InitResponse initRes = vd0310Service.init(initReq);

				if (!initRes.success && MiscUtils.isNotEmpty(initRes.alerts)) {
					throw new WfSystemException(initRes.alerts.get(0));
				}

				// 申請番号、件名を取得
//				entity = repository.getEntity(req.corporationCode, req.processId);

				// アクション実行リクエストを作成
				Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
				execReq.contents = initRes.contents;
				execReq.actionInfo = MiscUtils.isEmpty(initRes.contents.actionList) ? null : initRes.contents.actionList.stream().filter(a -> actionType.equals(a.actionType)).findFirst().orElse(null);
				execReq.startUserInfo = initRes.contents.startUserInfo;
				execReq.actionComment = null;

				execReq.additionAttachFileWfList = new ArrayList<>();
				execReq.removeAttachFileWfIdList = new ArrayList<>();
				execReq.additionInformationSharerList = new ArrayList<>();
				execReq.removeInformationSharerList = new ArrayList<>();
				execReq.changeRouteList = new ArrayList<>();
				execReq.runtimeMap = initRes.contents.runtimeMap;
				execReq.approvalRelationList = new ArrayList<>();

				// 状態遷移アクションのデフォルトが存在しない場合はエラーとする
				if (MiscUtils.isEmpty(execReq.actionInfo)) {
					text = "\"状態遷移アクションが設定されていないため処理を継続できません。" + "\n" +
						       "申請番号：" + tray.getApplicationNo() + "\n" +
								"件名：" + tray.getSubject() + "\"";
					errFlg = true;
					res.success = false;
				}

				if(!errFlg) {
					// アクション実行処理(vd0310のアクション実行処理を呼び出す)
					Vd0310ExecuteResponse execRes = (Vd0310ExecuteResponse)vd0310Service.execute(execReq);

					// 入力エラーがあった場合は、エラーとして返す(入力エラーの場合でもsuccessはtrueで返ってくる)
					// クライアント側でハンドリングしたいのでエラーにして返す
					if (execRes.errors != null && !execRes.errors.isEmpty()) {
						text = "\"画面の入力項目にエラーがあるため処理を継続できません。" + "\n" +
							       "申請番号：" + tray.getApplicationNo() + "\n" +
								   "件名：" + tray.getSubject() + "\"";
						errFlg = true;
						res.success = false;
					}else {
						text = "\"正常に処理が完了しました。" + "\n" +
							       "申請番号：" + tray.getApplicationNo() + "\n" +
									"件名：" + tray.getSubject() + "\"";

						// 基本successはtrueで返ってくるはず
						res.success = execRes.success;
					}
				}
				if(res.success) {
					conn.commit();
				}else {
					conn.rollback();
				}
			} catch (Exception e) {
				text = "\"システムエラーが発生しました。" + "\n" +
					       "申請番号：" + tray.getApplicationNo() + "\n" +
							"件名：" + tray.getSubject() + "\"";
				errFlg = true;
				res.success = false;
				try {
					conn.rollback();
				}catch(SQLException ex) {
					log.error("ロールバック失敗");
					res.success = false;
				}
			}

/** 2020/06/22 Slack連携時に画面の承認・差戻などの処理を実行するよう変更のため削除
			final Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
			execReq.contents = res.contents;
			execReq.viewWidth = ViewWidth.LG;
			execReq.actionInfo = saveActionInfo;
			execReq.startUserInfo = res.contents.startUserInfo;
			final RuntimeContext ctx = RuntimeContext.newInstance(execReq);
			userDataService.loadScreenAndUserData(ctx);
			execReq.runtimeMap = ctx.runtimeMap;

			Vd0310Contents contents = new Vd0310Contents();
			contents.screenId = trayEntity.screenId;
			contents.processDefName = tray.getProcessDefName();
			contents.screenProcessName = tray.getProcessDefName();
			contents.activityDefCode = tray.getActivityDefCode();

			final Map<String, Object> handOverParam = new HashMap<>();
			handOverParam.put(Vd0310Contents.class.getSimpleName(), contents);
			handOverParam.put(ActionType.class.getSimpleName(), actionType);
			handOverParam.put(RuntimeContext.class.getSimpleName(), ctx);

			//状態遷移API呼び出し
			if(ActionType.NORMAL.equals(actionType)){
				// 引数生成
				MoveActivityInstanceInParam in4 = new MoveActivityInstanceInParam();
				in4.setWfUserRole(userRole);
				in4.setCorporationCode(tray.getCorporationCode());
				in4.setProcessId(tray.getProcessId());
				in4.setActivityId(tray.getActivityId());
				in4.setActionCode(actionCode);
				in4.setWfUserRoleTransfer(null);
				in4.setSkip(true);
				in4.setHandOverParam(handOverParam);
				in4.setTimestampUpdatedProcess(tray.getTimestampUpdatedProcess());
				try {
					MoveActivityInstanceOutParam out4 = wf.moveActivityInstance(in4);
					wf.getConnection().commit();
					text = "\"正常に処理が完了しました。" + "\n" +
					       "申請番号：" + tray.getApplicationNo() + "\n" +
							"件名：" + tray.getSubject() + "\"";
				} catch (Exception e) {
					text = "\"システムエラーが発生しました。" + "\n" +
						       "申請番号：" + tray.getApplicationNo() + "\n" +
								"件名：" + tray.getSubject() + "\"";
					try {
						log.error(e.getMessage(), e);
						wf.getConnection().rollback();
					} catch (SQLException e1) {
						log.error(e.getMessage(), e);
					}
				}
			//差戻API呼び出し
			} else if(ActionType.SENDBACK.equals(actionType)){
				SendBackActivityInstanceInParam in5 = new SendBackActivityInstanceInParam();
				in5.setWfUserRole(userRole);
				in5.setCorporationCode(tray.getCorporationCode());
				in5.setProcessId(tray.getProcessId());
				in5.setActivityId(tray.getActivityId());
				in5.setActionCode(actionCode);
				in5.setHandOverParam(handOverParam);
				in5.setTimestampUpdatedProcess(tray.getTimestampUpdatedProcess());

				try {
					SendBackActivityInstanceOutParam out4 = wf.sendBackActivityInstance(in5);
					wf.getConnection().commit();
					text = "\"正常に処理が完了しました。" + "\n" +
						       "申請番号：" + tray.getApplicationNo() + "\n" +
								"件名：" + tray.getSubject() + "\"";
					} catch (Exception e) {
						text = "\"システムエラーが発生しました。" + "\n" +
							       "申請番号：" + tray.getApplicationNo() + "\n" +
									"件名：" + tray.getSubject() + "\"";
					try {
						log.error(e.getMessage(), e);
						wf.getConnection().rollback();
					} catch (SQLException e1) {
						log.error(e.getMessage(), e);
					}
				}

			//取消API呼び出し
			} else if(ActionType.CANCEL.equals(actionType)){
				StopProcessInstanceInParam in6 = new StopProcessInstanceInParam();
				in6.setCorporationCode(tray.getCorporationCode());
				in6.setProcessId(tray.getProcessId());
				in6.setActivityId(tray.getActivityId());
				in6.setActionCode(actionCode);
				in6.setWfUserRole(userRole);
				in6.setTimestampUpdatedProcess(tray.getTimestampUpdatedProcess());
				in6.setHandOverParam(handOverParam);

				try {
				// 処理呼出
				StopProcessInstanceOutParam out6 = wf.stopProcessInstance(in6);
				wf.getConnection().commit();
				text = "\"正常に処理が完了しました。" + "\n" +
					       "申請番号：" + tray.getApplicationNo() + "\n" +
							"件名：" + tray.getSubject() + "\"";
				} catch (Exception e) {
					text = "\"システムエラーが発生しました。" + "\n" +
						       "申請番号：" + tray.getApplicationNo() + "\n" +
								"件名：" + tray.getSubject() + "\"";
					try {
						log.error(e.getMessage(), e);
						wf.getConnection().rollback();
					} catch (SQLException e1) {
						log.error(e.getMessage(), e);
					}
				}
			}
2020/06/22 Slack連携時に画面の承認・差戻などの処理を実行するよう変更のため削除 **/
		}

		//処理結果を自分へSlackメッセージ送信する。
		String attachments = "[" +
				"{" +
				"\"callback_id\": \"\"," +
				"\"color\": \"#89ceeb\"," +
				"\"text\": " + text +
				"}" +
				"]";

		attachments = URLEncoder.encode(attachments, "UTF-8");
		title = URLEncoder.encode(title, "UTF-8");
		URL url = new URL("https://slack.com/api/chat.postMessage?as_user=true&token=" + token + "&channel=" + channel
					+ "&text=" + title + "&attachments=" + attachments);

		log.debug("Slack Log(URL)：" + url);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String strLog;
		while((strLog = reader.readLine()) != null){
			log.debug(strLog);
		}
		reader.close();

	}

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
