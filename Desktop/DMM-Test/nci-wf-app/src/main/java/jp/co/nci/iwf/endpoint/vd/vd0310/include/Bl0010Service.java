package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.NotImplementedException;

import jp.co.nci.integrated_workflow.api.param.input.GetActionHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActionHistoryListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.base.WftProcess;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WftProcessEx;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSortOrderImpl;
import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWftProcessInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.mail.MailCodeBook.MailVariables;
import jp.co.nci.iwf.component.mail.MailEntry;
import jp.co.nci.iwf.component.mail.MailService;
import jp.co.nci.iwf.component.mail.MailTemplate;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.BbsAttachFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ProcessBbsInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwtProcessBbsEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;
import jp.co.nci.iwf.jpa.entity.mw.MwtProcessBbs;

/**
 * ブロック：要説明（掲示板）サービス
 */
@BizLogic
public class Bl0010Service extends BaseService implements CodeMaster, CodeBook {
	@Inject private Bl0010Repository repository;
	@Inject private MailService mail;
	@Inject private WfInstanceWrapper wf;

	/**
	 * プロセスIDに紐付くプロセス掲示板情報を返す
	 * @param corporationCode 企業コード
	 * @param processId プロセスID
	 * @return
	 */
	public Bl0010Response getProcessBbs(Bl0010Request req) {
		final Bl0010Response res = createResponse(Bl0010Response.class, req);
		res.processBbsList = getProcessBbs(req.corporationCode, req.processId);
		res.success = true;
		return res;
	}

	/**
	 * プロセスIDに紐付くプロセス掲示板情報を返す
	 * @param corporationCode 企業コード
	 * @param processId プロセスID
	 * @return
	 */
	public List<ProcessBbsInfo> getProcessBbs(String corporationCode, Long processId) {

		// プロセス起動前は紐付けがないので抽出不要
		if (isEmpty(corporationCode) || processId == null)
			return null;

		final List<ProcessBbsInfo> results = new ArrayList<>();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// プロセスIDに紐付くプロセス掲示板情報を抽出し、親プロセス掲示板IDをキーにMap化
		final List<MwtProcessBbsEx> list = repository.getProcessBbs(corporationCode, processId, localeCode);
		final Map<Long, List<MwtProcessBbsEx>> map = list.stream()
				.collect(Collectors.groupingBy(e -> e.processBbsIdUp));

		// プロセスIDに紐付くプロセス掲示板情報を抽出し、プロセス掲示板IDをキーにMap化
		final List<MwtBbsAttachFileWf> listat = repository.getBbsAttachFile(corporationCode, processId);
		final Map<Long, List<MwtBbsAttachFileWf>> mapat = listat.stream()
				.collect(Collectors.groupingBy(e -> e.getProcessBbsId()));

		// 親プロセス掲示板ID＝０がスレッドの起点として、プロセス掲示板情報を画面用に整形（再帰的に）
		final List<MwtProcessBbsEx> threads = map.get(0L);
		if (threads != null) {
			// 投稿日時でソート
			threads.sort((t1, t2) -> compareTo(t1.timestampSubmit, t2.timestampSubmit));

			for (MwtProcessBbsEx thread : threads) {
				final ProcessBbsInfo pb = toProcessBbsInfo(thread, map, mapat);
				results.add(pb);
			}
		}
		return results;
	}

