package jp.co.nci.iwf.component.callbackFunction.mail;

import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.output.CreateProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.MoveActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.PullBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.SendBackActivityInstanceOutParam;
import jp.co.nci.integrated_workflow.api.param.output.StopProcessInstanceOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserByHisLocaleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.component.mail.MailCodeBook;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * メール送信用のWFコールバックファンクションの基底クラス
 */
public abstract class BaseMailCallbackFunction extends BaseCallbackFunction implements MailCodeBook {
	private Logger log = LoggerFactory.getLogger(getClass());

	/** 言語コードを指定してユーザマスタを抽出 */
	protected WfmUser getWfmUser(String corporationCode, String userCode, String localeCode) {
		if (MiscUtils.isEmpty(corporationCode) || MiscUtils.isEmpty(userCode))
			return null;

		// メールは操作者の言語ではなく、受信者の言語で送らないと意味がない。
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setUserCode(userCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setLanguage(localeCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		final List<WfmUser> users = getWfInstanceWrapper().searchWfmUser(in).getUserList();
		return users.isEmpty() ? null : users.get(0);
	}

	/** 対象ユーザのデフォルト言語コードでユーザマスタを抽出  */
	protected WfmUser getWfmUserByHisLocale(String corporationCode, String userCode) {
		// メールは操作者の言語ではなく、受信者の言語で送らないと意味がない。
		// よって、まずWFM_USERを検索し、抽出結果のWFM_USER.DEFAULT_LOCALE_CODEを使って再度 WFV_USERを検索する。
		// これで操作者の言語コードではなく、対象ユーザのデフォルト言語コードでWFM_USERを抽出できる。
		final SearchWfmUserByHisLocaleInParam in = new SearchWfmUserByHisLocaleInParam();
		in.setCorporationCode(corporationCode);
		in.setUserCode(userCode);
		final List<WfmUser> users = getWfInstanceWrapper().searchWfmUserByHisLocale(in).getUserList();
		return users.isEmpty() ? null : users.get(0);
	}

	/** メール送信サービスを返す */
	protected MailService getMailService() {
		return get(MailService.class);
	}

	/**
	 * 業務機能処理実行.
	 *
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 */
	@Override
	public void execute(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef) {

		// OutParamはnullなら、アクション機能定義の前後区分=前だと推定できる。
		// そしてメールは処理の結果を通知するためにメール送信処理されるので、前後区分＝前で行う処理がない
		if (result == null) {
			log.warn("OutParamが null のため、メール送信は行われませんでした。おそらくアクション機能定義の前後区分＝前で定義されていると推察しますが、メール送信は宛先が必要となるので前後区分＝後である必要があります。");
			return;
		}

		// メールテンプレート取得
		final MailTemplate template = getMailTemplate(param, result, actionType, contents, ctx, functionDef);
		if (template == null)
			return;

		// メールの送信先
		final List<MailEntry> entries = getMailEntry(param, result, actionType, contents, ctx, functionDef);
		if (entries == null || entries.isEmpty())
			return;

		// メールサービス
		final MailService service = getMailService();
		for (MailEntry entry : entries) {
			service.send(template, entry);
		}
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
	protected abstract List<MailEntry> getMailEntry(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef);

	/**
	 * メールテンプレートのインスタンスを返す
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 * @return
	 */
	protected abstract MailTemplate getMailTemplate(
			InParamCallbackBase param,
			OutParamCallbackBase result,
			String actionType,
			Vd0310Contents contents,
			RuntimeContext ctx,
			WfvFunctionDef functionDef);


	/** メールテンプレートのインスタンスを返す */
	protected MailActionTypes getMailActionType(OutParamCallbackBase result) {
		if (result == null) {
			throw new InternalServerErrorException("戻り値パラメータがNULLのため、MailActionTypesを特定できません");
		}

		if (result instanceof CreateProcessInstanceOutParam) {
			// 起票
			return MailActionTypes.CREATE;
		}
		else if (result instanceof MoveActivityInstanceOutParam) {
			// 状態遷移
			return MailActionTypes.MOVE;
		}
		else if (result instanceof PullBackActivityInstanceOutParam) {
			// 引き戻し
			return MailActionTypes.PULLBACK;
		}
		else if (result instanceof SendBackActivityInstanceOutParam) {
			// 差戻し
			return MailActionTypes.SENDBACK;
		}
		else if (result instanceof StopProcessInstanceOutParam) {
			// 却下（取消）
			WftProcess proc = ((StopProcessInstanceOutParam)result).getProcess();
			LoginInfo login = LoginInfo.get();
			if (eq(login.getCorporationCode(), proc.getCorporationCodeOpeStart())
					&& eq(login.getUserCode(), proc.getUserCodeProxyStart())) {
				// 起票者が廃案を実施した場合、取下げ
				return MailActionTypes.WITHDRAW;
			} else {
				// 起票者以外が廃案を行った場合、却下
				return MailActionTypes.REJECT;
			}
		}
		else {
			final String name = (result == null ? "null" : result.getClass().getSimpleName());
			throw new InternalServerErrorException(
					"メールテンプレートを特定できません。 OutParamCallbackBase=" + name);
		}
	}
}
