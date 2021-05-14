package jp.co.nci.iwf.component.tray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.BaseTrayInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetActivityListInParam.SelectMode;
import jp.co.nci.integrated_workflow.api.param.input.GetAuthTransferListInParam;
import jp.co.nci.integrated_workflow.api.param.output.BaseTrayOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.SearchConditionType;
import jp.co.nci.integrated_workflow.common.DateUtil;
import jp.co.nci.integrated_workflow.model.custom.WfSearchCondition;
import jp.co.nci.integrated_workflow.model.custom.WfSortOrder;
import jp.co.nci.integrated_workflow.model.custom.WfUserRole;
import jp.co.nci.integrated_workflow.model.custom.impl.WfSearchConditionImpl;
import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;
import jp.co.nci.iwf.component.HtmlResourceService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.cache.CacheManager;
import jp.co.nci.iwf.component.cache.LocaledCacheHolder;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;

/**
 * トレイ系画面サービスの基底クラス
 */
public abstract class TrayService extends BaseTrayService implements SearchConditionType {
	/** トレイ系画面のリポジトリ */
	@Inject private TrayRepository repository;
	/** HTMLファイルのキャッシュサービス */
	@Inject protected HtmlResourceService htmlCache;
	/** キャッシュマネージャ */
	@Inject private CacheManager cm;
	/** ロガー */
	@Inject private Logger log;

	/** トレイ検索条件のキャッシュ（キー：トレイ設定ID） */
	protected LocaledCacheHolder<Long, List<TrayConditionDef>> cacheConditions;
	/** トレイ検索結果のキャッシュ（キー：トレイ設定ID） */
	protected LocaledCacheHolder<Long, List<TrayResultDef>> cacheResults;

	/** Timestamp型として扱うフィールド名 */
	protected final Set<String> TIMESTAMPS = asSet("TIMESTAMP_UPDATED_PROCESS");
	/** Date型として扱うフィールド名 */
	protected final Set<String> DATES = asSet(
			"START_DATE", "BASE_DATE", "APPLICATION_DATE", "APPROVAL_DATE", "END_DATE"
			, "LAST_MODIFIED_DATE", "LIMIT_DATE", "LIMIT_DATE_PROCESS");
	/** Long型として扱うフィールド名 */
	protected final Set<String> LONGS = asSet("PROCESS_ID");
	/** Integer型として扱うフィールド名 */
	protected final Set<String> INTEGERS = asSet();
	/** 無視する検索条件 */
	private Set<String> IGNORE_CONDITIONS = asSet("PROXY_USER");

	@PostConstruct
	public void init() {
		cacheConditions = cm.newLocaledInstance(CacheInterval.EVERY_10SECONDS);
		cacheResults = cm.newLocaledInstance(CacheInterval.EVERY_10SECONDS);
	}

	@PreDestroy
	public void dispose() {
		cacheConditions.dispose();
		cacheResults.dispose();
	}


	/**
	 * トレイ系画面の初期化レスポンスを生成
	 * @param req リクエスト
	 * @param trayType トレイタイプ
	 * @return
	 */
	protected TrayInitResponse createTrayInitResponse(BaseRequest req, String trayType) {
		if (trayType == null)
			throw new BadRequestException("trayTypeが未指定です");

		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = login.getCorporationCode();
		final String localeCode = login.getLocaleCode();
		final TrayInitResponse res = createResponse(TrayInitResponse.class, req);
		res.trayType = trayType.toString();

		// 文書種別の選択肢
		res.processDefCodes = getProcessDefOptions(corporationCode);
		// 画面の選択肢
		res.selectableScreenCodes = getScreenCodes(corporationCode, localeCode);
		// 画面プロセスの選択肢
		res.selectableScreenProcesses = getScreenProcesses(corporationCode, localeCode);
		// 業務状態の選択肢
		res.businessStatuses = wfmLookup.getOptionItems(true, LookupTypeCode.BUSINESS_PROCESS_STATUS);
		// プロセス状態の選択肢
		res.processStatuses = wfmLookup.getOptionItems(true, LookupTypeCode.PROCESS_STATUS);
		// 代理元ユーザの選択肢
		res.proxyUsers = getProxyUsers();
		// トレイのテンプレート用HTML
		res.trayTemplateHtml = htmlCache.getContents("tray-template.html");

		// ログイン者の参照可能なトレイ設定を求める
		final TrayConfig config = getAccessibleTrayConfig(trayType);
		if (config == null)
			throw new InternalServerErrorException("トレイ設定が1つもありません");

		final long trayConfigId = config.trayConfigId;
		res.config = config;
		res.configConditions = getTrayConditions(trayConfigId);
		res.configResults = getTrayResultDefs(trayConfigId);

		// 画面タイトルにトレイ設定名を付与
		if (isNotEmpty(config.trayConfigName))
			res.title += " : [" + config.trayConfigName + "]";

		return res;
	}

