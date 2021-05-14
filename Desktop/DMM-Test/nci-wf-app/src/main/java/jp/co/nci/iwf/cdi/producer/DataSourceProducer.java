package jp.co.nci.iwf.cdi.producer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

/**
 * DataSource/ConnectionのProducer(Factory)
 */
@ApplicationScoped
public class DataSourceProducer {
	public static final String NAME = "java:comp/env/jdbc/MyDataSource";
	@Inject
	private Logger log;

	/** DataSourceのインスタンスを生成 */
	@ApplicationScoped
	@Produces
	public DataSource getDataSource() {
		try {
			log.debug("=== get DataSource from JNDI({}) === ", NAME);
			final DataSource ds = (DataSource)new InitialContext().lookup(NAME);
			try (Connection conn = ds.getConnection()){
				final DatabaseMetaData m = conn.getMetaData();
				log.trace("    -----------------------");
				log.trace("    driver={} {}", m.getDriverName(), m.getDriverVersion());
				log.trace("    url={}", m.getURL());
				log.trace("    user={}", m.getUserName());
				log.trace("    -----------------------");
			}
			catch (SQLException e) {
				throw new InternalServerErrorException(e);
			}
			return ds;
		}
		catch (NamingException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Connectionのインスタンスを取得 */
	@RequestScoped
	@Produces
	public Connection getConnection() {
		// CDIからDataSource取得し、それ経由でConnectionを取得。
		// CDI管理なので勝手にクローズしてはダメ（@Disposesによってリクエストスコープで自動的にクローズされる）。
		// またJPAの管理には無関係なので@Transactional宣言には影響を受けない。
		// ただしJTAの設定には影響を受けるので注意が必要だ。
		// つまり、persistence.xmlのtransaction-typeでJTAが設定されているかで動作が決まる。
		// JTAならグローバルトランザクションに参加するため、UserTransactionによって
		// トランザクション制御されるが非JTAなら自前でトランザクション制御する必要がある。
		final DataSource ds = CDI.current().select(DataSource.class).get();
		try {
			log.trace("=== get Connection from DataSource === ");
			return ds.getConnection();
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Connectionのリソース解放（CDIによってスコープの終わりに自動呼出しされる） */
	public void closeConnection(@Disposes Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				log.trace("=== closed Connection ===");
				conn.close();
			}
		} catch (SQLException e) {
			log.warn(e.getMessage(), e);
		}
	}
}
