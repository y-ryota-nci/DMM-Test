package jp.co.nci.iwf.endpoint.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmAuthTransferInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetWfmAuthTransferListInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmAuthTransferInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmAuthTransferInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmAuthTransferOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmAuthTransferOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmAuthTransferOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ApprovalType;
import jp.co.nci.integrated_workflow.common.CodeMaster.AuthTransferType;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmAuthTransfer;
import jp.co.nci.integrated_workflow.model.base.impl.WfmAuthTransferImpl;
import jp.co.nci.integrated_workflow.model.custom.WfmLookup;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.view.WfvAuthTransfer;
import jp.co.nci.integrated_workflow.param.input.SearchWfmLookupListInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvAuthTransferInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.accesslog.AccessLogService;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 代理設定のサービス.
 */
@BizLogic
public class WsSaveService extends BaseService {
	@Inject private WfmLookupService lookup;
	@Inject private MailService mailService;
	@Inject private WfInstanceWrapper wf;
	@Inject private Logger log;
	@Inject private AccessLogService accessLog;

	/**
	 * 初期化.
	 * @param req
	 * @return
	 */
	public WsSaveResponse init(WsSaveRequest req) {
		final WsSaveResponse res = createResponse(WsSaveResponse.class, req);
		// 更新であれば対象のデータを取得
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final Long seqNoAuthTransfer = req.seqNoAuthTransfer;
		if (isNotEmpty(corporationCode) && isNotEmpty(seqNoAuthTransfer)) {
			WfvAuthTransfer target = this.getWfvAuthTransfer(corporationCode, seqNoAuthTransfer);
			// 対象データが取得できない＝既に削除済みなので排他ロックエラーをスローする
			if (target == null) {
				throw new AlreadyUpdatedException();
			}
			res.target = target;
		} else {
			// 文書種別一覧を取得
			res.processDefs = this.getProcessDefOptions(corporationCode);
		}
		res.success = true;
		return res;
	}

	/**
	 * 代理設定の登録.
	 * @param req
	 * @return
	 */
	@Transactional
	public WsSaveResponse regist(WsPersistenceRequest req) {
		// 会社コード
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		// 代理元ユーザ
		final String userCode = isEmpty(req.userCode) ? sessionHolder.getLoginInfo().getUserCode() : req.userCode;
		// 「全ての文書種別」を選択しているか
		final boolean isAllProcessDef = req.processDefCodes.stream().anyMatch(s -> StringUtils.equals("ALL", s));
		// 権限委譲区分を設定
		// 文書種別で「全ての文書種別」を選択していれば「1：全権限」、でなければ「3：プロセス定義」を設定する
		final String authTransferType = isAllProcessDef ? AuthTransferType.ALL : AuthTransferType.PROCESS_DEF;

		// 登録用の代理設定情報一覧
		final List<WfmAuthTransfer> list = new ArrayList<>();
		// 文書種別毎に代理先ユーザ分の代理設定を行っていく
		String corporationCodeP      = null;
		String processDefCode        = null;
		String processDefDetailCode = null;
		for (String val : req.processDefCodes) {
			corporationCodeP     = isAllProcessDef ? null : corporationCode;
			processDefCode       = isAllProcessDef ? null : StringUtils.split(val, "_")[0];
			processDefDetailCode = isAllProcessDef ? null : StringUtils.split(val, "_")[1];
			for (String userCodeTransfer : req.userCodeTransfers) {
				WfmAuthTransfer entity = new WfmAuthTransferImpl();
				entity.setCorporationCode(corporationCode);
				entity.setAuthTransferType(authTransferType);
				// DMMでは決裁区分は「2:すべて」固定
				entity.setApprovalType(ApprovalType.ALL);
				entity.setUserCode(userCode);
				entity.setUserCodeTransfer(userCodeTransfer);
				entity.setCorporationCodeP(corporationCodeP);
				entity.setProcessDefCode(processDefCode);
				entity.setProcessDefDetailCode(processDefDetailCode);
				entity.setActivityDefCode(null);
				entity.setValidStartDate(req.validStartDate);
				entity.setValidEndDate(req.validEndDate);
				list.add(entity);
			}
		}

		// 登録用のInParamを生成
		final InsertWfmAuthTransferInParam in = new InsertWfmAuthTransferInParam();
		in.setWfUserRole(sessionHolder.getWfUserRole());
		for (WfmAuthTransfer entity : list) {
			in.setWfmAuthTransfer(entity);
			// 登録実行
			final InsertWfmAuthTransferOutParam out = wf.insertWfmAuthTransfer(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);
		}

		// 代理者就任通知メールの送信
		boolean requireSendMail = corpProp.getBool(CorporationProperty.AUTH_TRANSFER_NOTIFICATION, false);
		if (requireSendMail) {
			log.info("システムプロパティで代理者通知をする設定になっているため、代理者就任通知メールを送信します。");
			for (WfmAuthTransfer auth : list) {
				sendMail(auth, MailTemplateFileName.AUTH_TRANSFER);
			}
		} else {
			String msg = "システムプロパティで代理者通知をしない設定になっているため、代理者就任通知メールを送信しません。";
			log.info(msg);
			accessLog.appendDetail(msg);
		}

		// 戻りのレスポンスを生成
		final WsSaveResponse res = createResponse(WsSaveResponse.class, req);
//		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.proxyInfo));
		res.success = true;
		return res;
	}

