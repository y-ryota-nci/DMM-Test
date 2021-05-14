package jp.co.nci.iwf.endpoint.na.na0010;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;

/**
 * 新規申請画面リポジトリ
 */
@ApplicationScoped
public class Na0010Repository extends BaseRepository {

	public List<MwmScreenProcessLevel> getScreenProcessLevels(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("NA0010_01"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);

		return select(MwmScreenProcessLevel.class, sql.toString(), params.toArray());
	}

	public List<MwmScreenProcessDef> getScreenProcessDefs(
			String corporationCode
			, Collection<String> menuRoleCds
			, String localeCode) {

		final List<Object> params = new ArrayList<>();
		params.add(localeCode);

		StringBuilder replace = new StringBuilder();
		for (String menuRoleCd : menuRoleCds) {
			replace.append(replace.length() == 0 ? "?" : ", ?");
			params.add(menuRoleCd);
		}
		params.add(corporationCode);

		StringBuilder sql = new StringBuilder(getSql("NA0010_02").replaceFirst("###REPLACE###", replace.toString()));
		return select(MwmScreenProcessDef.class, sql, params.toArray());
	}

	public int getMaxLevel(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("NA0010_03"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		return count(sql.toString(), params.toArray());
	}
}
