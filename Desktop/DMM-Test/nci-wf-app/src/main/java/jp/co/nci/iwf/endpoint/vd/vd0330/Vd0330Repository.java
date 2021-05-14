package jp.co.nci.iwf.endpoint.vd.vd0330;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;

/**
 * 申請画面(参照)のリポジトリ
 */
@ApplicationScoped
public class Vd0330Repository extends BaseRepository {
	/** 画面コードから、画面情報を抽出 */
	public MwvScreen getScreen(String corporationCode, String screenCode, String localeCode) {
		Object[] params = { corporationCode, screenCode, localeCode };
		String sql = getSql("VD0330_01");
		return selectOne(MwvScreen.class, sql, params);
	}

}
