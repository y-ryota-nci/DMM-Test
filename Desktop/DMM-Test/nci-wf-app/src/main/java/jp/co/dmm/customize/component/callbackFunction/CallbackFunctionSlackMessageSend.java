package jp.co.dmm.customize.component.callbackFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.PullBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.SendBackActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.input.StopProcessInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.output.CreateProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.PullBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SendBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.StopProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.model.base.WftAssigned;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.base.impl.WftAssignedImpl;
import jp.co.nci.integrated_workflow.model.custom.WfSkipAction;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

public class CallbackFunctionSlackMessageSend extends BaseCallbackFunction{
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Slackメッセージ送信処理のメイン処理
	 */
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef){

		if (result == null) {
			// 前処理で呼び出されている場合は、何もせずリターンする。
			return;
		}

		try{

			//プロセス情報取得
			WftProcess proc = getProcessInfo(param, result, actionType, contents, ctx, functionDef);

			// 次の承認ステップに設定されているユーザ情報を取得する。
			List<WftAssigned> assignedList = getUserInfo(param, result, actionType, contents, ctx, functionDef);

			for (WftAssigned assignedUserInfo : assignedList){
				// Slackメッセージ送信処理を呼び出す。
				sendRequestToSlack(assignedUserInfo, proc, actionType, ctx, functionDef);
			}

		//DBアクセス関連のエラーはシステムエラーとする。
		} catch (PersistenceException e) {
			throw new InternalServerErrorException(e.getMessage(), e);
		//その他Slack関連のエラーは正常終了とし、ワーニングLOGを出力する。（Slack宛先無しなどのエラー）
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return;
		}
	}

	private void sendRequestToSlack(WftAssigned assignedUserInfo, WftProcess proc, String actionType, RuntimeContext ctx, WfvFunctionDef functionDef) throws PersistenceException,Exception{

		String title = null;
		String sql = null;
		Query query = null;
		List<Object[]> results = null;
		EntityManager em = get(EntityManager.class);

		//送信先ユーザ情報取得
		sql = "select EXTENDED_INFO_02,USER_ADDED_INFO from WFM_USER where CORPORATION_CODE = ? and USER_CODE = ?";
		query = em.createNativeQuery(sql);
		query.setParameter(1, assignedUserInfo.getCorporationCodeAssigned());
		query.setParameter(2, assignedUserInfo.getUserCodeAssigned());
		results = query.getResultList();
		String channel = "";
		String channelUserAddedInfo = "";
		if(results != null && results.size() > 0){
			channel = "@" + String.valueOf(results.get(0)[0]);
			channelUserAddedInfo = (results.get(0)[1]).toString();
		}

		//MWM_LOOKUPよりSlackプロパティ取得(送信先ユーザの会社を条件として取得)
		sql = "select LOOKUP_ID,LOOKUP_NAME2 from MWM_LOOKUP where CORPORATION_CODE = ? and LOOKUP_GROUP_ID = 'SLACK_PROPERTIES' and LOCALE_CODE = 'ja' and DELETE_FLAG = '0' order by SORT_ORDER";
		query = em.createNativeQuery(sql);
		query.setParameter(1, assignedUserInfo.getCorporationCodeAssigned());
		results = query.getResultList();
		String debugMode = (results.get(0)[1]).toString();
		String token = (results.get(1)[1]).toString();
		String debugTo = (results.get(2)[1]).toString();
		String domainUrl = (results.get(3)[1]).toString();

		//申請書名取得
		sql = "select CORPORATION_CODE,PROCESS_DEF_NAME from WFM_PROCESS_DEF where CORPORATION_CODE = ? and PROCESS_DEF_CODE = ? and PROCESS_DEF_DETAIL_CODE = ?";
		query = em.createNativeQuery(sql);
		query.setParameter(1, functionDef.getCorporationCode());
		query.setParameter(2, functionDef.getProcessDefCode());
		query.setParameter(3, functionDef.getProcessDefDetailCode());
		results = query.getResultList();
		String screenName = "";
		if(results != null && results.size() > 0){
			screenName = (results.get(0)[1]).toString();
		}

		//Slack送信タイトル設定
		if(ActionType.SENDBACK.equals(actionType)){
			title = "【" + screenName  + "】差戻通知";
		}
		else{
			title = "【" + screenName  + "】承認依頼";
		}

		//Slack送信パラメタ設定
		String attachments = "[" +
				"{" +
				"\"callback_id\": \""+ String.valueOf(proc.getProcessId()) + "|"
					+ assignedUserInfo.getCorporationCodeAssigned()  + "|"
					+ assignedUserInfo.getUserCodeAssigned()  + "|"
					+ channelUserAddedInfo +  "\"," +
				"\"color\": \"#89ceeb\"," +
				"\"text\": \"" + "申請番号：" + proc.getApplicationNo()  + "\n"
				+ "件名：" + proc.getSubject()  + "\n"
				+ "申請者：" + proc.getUserNameOperationStart()  + "\n\n";

		//Slack送信本文設定
		//getFunctionParameter01は本文に出力する画面項目のテーブル名、カラム名が定義されている。
		String param = functionDef.getFunctionParameter01();

		String[] outPutObj = param.split("\\|");

		for(int i = 0; i < outPutObj.length; i++){

			List<String> columnList = new ArrayList<String>();
			String [] outPutColumnObj = outPutObj[i].split(",");

			//出力対象項目の論理名を取得し、mapに保持する。
			Map<String,List<String>> map = new HashMap<String,List<String>>();
			sql = "select a.column_name,a.comments,b.DATA_TYPE from user_col_comments a,user_tab_columns b "
				+ "where a.column_name = b.column_name "
				+ "and a.table_name = b.table_name and a.table_name = ?";
			query = em.createNativeQuery(sql);
			query.setParameter(1, outPutColumnObj[0]);
			results = query.getResultList();
			for(int j = 0; j < results.size(); j++){
				map.put((results.get(j)[0]).toString(), new ArrayList<>(Arrays.asList((results.get(j)[1]).toString(),(results.get(j)[2]).toString())));
			}

			//出力対象項目の入力値取得
			sql = "select ";
			for(int j = 1; j < outPutColumnObj.length; j++){

				String[] convObj = outPutColumnObj[j].split("/");
				String columnStr = convObj[0];

				List<String> list = map.get(columnStr);

				//NUMBER型、DATE型はフォーマット変換する
				switch(list.get(1)){
				case "NUMBER":

					//通貨記号付与
					if(convObj.length > 1){

						//"M"が指定されている場合、通貨マスタより記号取得
						if("M".equals(convObj[1])){
							sql += "(SELECT mny.MNY_MRK FROM mny_mst mny WHERE mny.COMPANY_CD = main.CORPORATION_CODE and mny.MNY_CD = main.MNY_CD and mny.DLT_FG = '0') || TO_CHAR(main." + columnStr + ",'FM999G999G999G999G999') as " + columnStr + ",";
						}
						//"M"でない場合、"JPY固定"
						else{
							sql += "(SELECT mny.MNY_MRK FROM mny_mst mny WHERE mny.COMPANY_CD = main.CORPORATION_CODE and mny.MNY_CD = 'JPY' and mny.DLT_FG = '0') || TO_CHAR(main." + columnStr + ",'FM999G999G999G999G999') as " + columnStr + ",";
						}
					}
					else{
						sql += "TO_CHAR(main." + columnStr + ",'FM999G999G999G999G999') as " + columnStr + ",";
					}
					break;
				case "DATE":
					sql += "TO_CHAR(main." + columnStr + ",'YYYY/MM/DD') as " + columnStr + ",";
					break;
				default:
					sql += "main." + columnStr + ",";
				}
				columnList.add(columnStr);
			}
			sql = sql.substring(0, sql.length()-1) + " from " + outPutColumnObj[0] + " main where main.CORPORATION_CODE = ? and main.PROCESS_ID = ? and main.DELETE_FLAG = '0'";
			query = em.createNativeQuery(sql);
			query.setParameter(1, proc.getCorporationCode());
			query.setParameter(2, proc.getProcessId());
			results = query.getResultList();

			//本文生成
			for(int j = 0; j < results.size(); j++){
				for(int k = 0; k < columnList.size(); k++){

					String val = "";
					if(null != results.get(j)[k]){
						val = results.get(j)[k].toString();
					}

					attachments +=
							map.get(columnList.get(k)).get(0) + "：" + val + "\n";
				}
			}
		}

		attachments += "\n";
		attachments += "※この案件のワークフローのリンクは以下の通りです。" +  "\n";
		attachments += domainUrl + "/page/vd/vd0310.html?corporationCode=" + proc.getCorporationCode()
		 																				+ "&processId=" + proc.getProcessId()
		 																				+ "&activityId=" + assignedUserInfo.getActivityId()
		 																				+ "&trayType=WORKLIST&timestampUpdated=" + proc.getTimestampUpdated().getTime()
		 																				//+ "&proxyUser=" + assignedUserInfo.getCorporationCodeAssigned() + "_" + assignedUserInfo.getUserCodeAssigned() + "&from=mail";
		 																				+ "&proxyUser=&from=mail";

		// ---------------------------
		// アクション一覧取得
		// ---------------------------
		// 引数生成
		sql = "SELECT "
				+ "   ac.ACTION_NAME, "
				+ "   ac.ACTION_TYPE, "
				+ "   ac.ACTION_CODE "
				+ " FROM WFM_ACTION_DEF acdef, "
				+ "   WFM_ACTION ac "
				+ " WHERE acdef.CORPORATION_CODE = ac.CORPORATION_CODE "
				+ " AND acdef.ACTION_CODE        = ac.ACTION_CODE "
				+ " AND acdef.CORPORATION_CODE   = ? "
				+ " AND acdef.PROCESS_DEF_CODE   = ? "
				+ " AND acdef.ACTIVITY_DEF_CODE  = ? "
				+ " AND acdef.PROCESS_DEF_DETAIL_CODE  = ? "
				+ " AND ac.ACTION_TYPE          IN('0','2','3') "
				+ " AND acdef.DELETE_FLAG        = '0' "
				+ " AND ac.DELETE_FLAG           = '0' "
				+ " ORDER BY SORT_ORDER ";

		query = em.createNativeQuery(sql);
		query.setParameter(1, functionDef.getCorporationCode());
		query.setParameter(2, functionDef.getProcessDefCode());
		query.setParameter(3, assignedUserInfo.getActivityDefCode());
		query.setParameter(4, functionDef.getProcessDefDetailCode());

		results = query.getResultList();

		//Slack送信パラメタ設定
		attachments +=
				"\",\"actions\": [ ";

		for(int i = 0; i < results.size(); i++){
			attachments +=
					"{" +
				"\"name\": \"aprrove\"," +
				"\"text\": \"" + results.get(i)[0].toString() + "\"," +
				"\"type\": \"button\"," +
				"\"style\": \"default\"," +
				"\"value\": \"" + results.get(i)[0].toString() + "|" + results.get(i)[1].toString() + "|" + results.get(i)[2].toString() + "\"" +
				"},";
		}
		attachments = attachments.substring(0, attachments.length()-1) + "]}]";

		log.debug(attachments);
		attachments = URLEncoder.encode(attachments, "UTF-8");
		title = URLEncoder.encode(title, "UTF-8");

		// デバッグフラグがtrueの場合、プロパティファイルに設定したテスト用チャネルに送信する。
		if ("1".equals(debugMode)){
			channel = debugTo;
		}

		// Slackのメッセージ送信APIのURLを生成する。
		URL url = new URL("https://slack.com/api/chat.postMessage?as_user=true&token=" + token + "&channel=" + channel
				+ "&text=" + title + "&attachments=" + attachments);

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
	 * slackのメッセージを送付するユーザ情報を取得する。
	 * @param param
	 * @param result
	 * @param actionType
	 * @param contents
	 * @param ctx
	 * @param functionDef
	 * @return
	 */
	protected List<WftAssigned> getUserInfo(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef) {

		List<WftAssigned> assignedUserList = new ArrayList<WftAssigned>();

		List<WftAssigned> assignedList = null;
		List<WfSkipAction> skipActions = null;
		WftProcess proc = null;
		String comment = null;

		// メッセージの送信対象ユーザを求める
		if (result instanceof CreateProcessInstanceOutParam) {
			final CreateProcessInstanceOutParam out = (CreateProcessInstanceOutParam)result;
			proc = out.getProcess();
			assignedList = out.getAssignedList();
		}
		else if (result instanceof MoveActivityInstanceOutParam) {
			// 状態遷移
			final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam) result;
			proc = out.getProcess();
			assignedList = out.getAssignedList();
			skipActions = out.getSkipActionList();
			comment = ((MoveActivityInstanceInParam) param).getActionComment();
		}
		else if (result instanceof PullBackActivityInstanceOutParam) {
			// 引戻し
			final PullBackActivityInstanceOutParam out = ((PullBackActivityInstanceOutParam) result);
			proc = out.getProcess();
			assignedList = out.getAssignedList();
			comment = ((PullBackActivityInstanceInParam) param).getActionComment();
		}
		else if (result instanceof SendBackActivityInstanceOutParam) {
			// 差戻し
			final SendBackActivityInstanceOutParam out = ((SendBackActivityInstanceOutParam) result);
			proc = out.getProcess();
			assignedList = out.getAssignedList();
			comment = ((SendBackActivityInstanceInParam) param).getActionComment();
		}
		else if (result instanceof StopProcessInstanceOutParam) {
			// 却下（取消） 処理なし
			final StopProcessInstanceOutParam out = ((StopProcessInstanceOutParam) result);
			proc = out.getProcess();
			assignedList = new ArrayList<>();
			comment = ((StopProcessInstanceInParam) param).getActionComment();
			// 入力者
			String corporationCode = proc.getCorporationCodeStart();
			{
				final WftAssigned assigned = new WftAssignedImpl();
				assigned.setCorporationCodeAssigned(corporationCode);
				assigned.setUserCodeAssigned(proc.getUserCodeOperationStart());
				assignedList.add(assigned);
			}
			// 起票者
			{
				final WftAssigned assigned = new WftAssignedImpl();
				assigned.setCorporationCodeAssigned(corporationCode);
				assigned.setUserCodeAssigned(proc.getUserCodeProxyStart());
				assignedList.add(assigned);
			}
		}
		//Slack送信対象取得
		for (WftAssigned assigned : assignedList) {
			// スキップ（自動承認）されるアクティビティ下のユーザへは送信しない
			if (isAutoSkipActivity(assigned, skipActions)){
				continue;
			}
			// 該当申請書の該当ステップへの送信は行わない
			if (("201808151400".equals(assigned.getProcessDefCode()) && "0000000006".equals(assigned.getActivityDefCode()))
					|| ("PAYNEW".equals(assigned.getProcessDefCode()) && "0000000004".equals(assigned.getActivityDefCode()))
					|| ("PAYNEW".equals(assigned.getProcessDefCode()) && "0000000005".equals(assigned.getActivityDefCode()))
					|| ("PAYCHANGE".equals(assigned.getProcessDefCode()) && "0000000004".equals(assigned.getActivityDefCode()))
					|| ("PAYCHANGE".equals(assigned.getProcessDefCode()) && "0000000005".equals(assigned.getActivityDefCode()))
					){
				continue;
			}
			// 次の承認者が自分自身なら送信しない（操作者＝宛先は除外）
//			if (isSameUser(param.getWfUserRole(), assigned)){
//				continue;
//			}

			assignedUserList.add(assigned);
		}

		return assignedUserList;
	}

	/**
	 * プロセス情報を取得する。
	 * @param param
	 * @param result
	 * @param actionType
	 * @param contents
	 * @param ctx
	 * @param functionDef
	 * @return
	 */
	protected WftProcess getProcessInfo(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef) {

		WftProcess proc = null;

		// 宛先ユーザを求める
		if (result instanceof CreateProcessInstanceOutParam) {
			final CreateProcessInstanceOutParam out = (CreateProcessInstanceOutParam)result;
			proc = out.getProcess();
		}
		else if (result instanceof MoveActivityInstanceOutParam) {
			// 状態遷移
			final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam) result;
			proc = out.getProcess();
		}
		else if (result instanceof PullBackActivityInstanceOutParam) {
			// 引戻し
			final PullBackActivityInstanceOutParam out = ((PullBackActivityInstanceOutParam) result);
			proc = out.getProcess();
		}
		else if (result instanceof SendBackActivityInstanceOutParam) {
			// 差戻し
			final SendBackActivityInstanceOutParam out = ((SendBackActivityInstanceOutParam) result);
			proc = out.getProcess();
		}
		else if (result instanceof StopProcessInstanceOutParam) {
			// 却下（取消） 処理なし
			final StopProcessInstanceOutParam out = ((StopProcessInstanceOutParam) result);
			proc = out.getProcess();
		}

		return proc;
	}

	/** スキップ（自動承認）されるアクティビティか */
	private boolean isAutoSkipActivity(WftAssigned assigned, List<WfSkipAction> skipActions) {
		boolean isAutoSkipActivity = false;
		if (skipActions != null) {
			// スキップ（自動承認）されるアクティビティの場合、メール送信しない
			for (WfSkipAction skipAction : skipActions) {
				if (skipAction.getActivityId().equals(assigned.getActivityId())) {
					isAutoSkipActivity = true;
					break;
				}
			}
		}
		return isAutoSkipActivity;
	}

	/** 操作者＝アサインされたユーザか？ */
	private boolean isSameUser(WfUserRole ope, WftAssigned assigned) {
		// 操作者＝アサインされたユーザか？
		return eq(ope.getCorporationCode(), assigned.getCorporationCodeAssigned())
				&& eq(ope.getUserCode(), assigned.getUserCodeAssigned());
	}
}
