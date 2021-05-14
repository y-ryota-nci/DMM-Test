package jp.co.nci.iwf.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.BadRequestException;

import org.apache.commons.lang3.StringUtils;

/**
 * yyyy/MM/ddのフォーマッタ。
 * SimpleDateFormatのインスタンス生成が遅いので、
 * リクエストスコープで使いまわせるよう実装している。
 */
@RequestScoped
public class YmdFormatter {
	private SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");

	public String format(Date d) {
		if (d == null) return null;
		return f.format(d);
	}

	public Date parse(String s) {
		try {
			if (StringUtils.isEmpty(s)) return null;
			return f.parse(s);
		} catch (ParseException e) {
			throw new BadRequestException(e);
		}
	}

	public java.sql.Date parseSql(String s) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			Date d = f.parse(s);
			return new java.sql.Date(d.getTime());
		}
		catch (ParseException e) {
			try {
				return java.sql.Date.valueOf(s);
			}
			catch (IllegalArgumentException ex) {
				throw new BadRequestException(ex);
			}
		}
	}
}
