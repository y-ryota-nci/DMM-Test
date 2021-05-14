package jp.co.nci.iwf.endpoint.dc.dc0110;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenDocDef;

/**
 * 画面文書定義一覧のリポジトリ
 */
@ApplicationScoped
public class Dc0110Repository extends BaseRepository {

	@Inject
	private SessionHolder sessionHolder;

	public int count(Dc0110SearchRequest req) {
		StringBuilder sql = new StringBuilder(getSql("DC0110_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Dc0110SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// 画面文書定義コード
		if (isNotEmpty(req.screenDocCode)) {
			sql.append(" and SCREEN_DOC_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.screenDocCode));
		}
		// 画面文書定義名
		if (isNotEmpty(req.screenDocName)) {
			sql.append(" and SCREEN_DOC_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.screenDocName));
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
	public List<MwvScreenDocDef> select(Dc0110SearchRequest req, Dc0110SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("DC0110_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(MwvScreenDocDef.class, sql.toString(), params.toArray());
	}

	/**
	 * 削除
	 * @param screenProcessId
	 * @return
	 */
	public int delete(Long screenProcessId) {
		final Object[] params = new Object[] { screenProcessId };

		// ER図から関連するテーブルをすべて削除
		// MWM_ACCESSIBLE_DOC
		execSql(getSql("DC0110_03"), params);

		// MWM_SCREEN_DOC_DEF
		return execSql(getSql("DC0110_04"), params);

	}

}
