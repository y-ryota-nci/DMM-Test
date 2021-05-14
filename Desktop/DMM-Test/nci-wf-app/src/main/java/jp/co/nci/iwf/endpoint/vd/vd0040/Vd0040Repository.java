package jp.co.nci.iwf.endpoint.vd.vd0040;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;

/**
 * 画面プロセス定義一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0040Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	public int count(Vd0040SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("VD0040_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Vd0040SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// プロセス定義コード
		if (isNotEmpty(req.processDefCode)) {
			sql.append(" and PROCESS_DEF_CODE = ?");
			params.add(req.processDefCode);
		}
		// 画面プロセス定義コード
		if (isNotEmpty(req.screenProcessCode)) {
			sql.append(" and SCREEN_PROCESS_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.screenProcessCode));
		}
		// 画面プロセス定義名
		if (isNotEmpty(req.screenProcessName)) {
			sql.append(" and SCREEN_PROCESS_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.screenProcessName));
		}
		// 画面コード
		if (isNotEmpty(req.screenCode)) {
			sql.append(" and SCREEN_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.screenCode));
		}
		// 画面名
		if (isNotEmpty(req.screenName)) {
			sql.append(" and SCREEN_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.screenName));
		}
		// 有効期間
		if (req.validStartDate != null) {
			sql.append(" and VALID_START_DATE <= ?");
			params.add(req.validStartDate);
		}
		if (req.validEndDate != null) {
			sql.append(" and ? < VALID_END_DATE + 1");
			params.add(req.validEndDate);
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
	 * ページ制御付で検索
	 * @param req
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<MwvScreenProcessDef> select(Vd0040SearchRequest req, Vd0040SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("VD0040_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(MwvScreenProcessDef.class, sql.toString(), params.toArray());
	}

	/**
	 * 削除
	 * @param screenProcessId
	 * @return
	 */
	public int delete(Long screenProcessId) {
		final Object[] params = new Object[] { screenProcessId };

		// ER図から関連するテーブルをすべて削除

		// MWM_ACCESSIBLE_SCREEN
		execSql(getSql("VD0040_03"), params);

		// MWM_BLOCK_DISPLAY
		execSql(getSql("VD0040_04"), params);

		// MWM_SCREEN_PROCESS_DEF
		return execSql(getSql("VD0040_05"), params);
	}
}
