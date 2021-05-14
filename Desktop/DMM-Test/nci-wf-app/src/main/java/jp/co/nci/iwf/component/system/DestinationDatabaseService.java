package jp.co.nci.iwf.component.system;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.sql.DataSource;
import javax.ws.rs.InternalServerErrorException;

/**
 * DB接続先情報
 * @author nakamura.mitsuyuki
 *
 */
@ApplicationScoped
public class DestinationDatabaseService {

	public static final String KEY = "DBCONNECT";
	/** JDBC接続文字列：SQLServerのプレフィックス */
	private static final String PREFIX_MSSQL = "jdbc:sqlserver://";
	/** JDBC接続文字列：Oracleのプレフィックス */
	private static final String PREFIX_ORACLE = "jdbc:oracle:thin:@";

	/** DB接続Driver */
	private String driver;
	/** DB接続先URL（すべて） */
	private String fullUrl;
	/** DB接続先URLオプション */
	private String extraUrl;
	/** DB接続先URL（省略版） */
	private String url;
	/** DB接続先ユーザID */
	private String user;
	/** DB接続先DBMS */
	private String databaseName;

	/**
	 * 初期化
	 */
	@PostConstruct
	public void init() {
		DataSource ds = CDI.current().select(DataSource.class).get();
		try (Connection conn = ds.getConnection()){
			final DatabaseMetaData m = conn.getMetaData();
			fullUrl = m.getURL();
			user = m.getUserName();
			driver = String.format("%s %s", m.getDriverName(), m.getDriverVersion());
			databaseName = m.getDatabaseProductVersion();
			parse();
		}
		catch (SQLException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 接続情報を解析して、安全に表示可能な内容へと丸める
	 */
	private void parse() {
		if (fullUrl.startsWith(PREFIX_MSSQL)) {
			// SQLServer
			int pos = PREFIX_MSSQL.length();
			String str = fullUrl.substring(pos);
			String[] astr = str.split(";");
			String server = astr[0];
			Map<String, String> map = new HashMap<>();
			for (int i = 1; i < astr.length; i++) {
				String[] keyValue = astr[i].split("=");
				String key = keyValue[0];
				String value = keyValue.length <= 1 ? "" : keyValue[1];
				map.put(key, value);
			}
			StringBuilder sb = new StringBuilder(server);
			if (map.containsKey("databaseName"))
				sb.append("/").append(map.get("databaseName"));
			this.url = sb.toString();
			this.extraUrl = "";
		}
		else if (fullUrl.startsWith(PREFIX_ORACLE)) {
			// Oracle
			int pos = PREFIX_ORACLE.length();
			this.url = fullUrl.substring(pos);
			this.extraUrl = "";
		}
		else if (fullUrl != null && fullUrl.indexOf('?') >= 0) {
			// 謎
			this.url = fullUrl.substring(0, fullUrl.indexOf('?'));
			this.extraUrl = fullUrl.substring(fullUrl.indexOf('?') + 1);
		}
		else {
			// イミフ
			this.url = fullUrl;
			this.extraUrl = "";
		}

	}

	/** DB接続Driver */
	public String getDriver() {
		return driver;
	}

	/** DB接続先URL（省略版） */
	public String getUrl() {
		return url;
	}

	/** DB接続先ユーザID */
	public String getUser() {
		return user;
	}

	/** DB接続先URL（すべて） */
	public String getFullUrl() {
		return fullUrl;
	}

	/** DB接続先URLオプション */
	public String getExtraUrl() {
		return extraUrl;
	}

	/** DB接続先DBMS */
	public String getDatabaseName() {
		return databaseName;
	}
}
