package jp.co.nci.iwf.endpoint.dc.dc0070;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 拡張項目一覧リポジトリ.
 */
@ApplicationScoped
public class Dc0070Repository extends BaseRepository {

	/**
	 * 件数抽出
	 * @param req
	 * @return
	 */
	public int count(Dc0070Request req) {
		StringBuilder sql = new StringBuilder(getSql("DC0070_02"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		final StringBuilder countSql = new StringBuilder(getSql("DC0070_01").replaceFirst("###DC0070_02###", sql.toString()));

		return count(countSql.toString(), params.toArray());
	}

	/**
	 * 検索エンティティ抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<Dc0070Entity> select(Dc0070Request req, Dc0070Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("DC0070_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Dc0070Entity.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Dc0070Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(req.localeCode);

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and A.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// メタテンプレートコード
		if (isNotEmpty(req.metaTemplateCode)) {
			sql.append(" and A.META_TEMPLATE_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.metaTemplateCode));
		}
		// メタテンプレート名称
		if (isNotEmpty(req.metaTemplateName)) {
			sql.append(" and A.META_TEMPLATE_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.metaTemplateName));
		}
		// 削除区分
		if (isNotEmpty(req.deleteFlag)) {
			sql.append(" and A.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
		}

		// ページングおよびソート
		if (paging) {
			// ソート
			if (isNotEmpty(req.sortColumn)) {
				sql.append(toSortSql(req.sortColumn, req.sortAsc));
			}
			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}
}
