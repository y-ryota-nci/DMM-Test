package jp.co.dmm.customize.endpoint.sp.sp0040;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 反社情報のリポジトリ
 */
@ApplicationScoped
public class Sp0040Repository extends BaseRepository {

	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 反社情報一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Sp0040Request req) {
		final StringBuilder sql = new StringBuilder(getSql("SP0040_01").replaceFirst(REPLACE, "count(*)"));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);
		return count(sql.toString(), params.toArray());
	}

	/**
	 * 反社情報一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Sp0040Request req, Sp0040Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}

		final StringBuilder replace = new StringBuilder();
		replace.append("  RUNTIME_ID");
		replace.append(", CORPORATION_CODE");
		replace.append(", PROCESS_ID");
		replace.append(", PARENT_RUNTIME_ID");
		replace.append(", SORT_ORDER");
		replace.append(", COMPANY_CD");
		replace.append(", SPLR_CD");
		replace.append(", PEID");
		replace.append(", MTCH_NM");
		replace.append(", LND_CD");
		replace.append(", LND_NM");
		replace.append(", LND_CD_DJII");
		replace.append(", GND_TP");
		replace.append(", BRTH_DT ");

		final StringBuilder sql = new StringBuilder(getSql("SP0040_01").replaceFirst(REPLACE, replace.toString()));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);
		return select(Sp0040Entity.class, sql.toString(), params.toArray());
	}


	/**
	 * 反社情報検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Sp0040Request req, StringBuilder sql, List<Object> params, boolean paging) {

		// ロケールコード
		params.add(req.parentRuntimeId);

		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}

		// ページング
		if (paging) {
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

}
