package jp.co.dmm.customize.endpoint.py.py0020;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import jp.co.dmm.customize.endpoint.py.PayInfCodeBook;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActivityListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.SearchConditionType;
import jp.co.nci.integrated_workflow.common.WfSystemException;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;
import jp.co.nci.iwf.component.tray.TrayConfig;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.component.tray.TrayService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Service;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0000Service;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 一括承認一覧画面のサービス
 */
@BizLogic
public class Py0020Service extends TrayService implements PayInfCodeBook {
	/** エンティティ抽出用のFunction */
	private Function<GetActivityListInParam, GetActivityListOutParam> funcSelect;

//	/** 検索条件 */
//	private Set<String> CONDITIONS = asSet("SUBJECT", "APPLICATION_NO", "APPLICATION_DATE", "ORGANIZATION_CODE_PROCESS");

	@Inject
	private Vd0310Service vd0310Service;
	@Inject
	private Py0020Repository repository;

	/** サービスの初期化 */
	@PostConstruct
	public void initialize() {
		// IWF APIによるエンティティ抽出するラムダ式
		funcSelect = p -> wf.getActivityList(p);
	}

	/** ボタンブロックサービス */
	@Inject protected Bl0000Service bl0000Service;

	/**
	 * 初期化＆初期検索
	 * @param req
	 * @return
	 */
	public TrayInitResponse init(BaseRequest req) {
		final Py0020InitResponse res = new Py0020InitResponse();
		final LoginInfo login = sessionHolder.getLoginInfo();
		copyFieldsAndProperties(createTrayInitResponse(req, TrayType.BATCH.toString()), res);
		res.payMthOptionItems =repository.getOptionItems(login.getCorporationCode(), login.getLocaleCode(), false);
		res.success = true;
		return res;
	}

	/**
	 * 支払実績用のトレイを固定で取得する
	 */
	protected TrayConfig getAccessibleTrayConfig(String trayType) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final TrayConfig config = repository.getTrayConfig(login);
		return config;
	}

	/**
	 * 一括承認一覧検索処理
	 * @param req
	 * @return
	 */
	public BaseTrayResponse search(TraySearchRequest req) {
		// 検索条件生成
		final GetActivityListInParam in = toSearchInParam(req);

		// 検索実行し、レスポンスを生成
		final BaseTrayResponse res = createSearchResponse(req, funcSelect, in);
		return res;
	}

	public Py0020RedirectResponse getScreenCode(Py0020RedirectRequest req) {
		final Py0020RedirectResponse res = createResponse(Py0020RedirectResponse.class, req);
		final Py0020RedirectEntity e = repository.getEntity(req.companyCd, req.payNo);
		if (isEmpty(e)) {
			throw new NotFoundException("支払情報が見つかりません -> companyCd=" + req.companyCd+ " payNo=" + req.payNo);
		}
		res.screenCode = e.screenCode;
		res.advpayFg = e.advpayFg;
		res.success = true;
		return res;
	}

	/**
	 * 一括承認処理
	 * @param req
	 * @return
	 */
	public Py0020ExecuteResponse execute(Py0020ExecuteRequest req) {
		Py0020ExecuteResponse res = createResponse(Py0020ExecuteResponse.class, req);
		Py0020Entity entity = null;
		try {
			// 初期処理(vd0310の初期処理を呼び出す)
			Vd0310InitResponse initRes = vd0310Service.init(req);
			if (!initRes.success && isNotEmpty(initRes.alerts)) {
				throw new WfSystemException(initRes.alerts.get(0));
			}

			// 申請番号、件名を取得
			entity = repository.getEntity(req.corporationCode, req.processId);

			// アクション実行リクエストを作成
			Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
			execReq.contents = initRes.contents;
			execReq.actionInfo = isEmpty(initRes.contents.actionList) ? null : initRes.contents.actionList.stream().filter(a -> eq(req.actionType, a.actionType)).findFirst().orElse(null);
			execReq.startUserInfo = initRes.contents.startUserInfo;
			execReq.actionComment = null;

			execReq.additionAttachFileWfList = new ArrayList<>();
			execReq.removeAttachFileWfIdList = new ArrayList<>();
			execReq.additionInformationSharerList = new ArrayList<>();
			execReq.removeInformationSharerList = new ArrayList<>();
			execReq.changeRouteList = new ArrayList<>();
			execReq.runtimeMap = initRes.contents.runtimeMap;
			execReq.approvalRelationList = new ArrayList<>();

			// アクションが存在しない場合はエラーとする
			if (isEmpty(execReq.actionInfo)) {
				throw new WfSystemException(i18n.getText(MessageCd.MSG0268));
			}

			// アクション実行処理(vd0310のアクション実行処理を呼び出す)
			Vd0310ExecuteResponse execRes = (Vd0310ExecuteResponse)vd0310Service.execute(execReq);

			// 入力エラーがあった場合は、エラーとして返す(入力エラーの場合でもsuccessはtrueで返ってくる)
			// クライアント側でハンドリングしたいのでエラーにして返す
			if (execRes.errors != null && !execRes.errors.isEmpty()) {
				throw new WfSystemException(i18n.getText(MessageCd.MSG0269));
			}
			// 基本successはtrueで返ってくるはず
			res.success = execRes.success;
		} catch (Exception e) {
			String target = "";
			if (entity != null) {
				String applicationNo = isEmpty(entity.applicationNo) ? i18n.getText(MessageCd.noApplicationNo) : entity.applicationNo;
				String subject = isEmpty(entity.subject) ? i18n.getText(MessageCd.noTitle) : entity.subject;
				target = i18n.getText(MessageCd.MSG0270, applicationNo, subject);
			}
			res.alertMessage = String.format("%s<br>%s", toCause(e).getMessage(), target);
			res.success = false;
		}
		return res;
	}

	/** Reflection上のエラーではなく、直接のエラー原因を求める */
	private Throwable toCause(Throwable e) {
		Throwable ex = e;
		while ((ex instanceof UndeclaredThrowableException || ex instanceof ReflectiveOperationException)
				&& ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}

	/** リスエストからIWF APIコール用INパラメータを生成 */
	private GetActivityListInParam toSearchInParam(TraySearchRequest req) {
		GetActivityListInParam inParam = createSearchInParam(
				GetActivityListInParam.class,
				GetActivityListInParam.Mode.USER_TRAY,
				req);
		inParam.getSearchConditionList().add(new WfSearchConditionImpl<String>(WfvTray.BUSINESS_PROCESS_STATUS, SearchConditionType.EQUAL, BusinessProcessStatus.PY401));
//		inParam.getSearchConditionList().add(new WfSearchConditionImpl<String>(WfvTray.BATCH_PROCESSING_FLAG, SearchConditionType.EQUAL, CommonFlag.ON));
//		inParam.getSearchConditionList().add(new WfSearchConditionImpl<String>("pd." + WfvTray.BATCH_PROCESSING_FLAG, SearchConditionType.EQUAL, CommonFlag.ON));
		return inParam;
	}

}