	/**
	 * 代理者の就任／離任通知メールの送信
	 * @param corporationCode 代理元企業コード
	 * @param userCode 代理元ユーザコード
	 * @param auth 代理情報
	 * @param templateFileName メールテンプレートファイル名
	 */
	public void sendMail(WfmAuthTransfer auth, String templateFileName) {
		final WfmUser source = getWfmUser(auth.getCorporationCode(), auth.getUserCode());
		final WfmUser target = getWfmUser(auth.getCorporationCode(), auth.getUserCodeTransfer());
		if (isEmpty(target.getMailAddress())) {
			// メールアドレスがなければメールは送れない
			String msg = String.format(
					"代理者のメールアドレスが未設定のため、代理就任通知メールを送信できませんでした "
					+ "corporationCode=%s userCode=%s userName=%s"
					, target.getCorporationCode(), target.getUserCode(), target.getUserName());
			log.info(msg);
			accessLog.appendDetail(msg);
			return;
		}
		String processDefName = null;
		if (eq(AuthTransferType.ALL, auth.getAuthTransferType())) {
			// 権限移譲区分＝すべてなら、全プロセス定義が代理対象となる。つまり「すべて」だ
			processDefName = lookup.getName(LookupTypeCode.AUTH_TRANSFER_TYPE, AuthTransferType.ALL);
		} else {
			WfmProcessDef pd = getWfmProcessDef(auth);
			processDefName = (pd == null) ? null : pd.getProcessDefName();
		}
		// メールテンプレートの置換文字列Map
		final Map<String, String> variables = new HashMap<>();
		variables.put(MailVariables.SOURCE_USER_ID, source.getUserAddedInfo());
		variables.put(MailVariables.SOURCE_USER_NAME, source.getUserName());
		variables.put(MailVariables.TARGET_USER_ID, target.getUserAddedInfo());
		variables.put(MailVariables.TARGET_USER_NAME, target.getUserName());
		variables.put(MailVariables.VALID_START_DATE, toStr(auth.getValidStartDate(), "yyyy/MM/dd"));
		variables.put(MailVariables.VALID_END_DATE, toStr(auth.getValidEndDate(), "yyyy/MM/dd"));
		variables.put(MailVariables.PROCESS_DEF_NAME, processDefName);

		// テンプレートを読み込み、置換文字列Mapでプレースホルダーの置換を行ったうえで、指定された送信先へメールを送る
		final MailEntry entry = new MailEntry(target, variables, null);
		final MailTemplate template = mailService.toTemplate(templateFileName, source.getCorporationCode());
		mailService.send(template, entry);
	}

	/** プロセス定義抽出 */
	private WfmProcessDef getWfmProcessDef(WfmAuthTransfer auth) {
		String corporationCode = auth.getCorporationCode();
		String processDefCode = auth.getProcessDefCode();
		String processDefDetailCode = auth.getProcessDefDetailCode();
		if (isEmpty(corporationCode) || isEmpty(processDefCode) || isEmpty(processDefDetailCode))
			return null;

		SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		List<WfmProcessDef> procs = wf.searchWfmProcessDef(in).getProcessDefs();
		return procs.isEmpty() ? null : procs.get(0);
	}

	/** ユーザマスタ抽出 */
	private WfmUser getWfmUser(String corporationCode, String userCode) {
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setUserCode(userCode);
		List<WfmUser> users = wf.searchWfmUser(in).getUserList();
		return users.isEmpty() ? null : users.get(0);
	}

	/**
	 * 代理設定の更新.
	 * @param req
	 * @return
	 */
	@Transactional
	public WsSaveResponse update(WsPersistenceRequest req) {
		final String[] keys = req.entry.split("\t");
		final WfmAuthTransfer auth = this.getWfmAuthTransfer(keys[0], toLong(keys[1]));
		if (auth == null)
			throw new AlreadyUpdatedException();

		auth.setValidStartDate(toDate(keys[2], "yyyy/MM/dd"));
		auth.setValidEndDate(toDate(keys[3], "yyyy/MM/dd"));
		auth.setDeleteFlag(DeleteFlag.OFF);

		final UpdateWfmAuthTransferInParam in = new UpdateWfmAuthTransferInParam();
		in.setWfmAuthTransfer(auth);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final UpdateWfmAuthTransferOutParam out = wf.updateWfmAuthTransfer(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		// 戻りのレスポンスを生成
		final WsSaveResponse res = createResponse(WsSaveResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.proxyInfo));
		res.success = true;
		return res;
	}

