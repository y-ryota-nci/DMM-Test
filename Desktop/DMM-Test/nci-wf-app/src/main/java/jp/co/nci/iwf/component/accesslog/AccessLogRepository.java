package jp.co.nci.iwf.component.accesslog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.RowHandler;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmUser;
import jp.co.nci.integrated_workflow.model.view.WfvUserBelong;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfvUserBelongInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.endpoint.al.al0010.Al0010Entity;
import jp.co.nci.iwf.endpoint.sandbox.excel.SandboxUser;
import jp.co.nci.iwf.endpoint.sandbox.excel.SandboxUserBelong;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLog;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLogDetail;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * アクセスログへのDB書き込みクラス。
 * 自律型トランザクション制御もここで行う
 */
@ApplicationScoped
public class AccessLogRepository extends BaseRepository {
	/** アクセスログ用の作成者企業コード（ログイン前は不定なので） */
	private static final String NULL_CORPORATION_CODE = "---";
	/** アクセスログ用の作成者ユーザコード（ログイン前は不定なので） */
	private static final String NULL_USER_CODE = "---";

	@Inject private Logger log;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 独立したコネクションを使ってSQLを実行。トランザクションは単独でコミットされる。
	 * @param sql SQL
	 * @param params SQLに渡すパラメータ
	 * @return
	 */
	private int execSqlWithCommit(String sql, Object[] params) {
		int result = 0;

		// 独立したコネクションを取得してSQL発行＆コミット
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			// 元のautoCommitを退避
			boolean original = conn.getAutoCommit();
			try {
				conn.setAutoCommit(false);
				result = NativeSqlUtils.execSql(conn, sql, params);
				conn.commit();
			}
			catch (Exception e) {
				log.warn(e.getMessage(), e);

				// ロールバック
				try { conn.rollback(); }
				catch (SQLException ex) {};

				if (e instanceof RuntimeException)
					throw e;
				else
					new InternalServerErrorException(e);
			}
			finally {
				// 元のautoCommitを復元
				conn.setAutoCommit(original);
			}
		}
		catch (SQLException e) {
			// アクセスログの書き込みで処理を止めてはならないので、エラーは握りつぶす
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/** インサート */
	public int insert(final MwtAccessLog entity) {
		final Timestamp now = timestamp();
		final String sql = getSql("AL0000_01");
		final List<Object> params = new ArrayList<>();
		params.add(entity.getAccessLogId());		//ACCESS_LOG_ID
		params.add(entity.getAccessTime());			//ACCESS_TIME
		params.add(entity.getUri());				//URI
		params.add(entity.getAccessLogResultType());//ACCESS_LOG_RESULT_TYPE
		params.add(entity.getSessionId());			//SESSION_ID
		params.add(entity.getUserAgent());			//USER_AGENT
		params.add(entity.getOpeCorporationCode());	//OPE_CORPORATION_CODE
		params.add(entity.getOpeUserCode());		//OPE_USER_CODE
		params.add(entity.getOpeUserAddedInfo());	//OPE_USER_ADDED_INFO
		params.add(entity.getOpeIpAddress());		//OPE_IP_ADDRESS
		params.add(entity.getScreenId());			//SCREEN_ID
		params.add(entity.getScreenName());			//SCREEN_NAME
		params.add(entity.getActionName());			//ACTION_NAME
		params.add(entity.getAppVersion());			//APP_VERSION
		params.add(entity.getDbConnectString());	//DB_CONNECT_STRING
		params.add(entity.getHostIpAddress());		//HOST_IP_ADDRESS
		params.add(entity.getHostPort());			//HOST_PORT
		params.add(entity.getThreadName());			//THREAD_NAME
		params.add(entity.getSpoofingCorporationCode());	//SPOOFING_CORPORATION_CODE
		params.add(entity.getSpoofingUserCode());	//SPOOFING_USER_CODE
		params.add(entity.getSpoofingUserAddedInfo());		//SPOOFING_USER_ADDED_INFO
		params.add(1L);								//VERSION
		params.add(DeleteFlag.OFF);					//DELETE_FLAG
		params.add(NULL_CORPORATION_CODE);			//CORPORATION_CODE_CREATED
		params.add(NULL_USER_CODE);					//USER_CODE_CREATED
		params.add(now);							//TIMESTAMP_CREATED
		params.add(NULL_CORPORATION_CODE);			//CORPORATION_CODE_UPDATED
		params.add(NULL_USER_CODE);					//USER_CODE_UPDATED
		params.add(now);							//TIMESTAMP_UPDATED)

		return execSqlWithCommit(sql, params.toArray());
	}

	/**
	 * 全行を抽出（テスト実装）
	 * @param rowHandler
	 */
	public void selectAll(RowHandler<MwtAccessLog> rowHandler) {
		select(MwtAccessLog.class, getSql("CM0004"), rowHandler);
	}

	/**
	 * 先頭から指定件数だけを抽出を抽出（テスト実装）
	 * @param rowHandler
	 */
	public List<Al0010Entity> selectTop(int count) {
		String sql = "select * from MWV_ACCESS_LOG where LOCALE_CODE='ja' "
				+ "order by ACCESS_LOG_ID desc "
				+ "offset 0 rows fetch first ? rows only";
		Object[] params = { count };
		return select(Al0010Entity.class, sql, params);
	}

	/**
	 * 先頭から指定件数だけを抽出を抽出（テスト実装）
	 * @param
	 * @param rowHandler
	 */
	public void selectTop(int count, RowHandler<Al0010Entity> rowHandler) {
		String sql = "select * from MWV_ACCESS_LOG where LOCALE_CODE='ja' "
				+ "order by ACCESS_LOG_ID desc "
				+ "offset 0 rows fetch first ? rows only";
		Object[] params = { count };
		select(Al0010Entity.class, sql, params, rowHandler);
	}

	/**
	 * アクセスログ明細をインサート
	 * @param detail
	 */
	public int insert(MwtAccessLogDetail detail) {
		final Timestamp now = timestamp();
		final String sql = getSql("AL0000_03");
		final List<Object> params = new ArrayList<>();
		params.add(detail.getAccessLogDetailId());	//ACCESS_LOG_DETAIL_ID
		params.add(detail.getAccessLogId());		//ACCESS_LOG_ID
		params.add(detail.getAccessTime());			//ACCESS_TIME
		params.add(detail.getKeyValue());			//KEY_VALUE
		params.add(1L);								//VERSION
		params.add(DeleteFlag.OFF);					//DELETE_FLAG
		params.add(NULL_CORPORATION_CODE);			//CORPORATION_CODE_CREATED
		params.add(NULL_USER_CODE);					//USER_CODE_CREATED
		params.add(now);							//TIMESTAMP_CREATED
		params.add(NULL_CORPORATION_CODE);			//CORPORATION_CODE_UPDATED
		params.add(NULL_USER_CODE);					//USER_CODE_UPDATED
		params.add(now);							//TIMESTAMP_UPDATED

		return execSqlWithCommit(sql, params.toArray());
	}

	/**
	 * アクセスログの処理結果区分を更新
	 * @param accessLogId
	 * @param accessLogResultType
	 */
	public int update(long accessLogId, String accessLogResultType) {
		// アクセスログの書き込みは非同期なので、処理が前後することがある。
		// その場合はしばらく待ってリトライだ
		MwtAccessLog entity = null;
		Object[] params = { accessLogId };
		String sql = getSql("AL0000_04");
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			for (int retry = 0; entity == null && retry < 10; retry++) {
				entity = NativeSqlUtils.selectOne(conn, MwtAccessLog.class, sql, params);
				if (entity == null) {
					try { Thread.sleep(300L); }
					catch (InterruptedException e) {}
				}
			}
		}
		catch (SQLException e) {
			// アクセスログの書き込みで処理を止めてはならないので、エラーは握りつぶす
			log.error(e.getMessage(), e);
		}

		// まだ書き込まれていなければ、アクセスログの処理結果区分のUPDATE
		if (entity != null && eq(AccessLogResultType.UNKNOWN, entity.getAccessLogResultType())) {
			sql = getSql("AL0000_02");
			params = new Object[] { accessLogResultType, timestamp(), accessLogId };
			return execSqlWithCommit(sql, params);
		}
		return 0;
	}

