package jp.co.nci.iwf.endpoint.sandbox.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.api.param.input.RowHandler;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * サンドボックスSQL画面リポジトリ
 */
@ApplicationScoped
public class SandboxSqlRepository extends BaseRepository {
	private static final Object[] NO_PARAM = new Object[0];

	/**
	 * SQL実行
	 * @param req
	 * @return
	 */
	public List<Map<String, Object>> search(final SandboSqlRequest req) throws SQLException, Exception {
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			boolean autoCommit = conn.getAutoCommit();	// 処理前のautoCommitを退避
			try {
				conn.setAutoCommit(false);

				final AtomicInteger count = new AtomicInteger();
				final List<Map<String, Object>> results = new ArrayList<>();
				NativeSqlUtils.select(conn, req.sql, NO_PARAM, map -> {
					// 最大件数を超過すればそれ以上フェッチしない
					if (req.maxCount > 0 && req.maxCount < count.incrementAndGet()) {
						return false;
					}
					results.add(map);
					return true;
				});
				return results;
			}
			finally {
				// ロールバックを強制
				try { conn.rollback(); } catch (Exception e) {};
				try { conn.setAutoCommit(autoCommit); } catch (Exception e) {}
			}
		}
	}

	/** SQL実行して、行フェッチごとに行ハンドラーを呼び出す */
	public void selectAll(String sql, RowHandler<Map<String, Object>> rowHandler) throws Exception {
		try (Connection conn = NativeSqlUtils.getConnectionSA()) {
			boolean autoCommit = conn.getAutoCommit();	// 処理前のautoCommitを退避
			try {
				conn.setAutoCommit(false);
				NativeSqlUtils.select(conn, sql, NO_PARAM, rowHandler);
			}
			finally {
				// ロールバックを強制
				try { conn.rollback(); } catch (Exception e) {};
				try { conn.setAutoCommit(autoCommit); } catch (Exception e) {}
			}
		}
	}

}
