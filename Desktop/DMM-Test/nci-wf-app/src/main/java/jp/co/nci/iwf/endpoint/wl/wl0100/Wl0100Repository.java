package jp.co.nci.iwf.endpoint.wl.wl0100;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wm.WfvSelectStartUser;

/**
 * 起案担当者選択画面のリポジトリ
 */
@ApplicationScoped
public class Wl0100Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}$");

	@Inject private SessionHolder sessionHolder;

	/**
	 * 件数カウント
	 * @param req
	 * @return
	 */
	public int count(Wl0100Request req) {
		final StringBuilder sql = new StringBuilder(
				getSql("WL0100_01")
				.replaceFirst(REPLACE, "count(*)"));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングしながら検索
	 * @param req
	 * @return
	 */
	public List<WfvSelectStartUser> select(Wl0100Request req) {
		final StringBuilder sql = new StringBuilder(
				getSql("WL0100_01")
				.replaceFirst(REPLACE, "*"));
		final List<Object> params = new ArrayList<>();
		fillCondition(req, sql, params, true);

		return select(WfvSelectStartUser.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Wl0100Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(req.corporationCode);
		params.add(req.userCodeTransfer);
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(req.corporationCodeP);
		params.add(req.processDefCode);
		params.add(req.processDefDetailCode);

		// 起案担当者ユーザコード
		if (isNotEmpty(req.userCode)) {
			sql.append(" and USER_CODE = ?");
			params.add(req.userAddedInfo);
		}
		// 起案担当者のユーザID
		if (isNotEmpty(req.userAddedInfo)) {
			sql.append(" and USER_ADDED_INFO like ? escape '~'");
			params.add(escapeLikeFront(req.userAddedInfo));
		}
		// 起案担当者のユーザ氏名
		if (isNotEmpty(req.userName)) {
			sql.append(" and USER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.userName));
		}
		// 起案担当者の組織コード
		if (isNotEmpty(req.organizationCode)) {
			sql.append(" and ORGANIZATION_CODE = ?");
			params.add(req.organizationCode);
		}
		// 起案担当者の組織ID
		if (isNotEmpty(req.organizationAddedInfo)) {
			sql.append(" and ORGANIZATION_ADDED_INFO like ? escape '~'");
			params.add(escapeLikeFront(req.organizationAddedInfo));
		}
		// 起案担当者の組織名
		if (isNotEmpty(req.organizationName)) {
			sql.append(" and ORGANIZATION_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.organizationName));
		}
		// 起案担当者の組織階層名
		if (isNotEmpty(req.organizationTreeName)) {
			sql.append(" and ORGANIZATION_TREE_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.organizationTreeName));
		}

		// 起案担当者の役職コード
		if (isNotEmpty(req.postCode)) {
			sql.append(" and POST_CODE = ?");
			params.add(req.postCode);
		}
		// 起案担当者の役職ID
		if (isNotEmpty(req.postAddedInfo)) {
			sql.append(" and POST_ADDED_INFO like ? escape '~'");
			params.add(escapeLikeFront(req.postAddedInfo));
		}
		// 起案担当者の役職名
		if (isNotEmpty(req.postName)) {
			sql.append(" and POST_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.postName));
		}

		// 主務兼務
		if (isNotEmpty(req.jobType)) {
			sql.append(" and JOB_TYPE = ?");
			params.add(req.jobType);
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
