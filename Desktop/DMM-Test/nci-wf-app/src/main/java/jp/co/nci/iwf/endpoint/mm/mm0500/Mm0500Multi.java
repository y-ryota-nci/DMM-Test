package jp.co.nci.iwf.endpoint.mm.mm0500;

import java.util.HashMap;
import java.util.Map;

public class Mm0500Multi {
	public Mm0500Multi(String tableName, String columnName, long idSrc, long idDest) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.idSrc = idSrc;
		this.idDest = idDest;
	}

	public String tableName;
	public long idSrc;
	public long idDest;
	public String columnName;
	public Map<String, String> valueByLocaleCodes = new HashMap<>();
}
