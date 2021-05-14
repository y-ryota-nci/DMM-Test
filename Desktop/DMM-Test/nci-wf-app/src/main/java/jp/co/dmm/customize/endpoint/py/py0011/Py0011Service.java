package jp.co.dmm.customize.endpoint.py.py0011;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.dmm.customize.endpoint.py.PayInfCodeBook;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.model.view.impl.WfvUserBelongImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Service;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * クレカ明細消込画面サービス
 */
@BizLogic
public class Py0011Service extends BasePagingService implements PayInfCodeBook {

	@Inject private Py0011Repository repository;
	/** 申請・承認画面サービス */
	@Inject private Vd0310Service vd0310Service;
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** (画面デザイナー)画面定義読込サービス */
	@Inject private ScreenLoadService screenLoadService;
	@Inject protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Py0011SearchRequest req) {
		final int allCount = repository.count(req);
		final Py0011SearchResponse res = createResponse(Py0011SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	@Transactional
	public BaseResponse createProcess(Py0011CreateProcessRequest req) {
		final String screenCode = req.screenCode;
		final Map<String, List<Py0011InputEntity>> inputs =
				req.inputs
				.stream()
				.collect(Collectors.groupingBy(e -> String.join("@", e.companyCd, e.payYm.replaceAll("/", ""), e.splrCd, e.usrCd, toStr(e.useDt, "yyyyMM"))));


		final Map<String, Long> screenProcessIds = new HashMap<>();
		for (String key : inputs.keySet()) {
			final List<Py0011InputEntity> entitys = inputs.get(key);
			final String companyCd = entitys.get(0).companyCd;
			final String usrCd = entitys.get(0).usrCd;
			if (!screenProcessIds.containsKey(companyCd)) {
				screenProcessIds.put(entitys.get(0).companyCd, repository.getScreenProcessId(companyCd, screenCode));
			}
			if (isEmpty(screenProcessIds.get(companyCd))) {
				throw new InvalidUserInputException("起票可能な画面定義が存在しません。");
			}

			// 支払申請起票処理(保存)
			final Vd0310InitRequest initReq = new Vd0310InitRequest();
			initReq.screenProcessId = screenProcessIds.get(companyCd);
			initReq.trayType = TrayType.NEW;
			final Vd0310InitResponse initRes = vd0310Service.init(initReq);
			if (!initRes.success && isNotEmpty(initRes.alerts)) {
				throw new InvalidUserInputException(initRes.alerts.get(0));
			}

			// アクション実行リクエストを作成
			Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
			execReq.contents = initRes.contents;
			execReq.actionInfo = isEmpty(initRes.contents.actionList) ? null : initRes.contents.actionList.stream()
					.filter(a -> ActionType.SAVE.equals(a.actionType))
					.findFirst().orElse(null);

			// 保存アクションが存在しない場合はエラーとする
			if (isEmpty(execReq.actionInfo)) {
				throw new InvalidUserInputException("保存アクションが設定されていないため処理を継続できません。");
			}

			execReq.startUserInfo = createStartUserInfo(companyCd, usrCd);
			execReq.actionComment = null;
			execReq.additionAttachFileWfList = new ArrayList<>();
			execReq.removeAttachFileWfIdList = new ArrayList<>();
			execReq.additionInformationSharerList = new ArrayList<>();
			execReq.removeInformationSharerList = new ArrayList<>();
			execReq.changeRouteList = new ArrayList<>();
			execReq.approvalRelationList = new ArrayList<>();

			// クレカ明細更新処理
			repository.update(entitys);

			// WF支払依頼、WF支払依頼明細のトランザクションテーブルへ書き込むデータを読み込む
			final RuntimeContext ctx = RuntimeContext.newInstance(execReq);
			screenLoadService.loadScreenParts(ctx.screenId,  ctx);
			fillUserData("SCR0050".equals(screenCode), execReq.startUserInfo, entitys, ctx);
			execReq.runtimeMap = ctx.runtimeMap;

			// アクション実行処理(vd0310のアクション実行処理を呼び出す)
			// 入力エラーがあった場合は、エラーとして返す(入力エラーの場合でもsuccessはtrueで返ってくる)
			// クライアント側でハンドリングしたいのでエラーにして返す
			final BaseResponse execRes = vd0310Service.execute(execReq);
			if (execRes instanceof Vd0310ExecuteResponse) {
				final Vd0310ExecuteResponse res = (Vd0310ExecuteResponse)execRes;
				if (res.errors != null && !res.errors.isEmpty()) {
					throw new InvalidUserInputException(i18n.getText(MessageCd.MSG0269));
				}
			}
		}

		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		if (res.success) {
			res.successes.add("支払申請を行いました。");
		}

		return res;
	}

	private UserInfo createStartUserInfo(String corporationCode, String userAddedInfo) {
		// ログイン者の使うべき言語コードを決定
		wf.setLocale(sessionHolder.getLoginInfo().getLocaleCode());

		// ユーザ情報
		final List<WfvUserBelongImpl> belongs = getWfvUserBelongList(corporationCode, userAddedInfo);
		if (belongs == null || belongs.isEmpty())
			throw new InvalidUserInputException("ユーザ所属がありません");

		final WfvUserBelongImpl src = belongs.get(0);	// WFV_USER_BELONG.JOB_TYPEでソートされているので、主務が先頭
		final UserInfo userInfo = new UserInfo();

		userInfo.setCorporationCode(src.getCorporationCode());
		userInfo.setOrganizationCode(src.getOrganizationCode());
		userInfo.setOrganizationName(src.getOrganizationName());
		userInfo.setOrganizationCodeUp3(src.getOrganizationCodeUp3());
		userInfo.setOrganizationCode5(src.getOrganizationCode5());
		userInfo.setOrganizationName5(src.getOrganizationName5());
		userInfo.setPostCode(src.getPostCode());
		userInfo.setPostName(src.getPostName());
		userInfo.setUserCode(src.getUserCode());
		userInfo.setUserName(src.getUserName());
		userInfo.setExtendedInfo01(src.getExtendedInfo01());
		userInfo.setSbmtrAddr(src.getSbmtrAddr());
		return userInfo;
	}

	/** ユーザ所属ビュー抽出 */
	private List<WfvUserBelongImpl> getWfvUserBelongList(String corporationCode, String userAddedInfo) {
		final java.sql.Date today = today();
		final SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(corporationCode);
		in.setUserAddedInfo(userAddedInfo);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today);
		in.setValidEndDateOrganization(today);
		in.setValidStartDatePost(today);
		in.setValidEndDatePost(today);
		in.setValidStartDateUser(today);
		in.setValidEndDateUser(today);
		in.setValidStartDateUserBelong(today);
		in.setValidEndDateUserBelong(today);
		in.setOrderBy(new OrderBy[] {new OrderBy(true, WfcUserBelong.JOB_TYPE)});
		return wf.searchWfvUserBelong(in).getUserBelongList();
	}

	private void fillUserData(boolean rcvinsp, UserInfo startUserInfo, List<Py0011InputEntity> entitys, RuntimeContext ctx) {
		// WF支払依頼に設定するデータを読み込み
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		{
			final List<Object> params = new ArrayList<>();
			final String sql = repository.createPaySQL(rcvinsp, startUserInfo, entitys, params);
			final List<UserDataEntity>  userDataList = loader.getUserData(TableName.MWT_PAY, sql, params.toArray());
			tables.put(TableName.MWT_PAY, userDataList);
		}
		// WF支払依頼明細に設定するデータを読み込み
		{
			final List<Object> params = new ArrayList<>();
			final String sql = repository.createPaydtlSQL(rcvinsp, startUserInfo, entitys, params);
			final List<UserDataEntity>  userDataList = loader.getUserData(TableName.MWT_PAYDTL, sql, params.toArray());
			tables.put(TableName.MWT_PAYDTL, userDataList);
		}
		// 読み込んだデータからランタイムMapを生成
		loader.fillUserData(ctx, tables, false);

	}
}