	/** サンプル用ユーザマスタのデータをBeanへ読み込んで返す */
	public List<SandboxUser> getSampleUsers(String corporationCode) {
		List<SandboxUser> users = new ArrayList<>();
		SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setValidStartDate(today());
		in.setValidEndDate(today());
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmUser.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmUser.USER_ADDED_INFO),
				new OrderBy(true, "A." + WfmUser.USER_CODE)
		});
		for (WfmUser src : wf.searchWfmUser(in).getUserList()) {
			final SandboxUser user = new SandboxUser();
			copyFieldsAndProperties(src, user);
			user.belongs = getSampleBelongs(src);
			users.add(user);
		}
		return users;
	}

	/** サンプル用ユーザ所属マスタのデータをBeanへ読み込んで返す */
	private List<SandboxUserBelong> getSampleBelongs(WfmUser u) {
		List<SandboxUserBelong> belongs = new ArrayList<>();
		SearchWfvUserBelongInParam in = new SearchWfvUserBelongInParam();
		in.setCorporationCode(u.getCorporationCode());
		in.setUserCode(u.getUserCode());
		in.setDeleteFlagUser(DeleteFlag.OFF);
		in.setDeleteFlagUserBelong(DeleteFlag.OFF);
		in.setDeleteFlagOrganization(DeleteFlag.OFF);
		in.setDeleteFlagPost(DeleteFlag.OFF);
		in.setValidStartDateOrganization(today());
		in.setValidEndDateOrganization(today());
//		in.setValidStartDatePost(today());
//		in.setValidEndDatePost(today());
		in.setValidStartDateUser(today());
		in.setValidEndDateUser(today());
		in.setValidStartDateUserBelong(today());
		in.setValidEndDateUserBelong(today());
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, WfvUserBelong.CORPORATION_CODE),
				new OrderBy(true, WfvUserBelong.USER_CODE),
				new OrderBy(true, WfvUserBelong.SEQ_NO_USER_BELONG),
		});
		for (WfvUserBelong b : wf.searchWfvUserBelong(in).getUserBelongList()) {
			SandboxUserBelong belong = new SandboxUserBelong();
			copyFieldsAndProperties(b, belong);
			belongs.add(belong);
		}
		if (belongs.isEmpty())	// ダミーの空行
			belongs.add(new SandboxUserBelong());

		return belongs;
	}
}
