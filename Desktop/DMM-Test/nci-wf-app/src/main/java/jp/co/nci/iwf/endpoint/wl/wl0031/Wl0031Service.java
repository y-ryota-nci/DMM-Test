package jp.co.nci.iwf.endpoint.wl.wl0031;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetProcessHistoryListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ProcessStatus;
import jp.co.nci.integrated_workflow.common.CodeMaster.SearchConditionType;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;
import jp.co.nci.iwf.component.tray.TrayCsvStreamingOutput;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TrayResultDef;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.component.tray.TrayService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 強制変更画面サービス
 */
@BizLogic
public class Wl0031Service extends TrayService {
	/** エンティティ抽出用のFunction */
	private Function<GetProcessHistoryListInParam, GetProcessHistoryListOutParam> funcSelect;

	/** サービスの初期化 */
	@PostConstruct
	public void initialize() {
		// IWF APIによるエンティティ抽出するラムダ式
		funcSelect = p -> wf.getProcessHistoryList(p);
	}

	/**
	 * 画面の初期化レスポンスを生成
	 * @param req
	 * @return
	 */
	public TrayInitResponse init(BaseRequest req) {
		final TrayInitResponse res = createTrayInitResponse(req, TrayType.FORCE.toString());
		res.success = true;
		return res;
	}

	/**
	 * 汎用案件検索処理
	 * @param req
	 * @return
	 */
	public BaseTrayResponse search(TraySearchRequest req) {
		// 検索条件生成
		final GetProcessHistoryListInParam in = toSearchInParam(req);

		// 検索実行し、レスポンスを生成
		final BaseTrayResponse res = createSearchResponse(req, funcSelect, in);
		return res;
	}

	/**
	 * CSVダウンロード
	 */
	public StreamingOutput downloadCsv(TraySearchRequest req) {
		// 検索条件
		final GetProcessHistoryListInParam in = toSearchInParam(req);

		// トレイ設定検索結果の定義に従って抽出結果を整形し、OutputStreamへ書き込む
		final long trayConfigId = toLong(req.get("trayConfigId"));
		final List<TrayResultDef> defs = getTrayResultDefs(trayConfigId);
		return new TrayCsvStreamingOutput<>(defs, funcSelect, in, req);
	}

	/** リスエストからIWF APIコール用INパラメータを生成 */
	private GetProcessHistoryListInParam toSearchInParam(TraySearchRequest req) {
		 GetProcessHistoryListInParam inParam = createSearchInParam(
													GetProcessHistoryListInParam.class,
													GetProcessHistoryListInParam.Mode.CORPORATION_HISTORY,
													req);
		 // 処理中のステータスのみを対象とする
		 List<String> statusList = new ArrayList<>();
		 statusList.add(ProcessStatus.START);
		 statusList.add(ProcessStatus.WAIT);
		 statusList.add(ProcessStatus.RUN);
		 WfSearchCondition<List<String>> cond = new WfSearchConditionImpl<>(WfvActionHistory.PROCESS_STATUS, SearchConditionType.IN, statusList);

		 inParam.getSearchConditionList().add(cond);
		 return inParam;
	}
}
