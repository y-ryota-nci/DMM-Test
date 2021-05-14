package jp.co.nci.iwf.endpoint.wl.wl0037;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActivityListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.BatchProcessingFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DefaultFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.SearchConditionType;
import jp.co.nci.integrated_workflow.common.WfSystemException;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.view.WfvTray;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;
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
public class Wl0037Service extends TrayService {
	/** エンティティ抽出用のFunction */
	private Function<GetActivityListInParam, GetActivityListOutParam> funcSelect;

	@Inject
	private Vd0310Service vd0310Service;
	@Inject
	private Wl0037Repository repository;

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
		final TrayInitResponse res = createTrayInitResponse(req, TrayType.BATCH.toString());
		res.success = true;
		return res;
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

	/**
	 * 一括承認処理
	 * @param req
	 * @return
	 */
	public Wl0037BatchProcessingResponse batchProcessing(Wl0037BatchProcessingRequest req) {
		Wl0037BatchProcessingResponse res = createResponse(Wl0037BatchProcessingResponse.class, req);
		Wl0037Entity entity = null;
		try {
			// 初期処理(vd0310の初期処理を呼び出す)
			Vd0310InitResponse initRes = vd0310Service.init(req);
			if (!initRes.success && isNotEmpty(initRes.alerts)) {
				throw new WfSystemException(initRes.alerts.get(0));
			}

			// 申請番号、件名を取得
			entity = repository.getEntity(req.corporationCode, req.processId);

			// 一括承認対象チェック
			if (eq(BatchProcessingFlag.OFF, initRes.contents.activityDef.getBatchProcessingFlag())) {
				throw new WfSystemException(i18n.getText(MessageCd.MSG0267));
			}

			// アクション実行リクエストを作成
			Vd0310ExecuteRequest execReq = new Vd0310ExecuteRequest();
			execReq.contents = initRes.contents;
			execReq.actionInfo = isEmpty(initRes.contents.actionList) ? null : initRes.contents.actionList.stream().filter(a -> DefaultFlag.ON.equals(a.defaultFlag)).findFirst().orElse(null);
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
		inParam.getSearchConditionList().add(new WfSearchConditionImpl<String>(WfvTray.BATCH_PROCESSING_FLAG, SearchConditionType.EQUAL, CommonFlag.ON));
		inParam.getSearchConditionList().add(new WfSearchConditionImpl<String>("pd." + WfvTray.BATCH_PROCESSING_FLAG, SearchConditionType.EQUAL, CommonFlag.ON));
		return inParam;
	}

}
