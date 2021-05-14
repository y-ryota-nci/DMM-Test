package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 外部Javascript一覧のリポジトリ
 */
@ApplicationScoped
public class Vd0050Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	public int count(Vd0050SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("VD0050_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Vd0050SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());


		// 企業コード
		sql.append(" where J.CORPORATION_CODE = ?");
		params.add(req.corporationCode);

		// ファイル名
		if (isNotEmpty(req.fileName)) {
			sql.append(" and J.FILE_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.fileName));
		}
		// 最終更新日時
		if (req.from != null) {
			sql.append(" and H.TIMESTAMP_UPDATED >= ?");
			params.add(req.from);
		}
		if (req.to != null) {
			sql.append(" and H.TIMESTAMP_UPDATED < (? + 1)");
			params.add(req.to);
		}
		// 過去履歴を含む
		if (!req.includeOldVersions) {
			sql.append(" and H.DELETE_FLAG = '0'");
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
	public List<Vd0050Entity> select(Vd0050SearchRequest req, Vd0050SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("VD0050_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Vd0050Entity.class, sql.toString(), params.toArray());
	}

	/**
	 * 削除
	 * @param javascriptId
	 */
	public void delete(Long javascriptId) {
		// MWM_JAVASCRIPT
		final Object[] params = { javascriptId };
		execSql(getSql("VD0050_03"), params);

		// MWM_JAVASCRIPT_HISTORY
		execSql(getSql("VD0050_04"), params);

		// MWM_CONTAINER_JAVASCRIPT
		execSql(getSql("VD0050_05"), params);

		// MWM_SCREEN_JAVASCRIPT
		execSql(getSql("VD0050_06"), params);
	}

	public List<Vd0050Reference> getContainerJs(String corporationCode) {
		final String localeCode = LoginInfo.get().getLocaleCode();
		final Object[] params = { localeCode, corporationCode };
		return select(Vd0050Reference.class, getSql("VD0050_07"), params);
	}

	public List<Vd0050Reference> getScreenJs(String corporationCode) {
		final String localeCode = LoginInfo.get().getLocaleCode();
		final Object[] params = { localeCode, corporationCode };
		return select(Vd0050Reference.class, getSql("VD0050_08"), params);
	}
}
