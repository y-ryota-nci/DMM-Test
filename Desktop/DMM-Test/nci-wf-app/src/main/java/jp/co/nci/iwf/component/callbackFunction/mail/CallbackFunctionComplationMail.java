package jp.co.nci.iwf.component.callbackFunction.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ApprovalStatus;
import jp.co.nci.integrated_workflow.common.CodeMaster.ProcessStatus;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 完了通知メールのコールバックファンクション。
 * 起案者／入力者へ送信。
 */
public class CallbackFunctionComplationMail extends BaseMailCallbackFunction implements MailVariables {

	/**
	 * メール送信先リストを返す
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 * @return
	 */
	@Override
	protected List<MailEntry> getMailEntry(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		// この操作は状態遷移処理か
		if (!(param instanceof MoveActivityInstanceInParam))
			return null;
		if (!(result instanceof MoveActivityInstanceOutParam))
			return null;

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess proc = out.getProcess();
		final long activityId = in.getActivityId();
		final List<MailEntry> entries = new ArrayList<>();

		// 入力者
		final WfmUser inputUser;
		final String corporationCode = proc.getCorporationCodeStart();
		{
			inputUser = getWfmUserByHisLocale(corporationCode, proc.getUserCodeOperationStart());
			entries.add(toMailEntry(inputUser, activityId, proc, contents));
		}
		// 起票者（入力者と異なっているときだけ）
		{
			final WfmUser toUser = getWfmUserByHisLocale(corporationCode, proc.getUserCodeProxyStart());
			if (!eq(inputUser.getCorporationCode(), toUser.getCorporationCode())
					|| !eq(inputUser.getUserCode(), toUser.getUserCode())) {
				entries.add(toMailEntry(toUser, activityId, proc, contents));
			}
		}

		return entries;
	}

	/**
	 * メールテンプレートのインスタンスを返す
	 * @param toUser
	 * @param activityId
	 * @param proc
	 * @param contents
	 * @return
	 */
	private MailEntry toMailEntry(WfmUser toUser, Long activityId, WftProcess proc, Vd0310Contents contents) {
		final Map<String, String> variables = new HashMap<>();
		variables.put(TO_USER_NAME, toUser.getUserName());
		variables.put(SUBJECT, proc.getSubject());
		variables.put(APPLICATION_NO, proc.getApplicationNo());
		variables.put(USER_NAME_OPERATION_START, proc.getUserNameOperationStart());
		variables.put(ORGANIZATION_NAME_START, proc.getOrganizationNameStart());
		variables.put(PROCESS_DEF_NAME, contents.processDefName);
		variables.put(SCREEN_PROCESS_NAME, contents.screenProcessName);
		variables.put(CORPORATION_CODE, proc.getCorporationCode());
		variables.put(PROCESS_ID, toStr(proc.getProcessId()));
		variables.put(ACTIVITY_ID, toStr(activityId));
		variables.put(TIMESTAMP, toStr(proc.getTimestampUpdated().getTime()));
		return new MailEntry(toUser.getDefaultLocaleCode(), toUser.getMailAddress(), variables);
	}

	/**
	 * メールテンプレートのインスタンスを返す
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 */
	@Override
	protected MailTemplate getMailTemplate(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef) {

		// この操作は状態遷移処理か
		if (!(result instanceof MoveActivityInstanceOutParam))
			return null;

		// ステータスが(end)か否か？
		final MoveActivityInstanceOutParam out = (MoveActivityInstanceOutParam)result;
		final WftProcess process = out.getProcess();
		if (!ApprovalStatus.APPROVED.equals(process.getApprovalStatus()) &&
				!ProcessStatus.END.equals(process.getProcessStatus())) {
			return null;
		}
		// アクション機能定義で第一引数が指定されていれば、それをテンプレートファイル名として扱う
		// テンプレートファイル名が未指定なら、実行されたAPIに合わせた文面のメールテンプレートを自動選択
		final String fileName;
		if (functionDef != null && isNotEmpty(functionDef.getFunctionParameter01())) {
			fileName = functionDef.getFunctionParameter01().trim();
		} else {
			fileName = "NotificationComplation.txt";
		}

		final String corporationCode = param.getWfUserRole().getCorporationCode();
		return new MailTemplate(fileName, corporationCode);
	}

}
