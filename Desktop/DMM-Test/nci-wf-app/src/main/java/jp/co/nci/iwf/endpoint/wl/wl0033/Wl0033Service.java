package jp.co.nci.iwf.endpoint.wl.wl0033;

import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.StreamingOutput;

import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetProcessHistoryListOutParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;
import jp.co.nci.iwf.component.tray.TrayCsvStreamingOutput;
import jp.co.nci.iwf.component.tray.TrayInitResponse;
import jp.co.nci.iwf.component.tray.TrayResultDef;
import jp.co.nci.iwf.component.tray.TraySearchRequest;
import jp.co.nci.iwf.component.tray.TrayService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 汎用案件／案件検索サービス
 */
@BizLogic
public class Wl0033Service extends TrayService {
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
		final TrayInitResponse res = createTrayInitResponse(req, TrayType.ALL.toString());
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
		// 費用計上月は画面上は yyyy/MMで送信されてくるが、DBには yyyyMMで格納されているので、補正処理が必要
		final String from = (String)req.get("processBusinessInfo016From");
		req.put("processBusinessInfo016From", removeSlash(from));
		final String to = (String)req.get("processBusinessInfo016To");
		req.put("processBusinessInfo016To", removeSlash(to));

		return createSearchInParam(
				GetProcessHistoryListInParam.class,
				GetProcessHistoryListInParam.Mode.USER_INFO_SHARER_HISTORY,
				req);
	}

	private String removeSlash(String s) {
		if (s == null)
			return null;
		return s.replaceAll("/", "");
	}
}