	/** 画面プロセスの選択肢を生成 */
	public List<OptionItem> getScreenProcesses(String corporationCode, String localeCode) {
		final List<MwvScreenProcessDef> sps = repository.getMwmScreenProcess(corporationCode, localeCode);
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll(sps.stream()
				.map(r -> new OptionItem(r.screenProcessCode, r.screenProcessName))
				.collect(Collectors.toList()));
		return items;
	}

	/** 画面の選択肢を生成 */
	public List<OptionItem> getScreenCodes(String corporationCode, String localeCode) {
		final List<MwvScreen> screens = repository.getMwmScreens(corporationCode, localeCode);
		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);
		items.addAll(screens.stream()
				.map(r -> new OptionItem(r.screenCode, r.screenName))
				.collect(Collectors.toList()));
		return items;
	}

	/**
	 * ソート条件リストを生成
	 * @param req
	 * @return
	 */
	protected List<WfSortOrder> toWfSortList(TraySearchRequest req) {
		final List<WfSortOrder> sorts = new ArrayList<>();
		String sortColumn = (String)req.get("sortColumn");
		Boolean sortAsc = toBool(req.get("sortAsc").toString());
		if (isNotEmpty(sortColumn)) {
			String[] sortColumns = sortColumn.split(",\\s*");
			for (String col : sortColumns){
				sorts.add(toWfSort(col, sortAsc));
			}
		}
		return sorts;
	}

	/** トレイ設定検索条件マスタをキャッシュから求めて返す */
	protected List<TrayConditionDef> getTrayConditions(long trayConfigId) {
		// null判定を2回しているのは、synchronizedの遅延を最小にするため
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<TrayConditionDef> conditions = cacheConditions.get(localeCode, trayConfigId);
		if (conditions == null) {
			synchronized (cacheConditions) {
				conditions = cacheConditions.get(localeCode, trayConfigId);
				if (conditions == null) {
					conditions = repository.getTrayConditions(trayConfigId, localeCode);
					if (conditions.isEmpty()) {
						log.warn("トレイ設定検索条件が０件です。trayConfigId={}", trayConfigId);
					}
					cacheConditions.put(localeCode, trayConfigId, conditions);
				}
			}
		}
		return conditions;
	}

	/** トレイ設定検索結果マスタをキャッシュから求めて返す */
	protected List<TrayResultDef> getTrayResultDefs(long trayConfigId) {
		// null判定を2回しているのは、synchronizedの遅延を最小にするため
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<TrayResultDef> results = cacheResults.get(localeCode, trayConfigId);
		if (results == null) {
			synchronized (cacheResults) {
				results = cacheResults.get(localeCode, trayConfigId);
				if (results == null) {
					results = repository.getTrayResults(trayConfigId, localeCode);
					if (results.isEmpty()) {
						log.warn("トレイ設定検索結果が０件です。trayConfigId={}", trayConfigId);
					}
					cacheResults.put(localeCode, trayConfigId, results);
				}
			}
		}
		return results;
	}

	/** ログイン者の参照可能なトレイ設定を求める */
	protected TrayConfig getAccessibleTrayConfig(String trayType) {
		final LoginInfo login = sessionHolder.getLoginInfo();
		final TrayConfig config = repository.getTrayConfig(trayType, login);
		return config;
	}

	/** 代理元ユーザの選択肢 */
	protected List<OptionItem> getProxyUsers() {
		final GetAuthTransferListInParam in = new GetAuthTransferListInParam();
		in.setValid(true);
		in.setValidDate(DateUtil.getSysDate());
		in.setCorporationCodeP(sessionHolder.getLoginInfo().getCorporationCode());
		in.setUserCodeTo(sessionHolder.getLoginInfo().getUserCode());
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		wf.getAuthTransferList(in).getWfUserRoleList()
				.stream()
				.map(e -> new OptionItem(
						String.format("%s_%s", e.getCorporationCode(), e.getUserCode()),
						e.getUserName()))
				.forEach(e -> items.add(e));

		return items;
	}


	/**
	 * 絞り込み条件を生成
	 * @param req
	 * @return
	 */
	protected List<WfSearchCondition<?>> toWfSearchCondition(TraySearchRequest req) {
		@SuppressWarnings("unchecked")
		final List<Map<String, String>> configConditions = (List<Map<String, String>>)req.get("configConditions");
		final List<WfSearchCondition<?>> conds = new ArrayList<>();
		for (Map<String, String> c : configConditions) {
			final String businessInfoCode = c.get("businessInfoCode");
			final String id = toCamelCase(businessInfoCode);
			final String conditionMatchType = c.get("conditionMatchType");
			final String columnName = toColumnName(businessInfoCode);

			// 別処理にて検索条件が作成されるため無視
			if (IGNORE_CONDITIONS.contains(businessInfoCode)) {
				continue;
			}
			if (eq(WfvActionHistory.PROCESS_DEF_CODE, businessInfoCode)) {
				// 文書種別（プロセス定義コード）
				final Object value = getRequestValue(req, businessInfoCode, id);
				if (value == null) {
					continue;
				}
				// プロセス定義明細コードまで指定されている場合は、"_"で区切られている
				final String[] values = value.toString().split("_");
				final String searchConditionType = toWfSearchConditionType(conditionMatchType);
				if (values.length > 0) {
					conds.add(new WfSearchConditionImpl<>(
							WfvActionHistory.PROCESS_DEF_CODE, searchConditionType, values[0]));
				}
				if (values.length > 1) {
					// プロセス定義明細コード（指定があれば）
					conds.add(new WfSearchConditionImpl<>(
							WfvActionHistory.PROCESS_DEF_DETAIL_CODE, searchConditionType, values[1]));
				}
			}
			else if (eq(ConditionMatchType.RANGE, conditionMatchType)) {
				// FROM
				final Object from = getRequestValue(req, businessInfoCode, id + "From");
				if (isNotEmpty(from)) {
					conds.add(new WfSearchConditionImpl<>(columnName, GRATER_EQUAL, from));
				}
				// TO
				final Object to = getRequestValue(req, businessInfoCode, id + "To");
				if (isNotEmpty(to)) {
					conds.add(new WfSearchConditionImpl<>(columnName, LESS_EQUAL, to));
				}
			}
			else {
				// RANGE以外の演算子
				final Object value = getRequestValue(req, businessInfoCode, id);
				if (isNotEmpty(value)) {
					final String searchConditionType = toWfSearchConditionType(conditionMatchType);
					conds.add(new WfSearchConditionImpl<>(columnName, searchConditionType, value));
				}
			}
		};
		return conds;
	}

	/** 業務管理項目コードをカラム名に変換 */
	protected String toColumnName(String businessInfoCode) {
		return businessInfoCode;
	}

	/** リクエスト値を取得 */
	protected Object getRequestValue(TraySearchRequest req, String businessInfoCode, String id) {
		Object v = req.get(id);
		if (v == null)
			return v;

		String s = v.toString();
		if (TIMESTAMPS.contains(businessInfoCode))
			return toTimestamp(s, "yyyy/MM/dd");
		if (DATES.contains(businessInfoCode))
			return toDate(s, "yyyy/MM/dd");
		if (LONGS.contains(businessInfoCode))
			return toLong(s);
		if (INTEGERS.contains(businessInfoCode))
			return toInt(s);
		return v.toString();
	}

	/**
	 * トレイ設定検索条件.検索条件一致区分をWF検索条件区分に変換
	 * @param conditionMatchType 検索条件一致区分
	 * @return
	 */
	protected String toWfSearchConditionType(String conditionMatchType) {
		if (eq(ConditionMatchType.FULL, conditionMatchType))
			// 完全一致
			return EQUAL;
		if (eq(ConditionMatchType.FRONT, conditionMatchType))
			// 前方一致
			return PRE_MATCH;
		else if (eq(ConditionMatchType.BOTH, conditionMatchType))
			// 部分一致
			return MATCH;
		else if (eq(ConditionMatchType.GT, conditionMatchType)) {
			// 超過（＞）
			return GRATER_THAN;
		}
		else if (eq(ConditionMatchType.LT, conditionMatchType)) {
			// 未満（＜）
			return LESS_THAN;
		}
		else if (eq(ConditionMatchType.GTE, conditionMatchType)) {
			// 以上（≧）
			return GRATER_EQUAL;
		}
		else if (eq(ConditionMatchType.LTE, conditionMatchType)) {
			// 以下（≦）
			return LESS_EQUAL;
		}
		else if (eq(ConditionMatchType.NOT_EQUAL, conditionMatchType)) {
			// 不等価（！）
			return NOT_EQUAL;
		}
		throw new BadRequestException("検索一致区分が未定義です conditionMatchType=" + conditionMatchType);
	}

	/**
	 * ページ制御用のレスポンスを生成
	 * @param c
	 * @param req
	 * @param allCount
	 * @return
	 */
	protected BaseTrayResponse createTrayResponse(TraySearchRequest req, int allCount) {
		// TraySearchRequestの実体はMapなので、基底クラスのcreateResponse()を呼び出せない。
		// これを回避するためにダミーリクエストを生成し、TraySearchRequestの値を転写
		final BasePagingRequest dummy = new BasePagingRequest();
		for (Field f : dummy.getClass().getDeclaredFields()) {
			String name = f.getName();
			Object value = req.get(name);
			try {
				f.set(dummy, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new InternalServerErrorException(e);
			}
		}
		final BaseTrayResponse res = createResponse(BaseTrayResponse.class, dummy);

		final Integer pageSize = toInt(req.get("pageSize"));
		final int pageCount = calcPageCount(allCount, pageSize);
		Integer pageNo = toInt(req.get("pageNo"));
		pageNo = calcPageNo(pageNo, pageCount);
		res.allCount = allCount;
		res.pageNo = pageNo;
		res.pageCount = pageCount;

		// 件数で補正されたページ番号を反映
		req.put("pageNo", pageNo.toString());

		return res;
	}

	/**
	 * 検索条件INパラメータを生成
	 * @param clazz 検索条件INパラメータの型
	 * @param mode 検索モード（GetProcessHistoryListInParam.Mode.USER_INFO_SHARER_HISTORYとか）
	 * @param req 検索リクエスト
	 * @return
	 */
	protected <P extends BaseTrayInParam<?>> P createSearchInParam(Class<P> clazz, String mode, TraySearchRequest req) {
		// 基本的にクラスとModeが異なるだけで、どのトレイでも同じ絞り込み条件が適用されるので、共通化が出来る
		final P in = newInstance(clazz);
		in.setMode(mode);
		in.setSortType("");						//APIの旧ソート機能を無効にする
		in.setExecuting(true);
		in.setSelectMode(CodeMaster.SelectMode.BOTH);

		// 絞り込み条件
		final List<WfSearchCondition<?>> conds = toWfSearchCondition(req);
		in.setSearchConditionList(conds);

		// 代理
		final WfUserRole userRole = sessionHolder.getWfUserRole().clone(true);
		final String proxyUser = (String)req.get("proxyUser");
		if (isNotEmpty(proxyUser)) {
			final WfUserRole proxy = new WfUserRoleImpl();
			proxy.setCorporationCode(proxyUser.split("_")[0]);
			proxy.setUserCode(proxyUser.split("_")[1]);
			userRole.setProxyUserRole(proxy);
			userRole.setProxy(true);
		}
		in.setWfUserRole(userRole);

		// ソート条件
		final List<WfSortOrder> sorts = toWfSortList(req);
		in.setSortOrderList(sorts);

		return in;
	}

	/**
	 * List<エンティティ>を List<Map<エンティティ属性名, エンティティ属性値>>に変換。
	 *
	 * @param srcList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, String>> toListMap(List<?> srcList) {
		String json = toJsonFromObj(srcList);
		return toObjFromJson(json, List.class);
	}

	/**
	 * 検索実行し、検索結果レスポンスを生成
	 * @param req 検索条件リクエスト
	 * @param funcCount IWF APIによる件数カウントのラムダ式
	 * @param funcSelect IWF APIによるエンティティ抽出のラムダ式
	 * @param in IWF APIに渡すINパラメータ
	 * @return
	 */
	protected <P extends BaseTrayInParam<?>, O extends BaseTrayOutParam<?>> BaseTrayResponse createSearchResponse(
			TraySearchRequest req, Function<P, O> funcSelect, P in) {
		Integer pageSize = toInt(req.get("pageSize"));
		Integer pageNo = toInt(req.get("pageNo"));
		Integer start = calcStartRowNo(pageNo, pageSize);
		in.setRowCount(toLong(pageSize.longValue()));
		in.setRowNo(start.longValue());
		in.setSelectMode(SelectMode.BOTH);
		O out = funcSelect.apply(in);

		final BaseTrayResponse res = createTrayResponse(req, out.getAllCount().intValue());
		if (out.getAllCount().intValue() == 0) {
			res.results = new ArrayList<>();
		}
		else {
			// 実データの取得
			res.results = out.getTrayList();
			// 表示範囲
			res.start = start;
			res.end = calcEndRowNo(res.pageNo, pageSize, out.getAllCount().intValue());
		}
		res.success = true;
		return res;
	}

	/** QueryStringから検索条件ダミーリクエストを生成する */
	protected TraySearchRequest createDummyRequest(HttpServletRequest hsr) {
		// QueryStringをもとにTraySearchRequestをでっち上げ
		final TraySearchRequest req = new TraySearchRequest();
		for (Object k : hsr.getParameterMap().keySet()) {
			String key = k.toString();
			String[] values = hsr.getParameterMap().get(key);
			if (values == null || values.length == 0)
				req.put(key, null);
			else
				req.put(key, values[0]);
		}

		// トレイ設定マスタを抽出し、リクエストに追記
		long trayConfigId = toLong(req.get("trayConfigId"));
		final List<TrayConditionDef> conditions = getTrayConditions(trayConfigId);
		req.put("configConditions", toListMap(conditions));
//		final List<TrayResult> results = getTrayResults(trayConfigId);
//		req.put("configResults", toListMap(results));

		return req;
	}
}
