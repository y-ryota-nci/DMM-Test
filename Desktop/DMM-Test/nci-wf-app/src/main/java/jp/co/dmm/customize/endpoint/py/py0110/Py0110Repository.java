package jp.co.dmm.customize.endpoint.py.py0110;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 前払選択のリポジトリ
 */
@ApplicationScoped
public class Py0110Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0110SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0110_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Py0110SearchRequest req, Py0110SearchResponse res) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0110_01").replaceFirst(REPLACE, getSql("PY0110_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0110Entity.class, sql, params.toArray());
	}

	private void fillCondition(Py0110SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
//		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getCorporationCode());

		// 発注No
		if (isNotEmpty(req.maebaraiNo)) {
			sql.append(" and O.MAEBARAI_NO like ? escape '~'");
			params.add(escapeLikeFront(req.maebaraiNo));
		}
		// 発注件名
		if (isNotEmpty(req.maebaraiNm)) {
			sql.append(" and O.MAEBARAI_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.maebaraiNm));
		}
		// 取引先CD
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and O.SPLR_CD = ?");
			params.add(req.splrCd);
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
}
