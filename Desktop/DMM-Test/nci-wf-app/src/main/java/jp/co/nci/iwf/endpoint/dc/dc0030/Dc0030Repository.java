package jp.co.nci.iwf.endpoint.dc.dc0030;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenDocLevel;

/**
 * 新規申請画面リポジトリ
 */
@ApplicationScoped
public class Dc0030Repository extends BaseRepository {

	public List<MwmScreenDocLevel> getScreenDocLevels(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0030_01"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);

		return select(MwmScreenDocLevel.class, sql.toString(), params.toArray());
	}

	public List<MwmScreenDocDef> getScreenDocDefs(
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

		StringBuilder sql = new StringBuilder(getSql("DC0030_02").replaceFirst("###REPLACE###", replace.toString()));
		return select(MwmScreenDocDef.class, sql, params.toArray());
	}

	public int getMaxLevel(String corporationCode, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("DC0030_03"));
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(corporationCode);
		return count(sql.toString(), params.toArray());
	}
}
