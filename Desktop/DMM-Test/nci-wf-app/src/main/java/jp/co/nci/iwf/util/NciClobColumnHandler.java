package jp.co.nci.iwf.util;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ColumnHandler;

/**
 * DBUtilsのBeanProcessorにClobを解釈させるためのカラムハンドラー。
 * ファイル 'META-INF/services/org.apache.commons.dbutils.ColumnHandler' に定義することで
 * ServiceLocatorが自動的に読み込んで DBUtilsが参照するようになっている。
 */
public class NciClobColumnHandler implements ColumnHandler {

	@Override
	public boolean match(Class<?> propType) {
		return propType.equals(Clob.class);
	}

	@Override
	public Object apply(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

}
