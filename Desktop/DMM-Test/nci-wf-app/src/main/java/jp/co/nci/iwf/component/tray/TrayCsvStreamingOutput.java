package jp.co.nci.iwf.component.tray;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.input.BaseTrayInParam;
import jp.co.nci.integrated_workflow.api.param.output.BaseTrayOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DataType;
import jp.co.nci.integrated_workflow.common.CodeMaster.SelectMode;
import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.endpoint.downloadMonitor.DownloadNotifyService;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * トレイ系画面のCSVダウンロード出力ロジックの基底クラス
 *
 * @param <Object> 検索結果エンティティ
 * @param <P> 検索条件のINパラメータ（IWF APIの引数）
 */
public class TrayCsvStreamingOutput<P extends BaseTrayInParam<?>, O extends BaseTrayOutParam<?>>
		extends MiscUtils
		implements StreamingOutput, CodeBook {

	/** 無視する検索結果列 */
	private Set<String> IGNORE_RESULTS = asSet("PROXY_USER");

	private final Logger log = LoggerFactory.getLogger(getClass());

	/** トレイ設定検索結果 */
	private List<TrayResultDef> defs;
	private Function<P, O> function;
	private P in;
	private TraySearchRequest req;

	/**
	 * コンストラクタ
	 * @param defs トレイ設定検索結果マスタ
	 * @param func IWF APIを呼び出すラムダ式
	 */
	public TrayCsvStreamingOutput(List<TrayResultDef> defs, Function<P, O> func, P in, TraySearchRequest req) {
		this.defs = defs;
		this.function = func;
		this.in = in;
		this.req = req;
	}

	/**
	 * リソース解放
	 */
	private void dispose() {
		this.defs = null;
		this.function = null;
	}

	/**
	 * CSV書き込み処理
	 */
	@Override
	public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
		// ダウンロードモニターへ開始を通知
		final DownloadNotifyService notify = CDI.current().select(DownloadNotifyService.class).get();
		notify.begin();

		// 画面コード or 画面プロセスコードから画面IDを求める
			final TrayRepository repository = CDI.current().select(TrayRepository.class).get();
		final String localeCode = LoginInfo.get().getLocaleCode();
		final String corporationCode = LoginInfo.get().getCorporationCode();
		final String screenProcessCode = (String)req.get("screenProcessCode");
		final String screenCode = (String)req.get("screenCode");
		assert isNotEmpty(corporationCode);
		Long screenId = null;
		if (isNotEmpty(screenCode)) {
			final MwmScreen scn = repository.getMwmScreen(corporationCode, screenCode);
			screenId = scn.getScreenId();
			}
		else if (isNotEmpty(screenProcessCode)) {
			final MwvScreenProcessDef spd = repository.getMwvScreenProcessDef(corporationCode, screenProcessCode, localeCode);
			screenId = spd.screenId;
		}

		// CSVカラム定義を生成
		// 画面IDが指定されていなければ共通部分のみ、指定されていれば共通部分＋パーツ部分をCSVカラム定義とする
		final TrayCsvDef csvDef = new TrayCsvDef(defs, screenId);

		// CSVPrinterはCSV形式でストリームへの書き込みを出来るようにする機能を提供する
		try (CSVPrinter csv = NciCsvFormat.print(new OutputStreamWriter(output, MS932))) {
			// CSVヘッダ
			csv.printRecord(csvDef.toTitles());

			// エラーがあったか（重複ログの抑制用）
			final AtomicBoolean hasError = new AtomicBoolean(false);

			// 実データを抽出するが、ページングはさせない
			in.setSelectMode(SelectMode.DATA);
			in.setRowNo(null);
			in.setRowCount(null);

			// 行データをフェッチするたびに呼び出されるハンドラーに、CSV出力内容を定義
			in.setMapRowHandler(map -> {
				if (!hasError.get()) {
					// 行データがMapにアサインされているので、逐次CSVとしてストリームへ書き出す
					// このとき画面定義があれば、そのユーザデータも抽出してCSVへ追記する
					final List<Object[]> lines = mapToLines(map, csvDef);
					try {
						for (Object[] line : lines) {
							csv.printRecord(line);
						}
					}
					catch (IOException e) {
						// 一度ストリームへの書き込みに失敗したら、後続は必然的にすべて失敗するのが自明である
						log.error(e.getMessage(), e);
						hasError.set(true);
						return false;
					}
				}
				return true;
			});

			// IWF APIを呼び出して検索実行
			function.apply(in);
		}
		finally {
			// ダウンロードモニターへ終了を通知
			notify.end();

			dispose();
		}
	}

	/**
	 * 検索結果の行データMapをCSVの一行へ変換
	 * @param entity 行データを保持するMap（フィールド名がキー）
	 * @return
	 */
	private List<Object[]> mapToLines(Map<String, Object> entity, TrayCsvDef csvDef) {
		String corporationCode = (String)entity.get("CORPORATION_CODE");
		Long processId = toLong(entity.get("PROCESS_ID"));
		List<Object[]> lines = new ArrayList<>();

		// 共通情報（≒WFT_PROCESS）
		final Object[] commons = toCommons(entity);

		// 画面デザイナーの申請文書内容も出力するか？
		if (csvDef.columns.isEmpty()) {
			// 共通情報のみ
			lines.add(commons);
			return lines;
		}
		else {
			// 画面デザイナーの申請文書内容を抽出
			List<UserDataEntity> userDataList = getUserDataList(corporationCode, processId, csvDef);
			if (userDataList.isEmpty()) {
				// 申請内容がなにもなくても共通情報は出力しなければならないので、空情報を追加しておく
				userDataList.add(new UserDataEntity());
			}

			// 共通情報＋申請文書内容をCSVの一行として出力
			for (UserDataEntity userData : userDataList) {
				// 共通情報
				final List<Object> fields = new ArrayList<>();
				for (Object common : commons) {
					fields.add(common);
				}
				// 申請文書内容
				for (String colName : csvDef.columns.keySet()) {
					Object value = userData.values.get(colName);
					fields.add(toCsvValue(DataType.STRING, value));
				}
				lines.add(fields.toArray());
			}
		}
		return lines;
	}

	/** トレイ設定をもとにCSV対象カラムを共通情報から抜き出して配列化、 */
	private Object[] toCommons(Map<String, Object> entity) {
		List<String> values = new ArrayList<>(defs.size());
		for (int i = 0; i < defs.size(); i++) {
			final TrayResultDef def = defs.get(i);
			if (IGNORE_RESULTS.contains(def.businessInfoCode))
				continue;

			final Object value = entity.get(def.businessInfoCode);
			values.add(toCsvValue(def.dataType, value));
		}
		return values.toArray();
	}

	/** 抽出データを業務管理項目のデータ型に沿って整形 */
	private String toCsvValue(String dataType, Object value) {
		if (value == null)
			return "";
		else if (dataType == DataType.DATE)
			return FORMATTER_DATE.get().format((Date)value);
		else if (dataType == DataType.INT)
			return toStr(toBD(value));
		else
			return value.toString();
	}

	/** 画面デザイナで使用しているユーザデータを申請文書内容として抽出し、Mapリストとして返す */
	private List<UserDataEntity> getUserDataList(String corporationCode, Long processId, TrayCsvDef csvDef) {
		if (isEmpty(csvDef.columns.isEmpty())) {
			return new ArrayList<>();
		}
		final String sql = csvDef.sql;
		final Object[] params = { corporationCode, processId };
		try {
			EntityManager em = CDI.current().select(EntityManager.class).get();
			Connection conn = em.unwrap(Connection.class);	// このConnectionはJPAが管理しているので、勝手にクローズしてはダメ
			List<Map<String, Object>> results = NativeSqlUtils.select(conn, sql, params);
			return results.stream()
				.map(r -> new UserDataEntity(r))
				.collect(Collectors.toList());
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	};
}
