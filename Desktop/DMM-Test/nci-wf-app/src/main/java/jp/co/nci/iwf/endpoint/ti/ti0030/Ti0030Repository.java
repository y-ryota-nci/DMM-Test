package jp.co.nci.iwf.endpoint.ti.ti0030;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;

/**
 * 汎用テーブル設定一覧のリポジトリ
 */
@ApplicationScoped
public class Ti0030Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Ti0030Request req) {
		final StringBuilder sql = new StringBuilder(getSql("TI0030_01"));
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
	public List<Ti0030Entity> select(Ti0030Request req, Ti0030Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("TI0030_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Ti0030Entity.class, sql.toString(), params.toArray());
	}

	private void fillCondition(Ti0030Request req, StringBuilder sql, List<Object> params, boolean paging) {
		// 置換文字列（SELECT句）
		{
			final String FIND = "{REPLACEMENT}";
			final int start = sql.indexOf(FIND);
			final int end = start + FIND.length();
			final String replacement = (paging ? getSql("TI0030_02") : "count(*)");
			sql.replace(start, end, replacement);
		}

		final LoginInfo login = sessionHolder.getLoginInfo();
		final String corporationCode = login.getCorporationCode();
		final String localeCode = login.getLocaleCode();
		params.add(localeCode);
		params.add(corporationCode);

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
		// カテゴリID
		if (req.categoryId != null) {
			sql.append(" and CC.CATEGORY_ID = ?");
			params.add(req.categoryId);
		}
		// テーブル名
		if (isNotEmpty(req.tableName)) {
			sql.append(" and T.TABLE_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.tableName));
		}
		// 表示名
		if (isNotEmpty(req.displayName)) {
			sql.append(" " + getSql("TI0030_04"));
			params.add(escapeLikeBoth(req.displayName));
		}
		// テーブルのコメント
		if (isNotEmpty(req.logicalTableName)) {
			sql.append(" and nvl(M1.VAL, T.LOGICAL_TABLE_NAME) like ? escape '~'");
			params.add(escapeLikeBoth(req.logicalTableName));
		}
		// エンティティ区分
		if (isNotEmpty(req.entityType)) {
			sql.append(" and T.ENTITY_TYPE = ?");
			params.add(req.entityType);
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

	/** カテゴリ一覧 */
	public List<MwmCategory> getCategories(String corporationCode, String localeCode) {
		final Object[] params = {localeCode, corporationCode};
		return select(MwmCategory.class, getSql("TI0030_03"), params);
	}

}
