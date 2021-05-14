package jp.co.nci.iwf.endpoint.ti.ti0050;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearch;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearchColumn;

/**
 * 汎用テーブル検索条件一覧リポジトリ
 */
@ApplicationScoped
public class Ti0050Repository extends BaseRepository {
	/**
	 * 件数抽出
	 * @param req
	 * @return
	 */
	public int count(Ti0050Request req) {
		final StringBuilder sql = new StringBuilder(getSql("TI0050_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページ制御アリの検索
	 * @param req
	 * @param res
	 * @return
	 */
	public List<Ti0050Entity> select(Ti0050Request req, Ti0050Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("TI0050_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		// 検索結果
		final List<Ti0050Entity> results = select(Ti0050Entity.class, sql.toString(), params.toArray());

		// 検索結果から、検索条件IDだけを抜き出す
		final Set<Long> tableSearchIds = results.stream()
				.map(r -> r.tableSearchId)
				.collect(Collectors.toSet());

		// 検索条件IDを使用しているコンテナ一覧をMapとして抽出
		final Map<Long, List<Ti0050ContainerEntity>> map = getContainers(tableSearchIds);

		// 検索条件IDが使用しているコンテナ名をフィールド値として設定
		results.forEach(r -> {
			List<Ti0050ContainerEntity> containers = map.get(r.tableSearchId);
			if (containers != null && !containers.isEmpty()) {
				r.containerNames = containers.stream()
						.map(c -> "[" + c.containerCode + "]" + c.containerName)
						.distinct()
						.sorted()
						.collect(Collectors.joining(", "));
			}
		});
		return results;
	}

	/** 汎用テーブル検索条件IDリストから、使用しているコンテナ一覧をMap形式で抽出 */
	private Map<Long, List<Ti0050ContainerEntity>> getContainers(Set<Long> tableSearchIds) {
		final StringBuilder sql = new StringBuilder(getSql("TI0050_04"));
		sql.append(toInListSql(" and PT.TABLE_SEARCH_ID", tableSearchIds.size()));

		final List<Object> params = new ArrayList<>();
		params.add(LoginInfo.get().getLocaleCode());
		params.addAll(tableSearchIds);

		return select(Ti0050ContainerEntity.class, sql.toString(), params.toArray())
				.stream()
				.collect(Collectors.groupingBy(r -> r.tableSearchId, Collectors.toList()));
	}

	private void fillCondition(Ti0050Request req, StringBuilder sql, List<Object> params, boolean paging) {
		// 置換文字列（SELECT句）
		{
			final String FIND = "{SELECT}";
			final int start = sql.indexOf(FIND);
			final int end = start + FIND.length();
			final String replacement = (paging ? getSql("TI0050_02") : "count(*)");
			sql.replace(start, end, replacement);
		}

		final LoginInfo login = LoginInfo.get();
		params.add(login.getLocaleCode());
		params.add(login.getCorporationCode());
		params.add(req.tableId);

		// ロールによる権限の制限
		{
			final Set<String> menuRoleCodes = login.getMenuRoleCodes();
			final String FIND = "{MENU_ROLE_CODE}";
			final int start = sql.indexOf(FIND);
			final int end = start + FIND.length();
			final String replacement = toInListSql("TA.MENU_ROLE_CODE", login.getMenuRoleCodes().size());
			sql.replace(start, end, replacement);
			params.addAll(menuRoleCodes);
		}

		// 汎用テーブル検索コード
		if (isNotEmpty(req.tableSearchCode)) {
			sql.append(" and S.TABLE_SEARCH_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.tableSearchCode));
		}
		// 汎用テーブル検索名
		if (isNotEmpty(req.tableSearchName)) {
			sql.append(" and nvl(M1.VAL, S.TABLE_SEARCH_NAME) like ? escape '~'");
			params.add(escapeLikeBoth(req.tableSearchName));
		}
		// 表示名
		if (isNotEmpty(req.displayName)) {
			sql.append(" and nvl(M2.VAL, S.DISPLAY_NAME) like ? escape '~'");
			params.add(escapeLikeBoth(req.displayName));
		}
		// 初期検索フラグ
		if (isNotEmpty(req.defaultSearchFlag)) {
			sql.append(" and S.DEFAULT_SEARCH_FLAG = ?");
			params.add(req.defaultSearchFlag);
		}
		// 削除区分
		if (isNotEmpty(req.deleteFlag)) {
			sql.append(" and S.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
		}

		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	/**
	 * 汎用テーブル検索条件IDに紐付くレコードを論理削除
	 * @param tableSearchId
	 */
	public void logicalDelete(Long tableSearchId) {
		// 汎用テーブル検索条件
		final MwmTableSearch t = em.find(MwmTableSearch.class, tableSearchId);
		if (t != null) {
			t.setDeleteFlag(DeleteFlag.ON);
		}

		// 汎用テーブル検索条件カラム
		final Object[] params = { tableSearchId };
		List<MwmTableSearchColumn> cols = select(MwmTableSearchColumn.class, getSql("TI0050_03"), params);
		for (MwmTableSearchColumn col : cols) {
			col.setDeleteFlag(DeleteFlag.ON);
		}
		em.flush();
	}

}