	/** プロセス掲示板情報を画面用に整形（再帰的に） */
	private ProcessBbsInfo toProcessBbsInfo(MwtProcessBbsEx src, final Map<Long, List<MwtProcessBbsEx>> map, final Map<Long, List<MwtBbsAttachFileWf>> mapat) {
		// 変換
		final ProcessBbsInfo pb = new ProcessBbsInfo(src);

		// 削除済みなら記事を「削除済」へ書き換え
		if (eq(DeleteFlag.ON, src.getDeleteFlag())) {
			pb.contents = "(" + i18n.getText(MessageCd.deletedArticle) + ")";
		}

		// この投稿に対する添付ファイルリスト
		final List<MwtBbsAttachFileWf> attachFiles = mapat.get(pb.processBbsId);
		if (eq(DeleteFlag.ON, src.getDeleteFlag()) || attachFiles == null) {
			pb.attachFiles = new ArrayList<>();
		}
		else {
			pb.attachFiles = attachFiles.stream()
					.map(a -> new BbsAttachFileWfInfo(a))
					.collect(Collectors.toList());
		}

		// この投稿に対する返信リスト
		final List<MwtProcessBbsEx> replies = map.get(pb.processBbsId);
		if (replies == null) {
			pb.replies = new ArrayList<>();
		}
		else {
			pb.replies = replies.stream()
					.map(r -> toProcessBbsInfo(r, map, mapat))		// 再帰呼び出し
					.sorted((e1, e2) -> compareTo(e1.timestampSubmit, e2.timestampSubmit))	// 投稿日時でソート
					.collect(Collectors.toList());
		}

		return pb;
	}

