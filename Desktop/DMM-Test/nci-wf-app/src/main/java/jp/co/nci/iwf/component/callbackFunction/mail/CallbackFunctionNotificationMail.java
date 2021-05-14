package jp.co.nci.iwf.component.callbackFunction.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
import jp.co.nci.integrated_workflow.common.CodeMaster.AuthTransferType;
import jp.co.nci.integrated_workflow.model.base.WftAssigned;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.base.impl.WftAssignedImpl;
import jp.co.nci.integrated_workflow.model.custom.WfSkipAction;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAuthTransfer;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAuthTransferInParam;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 次遷移通知メールのコールバックファンクション。
 * 遷移先アクティビティにアサインされたユーザへ送信。
 */
public class CallbackFunctionNotificationMail extends BaseMailCallbackFunction implements MailVariables {


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

		if (result == null) {
			// OUTパラメータがNULLということは、前処理で呼び出されたと思われる。メール送信は必ず後処理でなければならぬ。
			return null;
		}

		final String fileName;

		// アクション機能定義で第一引数が指定されていれば、それをテンプレートファイル名として扱う
		// テンプレートファイル名が未指定なら、実行されたAPIに合わせた文面のメールテンプレートを自動選択
		if (functionDef != null && isNotEmpty(functionDef.getFunctionParameter01())) {
			fileName = functionDef.getFunctionParameter01().trim();
		}
		else {
			final MailActionTypes type = getMailActionType(result);
			switch (type) {
			case CREATE: 	fileName = "NotificationCreate.txt"; break;
			case MOVE:		fileName = "NotificationMove.txt"; break;
			case PULLBACK:	fileName = "NotificationPullback.txt"; break;
			case SENDBACK:	fileName = "NotificationSendback.txt"; break;
			case WITHDRAW:
				// 却下（取消）
				final WftProcess proc = ((StopProcessInstanceOutParam)result).getProcess();
				final LoginInfo login = LoginInfo.get();
				if (eq(login.getCorporationCode(), proc.getCorporationCodeOpeStart())
						&& eq(login.getUserCode(), proc.getUserCodeProxyStart())) {
					// 起票者が廃案を実施した場合、取下げ
					fileName = "NotificationWithdraw.txt";
				} else {
					// 起票者以外が廃案を行った場合、却下
					fileName = "NotificationReject.txt";
				}
				break;
			default:
				// おそらく間違ったアクティビティ／アクションに次遷移通知メールのコールバックをつけているはず
				return null;
			}
		}
		final String corporationCode = param.getWfUserRole().getCorporationCode();
		return new MailTemplate(fileName, corporationCode);
	}

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
	protected List<MailEntry> getMailEntry(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef) {

		List<WftAssigned> assignedList = null;
		List<WfSkipAction> skipActions = null;
		WftProcess proc = null;
		String comment = null;

		// 宛先ユーザを求める
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

		final List<MailEntry> mailEntries = new ArrayList<>();
		for (WftAssigned assigned : assignedList) {
			// スキップ（自動承認）されるアクティビティ下のユーザへは送信しない
			if (isAutoSkipActivity(assigned, skipActions))
				continue;
			// 次の承認者が自分自身なら送信しない（操作者＝宛先は除外）
			if (isSameUser(param.getWfUserRole(), assigned))
				continue;

			// アサインされた承認者を宛先としてエントリ作成
			final MailEntry entry = toMailEntry(assigned, proc, contents, comment);
			if (entry != null) {
				mailEntries.add(entry);

				// 承認者の代理者も宛先リストに追加する
				mailEntries.addAll(getProxyUser(assigned, proc, contents, comment));
			}
		}
		return mailEntries;
	}

	/** 操作者＝アサインされたユーザか？ */
	private boolean isSameUser(WfUserRole ope, WftAssigned assigned) {
		// 操作者＝アサインされたユーザか？
		return eq(ope.getCorporationCode(), assigned.getCorporationCodeAssigned())
				&& eq(ope.getUserCode(), assigned.getUserCodeAssigned());
	}

	/** 代理者を検索 */
	private List<MailEntry> getProxyUser(WftAssigned assigned, WftProcess process, Vd0310Contents contents, String comment) {
		// アサインされたユーザの代理者情報を権限委譲TBLから抽出
		final SearchWfmAuthTransferInParam inParam = new SearchWfmAuthTransferInParam();
		inParam.setCorporationCodeP(assigned.getCorporationCode());
		inParam.setProcessDefCode(assigned.getProcessDefCode());
		inParam.setProcessDefDetailCode(assigned.getProcessDefDetailCode());
		inParam.setCorporationCode(assigned.getCorporationCodeAssigned());
		inParam.setUserCode(assigned.getUserCodeAssigned());
		inParam.setValidDate(MiscUtils.today());
		inParam.setAuthTransferType(new String[]{AuthTransferType.ALL, AuthTransferType.PROCESS_DEF});
		final List<WfmAuthTransfer> authTransferList =
				getWfInstanceWrapper().searchWfmAuthTransfer(inParam).getList();

		// 代理者を宛先としてメールエントリを作成
		final List<MailEntry> list = new ArrayList<>();
		for (WfmAuthTransfer auth: authTransferList) {
			// 代理者の言語コードでユーザマスタを読み直し
			final WfmUser toUser = getWfmUserByHisLocale(auth.getCorporationCode(), auth.getUserCodeTransfer());
			if (toUser != null) {
				final MailEntry entry = toMailEntry(toUser, assigned, process, contents, comment);
				if (entry != null)
					list.add(entry);
			}
		}
		return list;
	}

	/** アサイン情報からメールエントリを生成 */
	private MailEntry toMailEntry(WftAssigned assigned, WftProcess process, Vd0310Contents contents, String comment) {
		// アサインされた承認者の言語コードでユーザマスタを読み直し
		final WfmUser user = getWfmUserByHisLocale(assigned.getCorporationCodeAssigned(), assigned.getUserCodeAssigned());
		return toMailEntry(user, assigned, process, contents, comment);
	}

	/** ユーザ所属情報からメールエントリを生成 */
	private MailEntry toMailEntry(WfmUser toUser, WftAssigned assigned, WftProcess proc, Vd0310Contents contents, String comment) {
		// 依頼元ユーザ名＝操作者。操作者と宛先ユーザの言語コードが異なれば、宛先ユーザの言語コードでマスタを読み直し
		final LoginInfo login = LoginInfo.get();
		String fromUserName = login.getUserName();
		if (!eq(login.getDefaultLocaleCode(), toUser.getDefaultLocaleCode())) {
			final WfmUser fromUser = getWfmUser(login.getCorporationCode(), login.getUserCode(), toUser.getDefaultLocaleCode());
			if (fromUser != null)
				fromUserName = fromUser.getUserName();
		}
		final Map<String, String> variables = new HashMap<>();
		variables.put(TO_USER_NAME, toUser.getUserName());
		variables.put(FROM_USER_NAME, fromUserName);
		variables.put(SUBJECT, proc.getSubject());
		variables.put(APPLICATION_NO, proc.getApplicationNo());
		variables.put(USER_NAME_OPERATION_START, proc.getUserNameOperationStart());
		variables.put(ORGANIZATION_NAME_START, proc.getOrganizationNameStart());
		variables.put(COMMENT, comment);
		variables.put(PROCESS_DEF_NAME, contents.processDefName);
		variables.put(SCREEN_PROCESS_NAME, contents.screenProcessName);
		variables.put(CORPORATION_CODE, proc.getCorporationCode());
		variables.put(PROCESS_ID, toStr(proc.getProcessId()));
		variables.put(ACTIVITY_ID, toStr(assigned.getActivityId()));
		variables.put(TIMESTAMP, toStr(proc.getTimestampUpdated().getTime()));
		// 代理先ユーザへ送信する場合は代理元のユーザIDをパラメータに設定
		if (!MiscUtils.eq(toUser.getUserCode(), assigned.getUserCodeAssigned())) {
			variables.put(PROXY_USER, assigned.getCorporationCodeAssigned() + "_" + assigned.getUserCodeAssigned());
		} else {
			variables.put(PROXY_USER, StringUtils.EMPTY);
		}
		return new MailEntry(toUser.getDefaultLocaleCode(), toUser.getMailAddress(), variables);
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
}
