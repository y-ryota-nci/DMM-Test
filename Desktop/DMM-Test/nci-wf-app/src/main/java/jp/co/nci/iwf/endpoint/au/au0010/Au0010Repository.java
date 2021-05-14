package jp.co.nci.iwf.endpoint.au.au0010;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.WfvUserPassword;

/**
 * パスワード一覧のリポジトリ
 */
@ApplicationScoped
public class Au0010Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	public int count(Au0010Request req) {
		StringBuilder sb = new StringBuilder(getSql("AU0010_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sb, params, false);

		String sql = sb.toString().replaceAll(quotePattern("{REPLACEMENT}"), "count(*)");
		return count(sql, params.toArray());
	}

	private void fillCondition(Au0010Request req, StringBuilder sql, List<Object> params, boolean sort) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// ユーザID
		if (isNotEmpty(req.userAddedInfo)) {
			sql.append(" and USER_ADDED_INFO like ? escape '~'");
			params.add(escapeLikeFront(req.userAddedInfo));
		}
		// ユーザ氏名
		if (isNotEmpty(req.userName)) {
			sql.append(" and USER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.userName));
		}
		// アカウントロック
		if (req.lockFlag) {
			sql.append(" and LOCK_FLAG = ?");
			params.add(CommonFlag.ON);
		}
		// パスワード変更要求
		if (req.changeRequestFlag) {
			sql.append(" and CHANGE_REQUEST_FLAG = ?");
			params.add(CommonFlag.ON);
		}

		// ソート
		if (sort && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	public List<WfvUserPassword> select(Au0010Request req, Au0010Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sb = new StringBuilder(getSql("AU0010_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sb, params, true);

		String sql = sb.toString().replaceAll(quotePattern("{REPLACEMENT}"), "*");
		return select(WfvUserPassword.class, sql, params.toArray());
	}
}
