package jp.co.nci.iwf.endpoint.vd.vd0010;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;

/**
 * コンテナ一覧リポジトリ
 */
@ApplicationScoped
public class Vd0010Repository extends BaseRepository {
	@Inject
	private SessionHolder sessionHolder;

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Vd0010Request req) {
		StringBuilder sql = new StringBuilder(getSql("VD0010_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Vd0010Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());

		// 企業コード
		if (isNotEmpty(req.corporationCode)) {
			sql.append(" and C.CORPORATION_CODE = ?");
			params.add(req.corporationCode);
		}
		// コンテナ名
		if (isNotEmpty(req.containerName)) {
			sql.append(" and C.CONTAINER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.containerName));
		}
		// コンテナコード
		if (isNotEmpty(req.containerCode)) {
			sql.append(" and C.CONTAINER_CODE like ? escape '~'");
			params.add(escapeLikeFront(req.containerCode));
		}
		// テーブル名
		if (isNotEmpty(req.tableName)) {
			sql.append(" and C.TABLE_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.tableName));
		}
		// 削除区分
		if (isNotEmpty(req.deleteFlag)) {
			sql.append(" and C.DELETE_FLAG = ?");
			params.add(req.deleteFlag);
		}
		// テーブル同期
		if (CommonFlag.ON.equals(req.syncTable)) {
			sql.append(" and (C.TABLE_SYNC_TIMESTAMP >= C.TABLE_MODIFIED_TIMESTAMP)");
		} else if (CommonFlag.OFF.equals(req.syncTable)) {
			sql.append(" and (C.TABLE_SYNC_TIMESTAMP is null or C.TABLE_SYNC_TIMESTAMP < C.TABLE_MODIFIED_TIMESTAMP)");
		}
		// テーブル同期日時
		if (req.tableSyncFrom != null) {
			sql.append(" and C.TABLE_SYNC_TIMESTAMP >= ?");
			params.add(req.tableSyncFrom);
		}
		if (req.tableSyncTo != null) {
			sql.append(" and C.TABLE_SYNC_TIMESTAMP < ?");
			params.add(addDay(trunc(req.tableSyncTo), 1));
		}
		// テーブル定義変更日時
		if (req.tableModifiedFrom != null) {
			sql.append(" and C.TABLE_MODIFIED_TIMESTAMP >= ?");
			params.add(req.tableModifiedFrom);
		}
		if (req.tableModifiedTo != null) {
			sql.append(" and C.TABLE_MODIFIED_TIMESTAMP < ?");
			params.add(addDay(trunc(req.tableModifiedTo), 1));
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
	public List<Vd0010Entity> select(Vd0010Request req, Vd0010Response res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final StringBuilder sql = new StringBuilder(getSql("VD0010_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Vd0010Entity.class, sql.toString(), params.toArray());
	}

	/**
	 * テーブル同期日時を更新
	 * @param c
	 */
	@Transactional
	public void updateTableSyncTimestamp(MwmContainer c) {
		// VERSIONによる排他ロックをしない。
		// なぜなら、仮にMWM_CONTAINERへの排他ロックをしたとしても
		// テーブル同期用のDDLを発行しているため、MWM_CONTAINERへの行ロックしても
		// 排他ロックが出来ないのは自明であり、失敗する可能性のある排他ロックを
		// すること自体が無意味であろう。
		// したがってSQLを直接発行してテーブル同期日時だけを更新してやるのがスジである。
		final LoginInfo login = sessionHolder.getLoginInfo();
		final List<Object> params = new ArrayList<>();
		final Timestamp timestamp = timestamp();
		params.add(timestamp);
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(timestamp);
		params.add(c.getContainerId());
		execSql(getSql("VD0010_03"), params.toArray());
	}

	/** コンテナIDをキーにコンテナ情報を抽出 */
	public Vd0010Entity get(Long containerId) {
		if (containerId == null)
			return null;

		final StringBuilder sql = new StringBuilder(getSql("VD0010_02"));
		sql.append(" and C.CONTAINER_ID = ?");

		final Object[] params = { sessionHolder.getLoginInfo().getLocaleCode(), containerId };

		return selectOne(Vd0010Entity.class, sql.toString(), params);
	}
}
