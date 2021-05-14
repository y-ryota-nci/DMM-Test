package jp.co.dmm.customize.endpoint.py.py0080;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.dmm.customize.component.DmmCodeBook.AdvpaySts;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 前払残高一覧のリポジトリ
 */
@ApplicationScoped
public class Py0080Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0080SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0080_01").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Py0080SearchRequest req, Py0080SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0080_01").replaceFirst(REPLACE, getSql("PY0080_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0080Entity.class, sql, params.toArray());
	}

	/**
	 * Excel出力用（ページングなしで抽出）
	 * @param req
	 * @return
	 */
	public List<Py0080Entity> selectExcel(Py0080SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0080_01").replaceFirst(REPLACE, getSql("PY0080_02")));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);
		return select(Py0080Entity.class, sql, params.toArray());
	}


	private void fillCondition(Py0080SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(LoginInfo.get().getLocaleCode());
		// 会社コード
		if (isNotEmpty(req.companyCd)) {
			sql.append(" and A.COMPANY_CD = ? ");
			params.add(req.companyCd);
		}

		// 前払No
		if (isNotEmpty(req.advpayNo)) {
			sql.append(" and A.ADVPAY_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.advpayNo));
		}
		// 前払件名
		if (isNotEmpty(req.payNm)) {
			sql.append(" and P.PAY_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.payNm));
		}
		// 取引先CD
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and A.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// ステータス
		List<String> advpayStsList = new ArrayList<>();
		if (req.advpaySts0) advpayStsList.add(AdvpaySts.INIT);
		if (req.advpaySts1) advpayStsList.add(AdvpaySts.DOING);
		if (req.advpaySts2) advpayStsList.add(AdvpaySts.DONE);
		if (!advpayStsList.isEmpty()) {
			sql.append(" and " + toInListSql("A.ADVPAY_STS", advpayStsList.size()));
			params.addAll(advpayStsList);
		}
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
