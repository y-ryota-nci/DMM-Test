package jp.co.nci.iwf.endpoint.vd.vd0030;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;

/**
 * 画面一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0030Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	@Inject
	private SessionHolder sessionHolder;

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Vd0030SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("VD0030_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Vd0030SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// コンテナ名
		if (isNotEmpty(req.containerName)) {
			sql.append(" and CONTAINER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.containerName));
		}
		// コンテナコード
		if (isNotEmpty(req.containerCode)) {
			sql.append(" and CONTAINER_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.containerCode));
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
		// スクラッチ区分
		if (isNotEmpty(req.scratchFlag)) {
			sql.append(" and SCRATCH_FLAG = ?");
			params.add(req.scratchFlag);
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
	public List<MwvScreen> select(Vd0030SearchRequest req, Vd0030SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("VD0030_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(MwvScreen.class, sql.toString(), params.toArray());
	}

	/**
	 * 削除
	 * @param screenId
	 * @return
	 */
	public int delete(Long screenId) {
		final Object[] params = new Object[] { screenId };

		// ER図から関連するテーブルをすべて削除

		// MWM_ACCESSIBLE_SCREEN
		execSql(getSql("VD0030_03"), params);

//		FKがないので、画面プロセス定義階層マスタは削除してはダメ（FKの向きが逆だから）
//		// MWM_SCREEN_PROCESS_LEVEL
//		execSql(getSql("VD0030_04"), params);

		// MWM_BLOCK_DISPLAY
		execSql(getSql("VD0030_05"), params);

		// MWM_SCREEN_PROCESS_DEF
		execSql(getSql("VD0030_06"), params);

		// MWM_SCREEN_PARTS_COND_ITEM
		execSql(getSql("VD0030_12"), params);

		// MWM_SCREEN_PARTS_COND
		execSql(getSql("VD0030_13"), params);

		// MWM_SCREEN_CALC_ITEM
		execSql(getSql("VD0030_08"), params);

		// MWM_SCREEN_CALC_EC
		execSql(getSql("VD0030_09"), params);

		// MWM_SCREEN_CALC
		execSql(getSql("VD0030_10"), params);

		// MWM_SCREEN
		return execSql(getSql("VD0030_11"), params);
	}

	public MwmScreen get(Long screenId) {
		return em.find(MwmScreen.class, screenId);
	}

	/** 画面情報を抽出 */
	public List<MwmScreen> getScreens(List<Long> screenIds) {
		final String sql = getSql("VD0030_14")
				.replaceFirst(REPLACE, toInListSql("and SCREEN_ID", screenIds.size()));
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.addAll(screenIds);
		return select(MwmScreen.class, sql, params.toArray());
	}
}