	/**
	 * プロセス掲示板の記事を削除
	 * @param contents
	 * @param pb
	 */
	@Transactional
	public Bl0010Response delete(Bl0010Request req) {
		repository.delete(req.processBbsId, sessionHolder.getLoginInfo());

		final Bl0010Response res = createResponse(Bl0010Response.class, req);
		res.processBbsList = getProcessBbs(req.corporationCode, req.processId);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.article));
		res.success = true;
		return res;
	}

	/**
	 * プロセス掲示板の記事を新規投稿
	 * @param req
	 * @param res
	 */
	@Transactional
	public Bl0010Response submit(Bl0010Request req) {
		final Bl0010Response res = createResponse(Bl0010Response.class, req);

		// バリデーション
		String error = validate(req);

		if (isEmpty(error)) {
			final ProcessBbsInfo pb = req.processBbsInfo;
			final String[] belongs = pb.belong.split("@");
			final String organizationCodeSubmit = belongs[0];
			final String postCodeSubmit = belongs[1];

			final MwtProcessBbs e = new MwtProcessBbs();
			e.setContents(pb.contents);
			e.setCorporationCode(req.corporationCode);
			e.setCorporationCodeSubmit(sessionHolder.getLoginInfo().getCorporationCode());
			e.setUserCodeSubmit(sessionHolder.getLoginInfo().getUserCode());
			e.setDeleteFlag(DeleteFlag.OFF);
			e.setOrganizationCodeSubmit(organizationCodeSubmit);
			e.setPostCodeSubmit(postCodeSubmit);
			e.setProcessBbsIdUp(pb.processBbsIdUp);		// 新規スレッドなら親プロセス掲示板IDが 0 になっているはず
			e.setProcessBbsMailType(pb.processBbsMailType);
			e.setProcessId(req.processId);
			e.setTimestampSubmit(timestamp());
			repository.insert(e);

			for (BbsAttachFileWfInfo at : pb.attachFiles) {
				MwtBbsAttachFileWf b = repository.getAttachFile(at.bbsAttachFileWfId);
				if (b != null) {
					b.setProcessBbsId(e.getProcessBbsId());
					b.setDeleteFlag(DeleteFlag.OFF);
					repository.update(b);
				}
			}

			// メール送信
			sendMail(e);

			res.processBbsList = getProcessBbs(req.corporationCode, req.processId);
			res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.article));
			res.success = true;
		}
		else {
			res.addAlerts(error);
			res.success = false;
		}
		return res;
	}

	/** 投稿用バリデーション */
	private String validate(Bl0010Request req) {
		final ProcessBbsInfo pb = req.processBbsInfo;

		// プロセスID
		if (isEmpty(req.corporationCode))
			return i18n.getText(MessageCd.MSG0001, MessageCd.corporationCode);
		if (req.processId == null)
			return i18n.getText(MessageCd.MSG0001, MessageCd.processId);

		// 親記事
		if (pb.processBbsIdUp == null || pb.processBbsIdUp < 0)
			return i18n.getText(MessageCd.MSG0001, MessageCd.parentArticle);
		if (pb.processBbsIdUp > 0) {
			if (repository.get(pb.processBbsIdUp) == null) {
				return i18n.getText(MessageCd.MSG0156, MessageCd.parentArticle);
			}
		}
		// 所属
		if (isEmpty(pb.belong) || pb.belong.indexOf("@") < 0)
			return i18n.getText(MessageCd.MSG0001, MessageCd.affiliation);
		// 通知方法
		if (isEmpty(pb.processBbsMailType))
			return i18n.getText(MessageCd.MSG0001, MessageCd.notificationMethod);
		return null;
	}

	/** 通知メールを送信 */
	private void sendMail(MwtProcessBbs e) {
		final WftProcessEx proc = getWftProcess(e.getCorporationCode(), e.getProcessId());
		final WfmProcessDef pd = getWfmProcessDef(proc);
		final WfvActionHistory activity = getCurrentActivity(e.getProcessId());
		final LoginInfo login = sessionHolder.getLoginInfo();

		// プロセス掲示板メール区分から宛先を求める
		final Set<Bl0010AssignedUser> users = new HashSet<>();
		switch (e.getProcessBbsMailType()) {
		case ProcessBbsMailType.NONE:
			break;
		case ProcessBbsMailType.STARTER_ONLY:
			users.addAll(getStartUsers(e, proc));
			break;
		case ProcessBbsMailType.STARTER_AND_REPLY:
			users.addAll(getStarterAndReplyUsers(e, proc));
			break;
		case ProcessBbsMailType.APPROVED_USERS:
			users.addAll(getAllApprovedUsers(e, proc));
			break;
		case ProcessBbsMailType.ALL_ASSIGNED_USERS:
			users.addAll(getAllAssignedUsers(e, proc));
			break;
		default:
			throw new NotImplementedException("実装されていないプロセス掲示板メール区分です。ProcessBbsMailType=" + e.getProcessBbsMailType());
		}

		// テンプレートを読み込み、置換文字列Mapでプレースホルダーの置換を行ったうえで、指定された送信先へメールを送る
		final MailTemplate template = mail.toTemplate(
				MailTemplateFileName.WF_PROCESS_BBS, login.getCorporationCode());

		for (Bl0010AssignedUser user : users) {
			// 置換文字列Map
			final Map<String, String> variables = new HashMap<>();
			variables.put(MailVariables.SUBJECT, proc.getSubject());
			variables.put(MailVariables.TO_USER_NAME, user.userName);
			variables.put(MailVariables.FROM_USER_NAME, login.getUserName());
			variables.put(MailVariables.APPLICATION_NO, proc.getApplicationNo());
			variables.put(MailVariables.PROCESS_DEF_NAME, pd.getProcessDefName());
			variables.put(MailVariables.USER_NAME_OPERATION_START, proc.getUserNameOperationStart());
			variables.put(MailVariables.ORGANIZATION_NAME_START, proc.getOrganizationNameStart());
			variables.put(MailVariables.CORPORATION_CODE, proc.getCorporationCode());
			variables.put(MailVariables.PROCESS_ID, proc.getProcessId().toString());
			variables.put(MailVariables.ACTIVITY_ID, String.valueOf(activity.getActivityId()));
			variables.put(MailVariables.TIMESTAMP, String.valueOf(proc.getTimestampUpdated().getTime()));

			final MailEntry entry = new MailEntry(
					user.defaultLocaleCode, user.mailAddress, variables);
			mail.send(template, entry);
		}
	}

	/** プロセスインスタンス情報を抽出 */
	private WftProcessEx getWftProcess(String corporationCode, Long processId) {
		final SearchWftProcessInParam in = new SearchWftProcessInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);
		List<WftProcessEx> list = wf.searchWftProcess(in).getResultList();
		return (list.isEmpty() ? null : list.get(0));
	}

	/**
	 * 対象プロセスIDで操作者のアクセスできる最新のアクティビティのトレイ情報を返す
	 * @param processId 対象プロセスID
	 * @return
	 */
	public WfvActionHistory getCurrentActivity(long processId) {
		final GetActionHistoryListInParam in = new GetActionHistoryListInParam();
		// ソート機能(処理日順)
		in.setSortType("");//旧ソート機能は使用しない

		final List<WfSortOrder> sortOrderList = new ArrayList<>();
		{
			final WfSortOrder sort = new WfSortOrderImpl();
			sort.setSortAsNumber(false);
			sort.setSortOrderType(CodeMaster.SortOrderType.DESC);
			sort.setColumnName(WfvActionHistory.EXECUTION_DATE);
			sortOrderList.add(sort);
		}
		in.setSortOrderList(sortOrderList);
		// プロセスID
		in.setProcessId(processId);
		// 処理中の伝票を対象
		in.setExecuting(true);
		// モード
		in.setMode(GetActionHistoryListInParam.Mode.PROCESS_A_HISTORY);//指定伝票承認履歴
		in.setSelectMode(GetActionHistoryListInParam.SelectMode.DATA);	// 実データのみ
		// ユーザー情報セット
		in.setWfUserRole(sessionHolder.getWfUserRole());

		// 実行
		final GetActionHistoryListOutParam out = wf.getActionHistoryList(in);
		final List<WfvActionHistory> list = out.getTrayList();

		// 差し替え
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/** プロセス定義 */
	private WfmProcessDef getWfmProcessDef(WftProcessEx proc) {
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(proc.getCorporationCode());
		in.setProcessDefCode(proc.getProcessDefCode());
		in.setProcessDefDetailCode(proc.getProcessDefDetailCode());
		final List<WfmProcessDef> list = wf.searchWfmProcessDef(in).getProcessDefs();
		return (list.isEmpty() ? null : list.get(0));
	}

	/** 全参加者のユーザマスタを求める */
	private List<Bl0010AssignedUser> getAllAssignedUsers(MwtProcessBbs e, WftProcess proc) {
		boolean approvedActivity = false;
		List<Bl0010AssignedUser> users = repository.getAssignedUser(proc, approvedActivity);
		return users;
	}

	/** 承認済みアクティビティの参加者 */
	private List<Bl0010AssignedUser> getAllApprovedUsers(MwtProcessBbs e, WftProcess proc) {
		// 厳密には「処理済アクティビティ＋現アクティビティ」の参加者。
		// よって処理済みアクティビティで実際には承認しなかった人や、承認してないが現アクティビティの参加者には通知される
		boolean approvedActivity = true;
		List<Bl0010AssignedUser> users = repository.getAssignedUser(proc, approvedActivity);
		return users;
	}

	/** 起案者＋入力者＋親記事投稿者のユーザ情報を返す */
	private Set<Bl0010AssignedUser> getStarterAndReplyUsers(MwtProcessBbs e, WftProcess proc) {
		final Set<Bl0010AssignedUser> users = getStartUsers(e, proc);
		final MwtProcessBbs parent = repository.get(e.getProcessBbsIdUp());
		if (parent != null) {
			Bl0010AssignedUser user = repository.getWfmUser(
					parent.getCorporationCodeSubmit(), parent.getUserCodeSubmit());
			if (user != null)
				users.add(user);
		}
		return users;
	}

	/** 起案者＋入力者のユーザ情報を返す */
	private Set<Bl0010AssignedUser> getStartUsers(MwtProcessBbs e, WftProcess proc) {
		final Set<Bl0010AssignedUser> users = new HashSet<>();
		{
			Bl0010AssignedUser user = repository.getWfmUser(
					proc.getCorporationCode(), proc.getUserCodeOperationStart());
			if (user != null)
					users.add(user);
		}
		{
			Bl0010AssignedUser user = repository.getWfmUser(
					proc.getCorporationCodeOpeStart(), proc.getUserCodeProxyStart());
			if (user != null)
				users.add(user);
		}
		return users;
	}

	/**
	 * ダウンロード処理
	 * @param bbsAttachFileWfId
	 * @return
	 */
	public MwtBbsAttachFileWf download(Long bbsAttachFileWfId) {
		return repository.getAttachFile(bbsAttachFileWfId);
	}

}
