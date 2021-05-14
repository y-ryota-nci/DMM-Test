package jp.co.dmm.customize.endpoint.ct.ct0020;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

@ApplicationScoped
public class Ct0020Repository extends BaseRepository {

	/**
	 * 件数抽出
	 * @param req
	 * @return
	 */
	public int count(Ct0020SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("CT0020_01"));

		List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 検索エンティティ抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<Ct0020Entity> select(Ct0020SearchRequest req, Ct0020SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("CT0020_02"));

		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, true);

		return select(Ct0020Entity.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Ct0020SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(req.corporationCode);

		// カタログコード
		if (isNotEmpty(req.catalogCode)) {
			sql.append(" and C.CATALOG_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.catalogCode));
		}
		// カタログ名
		if (isNotEmpty(req.catalogName)) {
			sql.append(" and C.CATALOG_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.catalogName));
		}
		// カタログカテゴリ
		if (isNotEmpty(req.catalogCategoryId)) {
			sql.append(" and C.CATALOG_CATEGORY_ID = ?");
			params.add(req.catalogCategoryId);
		}

		if (paging && isNotEmpty(req.sortColumn)) {
			// ソート
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

}
