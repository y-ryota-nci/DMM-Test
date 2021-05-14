package jp.co.dmm.customize.component.callbackFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.dmm.customize.component.DmmCodeBook.AssignRoleCodes;
import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.view.WfvAssignableUser;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.integrated_workflow.param.input.SearchAssignableUserListInParam;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRepeater;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

public class CallbackFunctionSlackMessageSend_CNTRCT extends BaseCallbackFunction{
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 変更契約申請で本部長承認時に呼び出されるコールバックファンクション(Slackメッセージ送信)。
	 * 【申請支払予約のある変更契約申請のみ対象】
	 */
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef){

		if (result == null) {
			// 前処理で呼び出されている場合は、何もせずリターンする。
			return;
		}

		// 支払予約がない場合は、何もせずリターンする。
		if(!"1".equals(ctx.runtimeMap.get("CHK1039").getValue())){
			return;
		}

		try{

			// 会社コード
			String companyCd = ctx.runtimeMap.get("TXT0305").getValue();

			//プロセス情報取得
			final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam) result;
			WftProcess proc = out.getProcess();

		    // 送信先ユーザ取得（DMM014：CFO室）
			List<WfvAssignableUser> sendUserList = getAssignableUser(companyCd);

			for (WfvAssignableUser assignedUserInfo : sendUserList){
				// Slackメッセージ送信処理を呼び出す。
				sendRequestToSlack(assignedUserInfo, ctx, proc);
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

	/**
	 * 参加者ロールに紐付くユーザを取得する。（API利用）
	 * @param companyCd
	 * @return
	 */
	public List<WfvAssignableUser> getAssignableUser(String companyCd) {
		SearchAssignableUserListInParam in = new SearchAssignableUserListInParam();
		//検索条件
		in.setCorporationCode(companyCd);
		in.setValidStartDateArd(new java.sql.Date(System.currentTimeMillis()));
		in.setValidEndDateArd(new java.sql.Date(System.currentTimeMillis()));
		in.setAssignRoleCode(AssignRoleCodes.CFO);
		return getWfInstanceWrapper().searchAssignableUserList(in).getAssignableUserList();
	}

	/**
	 * Slack通知
	 * @param assignedUserInfo
	 * @param ctx
	 * @param proc
	 * @return
	 */
	private void sendRequestToSlack(WfvAssignableUser assignedUserInfo, RuntimeContext ctx, WftProcess proc) throws PersistenceException,Exception{

		String title = null;
		String text = null;
		String sql = null;
		Query query = null;
		List<Object[]> results = null;
		EntityManager em = get(EntityManager.class);

		//送信先ユーザ情報取得
		sql = "select EXTENDED_INFO_02,USER_ADDED_INFO from WFM_USER where CORPORATION_CODE = ? and USER_CODE = ?";
		query = em.createNativeQuery(sql);
		query.setParameter(1, assignedUserInfo.getCorporationCodeAssigned());
		query.setParameter(2, assignedUserInfo.getUserCode());
		results = query.getResultList();
		String channel = "";
		if(results != null && results.size() > 0){
			channel = "@" + String.valueOf(results.get(0)[0]);
		}

		//MWM_LOOKUPよりSlackプロパティ取得(送信先ユーザの会社を条件として取得)
		sql = "select LOOKUP_ID,LOOKUP_NAME2 from MWM_LOOKUP where CORPORATION_CODE = ? and LOOKUP_GROUP_ID = 'SLACK_PROPERTIES' and LOCALE_CODE = 'ja' and DELETE_FLAG = '0' order by SORT_ORDER";
		query = em.createNativeQuery(sql);
		query.setParameter(1, assignedUserInfo.getCorporationCodeAssigned());
		results = query.getResultList();
		String debugMode = (results.get(0)[1]).toString();
		String token = (results.get(1)[1]).toString();
		String debugTo = (results.get(2)[1]).toString();


		//取引先情報
		String splrNmKj = "";
		PartsRepeater repeater = (PartsRepeater)ctx.runtimeMap.get("RPT1000");	// 取引先のリピーター
		for (PartsContainerRow detail : repeater.rows) {

			String splrNmKjId = detail.children.stream().filter(id -> id.endsWith("TXT1002")).findFirst().orElseGet(null);
			splrNmKj += ctx.runtimeMap.get(splrNmKjId).getValue()  + "、";
		}
		if (splrNmKj.endsWith("、")) {
			int last = splrNmKj.lastIndexOf("、");
			splrNmKj = splrNmKj.substring(0, last);
		}

		//Slack送信タイトル設定
		title = "【変更契約申請】本部長承認通知";

		//Slack送信本文設定
		text = "申請番号：" + proc.getApplicationNo()  + "\n"
				+ "契約件名：" + proc.getSubject()  + "\n"
				+ "申請者：" + proc.getUserNameOperationStart()  + "\n"
				+ "申請者所属組織：" + proc.getOrganizationNameOpeStart()  + "\n"
				+ "取引先名：" + splrNmKj  + "\n";

		//Slackパラメタ設定
		String attachments = "[" +
				"{" +
				"\"callback_id\": \"\"," +
				"\"color\": \"#89ceeb\"," +
				"\"text\": " + text +
				"}" +
				"]";

		log.debug(title);
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

}