	/**
	 * 代理設定の削除.
	 * @param req
	 * @return
	 */
	@Transactional
	public WsSaveResponse delete(WsPersistenceRequest req) {
		final String[] keys = req.entry.split("\t");
		final WfmAuthTransfer auth = this.getWfmAuthTransfer(keys[0], toLong(keys[1]));
		if (auth != null) {
			final DeleteWfmAuthTransferInParam in = new DeleteWfmAuthTransferInParam();
			in.setWfmAuthTransfer(auth);
			in.setWfUserRole(sessionHolder.getWfUserRole());
			final DeleteWfmAuthTransferOutParam out = wf.deleteWfmAuthTransfer(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);

			// 代理者離任通知メールの送信
			boolean requireSendMail = corpProp.getBool(CorporationProperty.AUTH_TRANSFER_NOTIFICATION, false);
			if (requireSendMail) {
				log.info("システムプロパティで代理者通知をする設定になっているため、代理者離任通知メールを送信します。");
				sendMail(auth, MailTemplateFileName.AUTH_TRANSFER_DELETE);
			} else {
				String msg = "システムプロパティで代理者通知をしない設定になっているため、代理者離任通知メールを送信しません。";
				log.info(msg);
				accessLog.appendDetail(msg);
			}
		}

		// 戻りのレスポンスを生成
		final WsSaveResponse res = createResponse(WsSaveResponse.class, req);
//		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.proxyInfo));
		res.success = true;
		return res;
	}

	/**
	 * 権限委譲マスタビュー取得.
	 * @param corporationCode 会社コード
	 * @param seqNoAuthTransfer 権限委譲連番
	 * @return 権限委譲マスタ
	 */
	private WfvAuthTransfer getWfvAuthTransfer(String corporationCode, Long seqNoAuthTransfer) {
		final SearchWfvAuthTransferInParam in = new SearchWfvAuthTransferInParam();
		in.setSeqNoAuthTransfer(seqNoAuthTransfer);
		in.setValid(false);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索実行
		final List<WfvAuthTransfer> list = wf.searchWfvAuthTransfer(in).getAuthTransferList();
		return list.stream().findFirst().orElse(null);
	}

	/**
	 * 権限委譲マスタ取得.
	 * @param corporationCode 会社コード
	 * @param seqNoAuthTransfer 権限委譲連番
	 * @return 権限委譲マスタ
	 */
	public WfmAuthTransfer getWfmAuthTransfer(String corporationCode, Long seqNoAuthTransfer) {
		final GetWfmAuthTransferListInParam in = new GetWfmAuthTransferListInParam();
		WfmAuthTransfer cond = new WfmAuthTransferImpl();
		cond.setCorporationCode(corporationCode);
		cond.setSeqNoAuthTransfer(seqNoAuthTransfer);
		cond.setDeleteFlag(DeleteFlag.OFF);
		in.setWfmAuthTransfer(cond);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索実行
		final List<WfmAuthTransfer> list = wf.getWfmAuthTransferList(in).getWfmAuthTransferList();
		return list.stream().findFirst().orElse(null);
	}

	/**
	 * 文書種別(プロセス定義)の選択肢取得.
	 * @param corporationCode 会社コード
	 * @return 文書種別(プロセス定義)の選択肢一覧
	 */
	private List<OptionItem> getProcessDefOptions(String corporationCode) {
		// 戻り値
		final List<OptionItem> optionItems = new ArrayList<>();
		// 先頭行に「全ての文書種別」のオプション追加
		optionItems.addAll( this.getWfmLookup(corporationCode, "ALL_PROCESS_DEF") );
		// 検索条件生成
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[]{new OrderBy(true, WfmProcessDef.PROCESS_DEF_CODE), new OrderBy(false, WfmProcessDef.PROCESS_DEF_DETAIL_CODE)});
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索実行
		wf.searchWfmProcessDef(in).getProcessDefs()
				.stream()
				.map(e -> new OptionItem(
						String.format("%s_%s", e.getProcessDefCode(), e.getProcessDefDetailCode()),
						e.getProcessDefName()))
				.forEach(e -> optionItems.add(e));
		return optionItems;
	}

	private List<OptionItem> getWfmLookup(String corporationCode, String lookupTypeCode) {
		final SearchWfmLookupListInParam in = new SearchWfmLookupListInParam();
		in.setCorporationCode(corporationCode);
		in.setLookupTypeCode(lookupTypeCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 検索実行
		List<WfmLookup> list = wf.searchWfmLookupList(in).getResultList();
		return list.stream().map(e -> new OptionItem(e.getLookupCode(), e.getLookupName())).collect(Collectors.toList());
	}
}
