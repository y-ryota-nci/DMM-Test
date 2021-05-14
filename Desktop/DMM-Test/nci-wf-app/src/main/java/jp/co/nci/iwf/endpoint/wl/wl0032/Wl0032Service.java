package jp.co.nci.iwf.endpoint.wl.wl0032;

import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetActivityListOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.gadget.GadgetCountService;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;
import jp.co.nci.iwf.component.tray.TrayCsvStreamingOutput;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TrayResultDef;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.component.tray.TrayService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 自案件検索画面サービス
 */
@BizLogic
public class Wl0032Service extends TrayService {
	/** エンティティ抽出用のFunction */
	private Function<GetActivityListInParam, GetActivityListOutParam> funcSelect;

	/** ガジェット件数サービス */
	@Inject private GadgetCountService gadgetCountService;

	/** サービスの初期化 */
	@PostConstruct
	public void initialize() {
		// IWF APIによるエンティティ抽出するラムダ式
		funcSelect = p -> wf.getActivityList(p);
	}

	/**
	 * 画面の初期化レスポンスを生成
	 * @param req
	 * @return
	 */
	public TrayInitResponse init(BaseRequest req) {
		final TrayInitResponse res = createTrayInitResponse(req, TrayType.OWN.toString());
		res.success = true;
		return res;
	}

	/**
	 * 自案件検索処理
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
	 * 自案件検索処理(ガジェット：承認済み)
	 * @param req
	 * @return
	 */
	public BaseTrayResponse searchFromGadgetApproved(TraySearchRequest req) {
		// 検索条件生成
		final GetActivityListInParam in = toSearchInParam(req);
		gadgetCountService.addApprovedConditions(in.getSearchConditionList());

		// 検索実行し、レスポンスを生成
		final BaseTrayResponse res = createSearchResponse(req, funcSelect, in);
		return res;
	}

	/**
	 * CSVダウンロード
	 */
	public StreamingOutput downloadCsv(TraySearchRequest req) {
		// 検索条件
		final GetActivityListInParam in = toSearchInParam(req);

		// トレイ設定検索結果の定義に従って抽出結果を整形し、OutputStreamへ書き込む
		final long trayConfigId = toLong(req.get("trayConfigId"));
		final List<TrayResultDef> defs = getTrayResultDefs(trayConfigId);
		return new TrayCsvStreamingOutput<>(defs, funcSelect, in, req);
	}

	/** リスエストからIWF APIコール用INパラメータを生成 */
	private GetActivityListInParam toSearchInParam(TraySearchRequest req) {
		return createSearchInParam(
				GetActivityListInParam.class,
				GetActivityListInParam.Mode.OWN_PROCESS_TRAY,
				req);
	}
}
