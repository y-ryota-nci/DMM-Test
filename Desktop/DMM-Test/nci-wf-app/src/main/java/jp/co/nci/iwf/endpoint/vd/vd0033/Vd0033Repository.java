package jp.co.nci.iwf.endpoint.vd.vd0033;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 画面Javascript設定画面リポジトリ
 */
@ApplicationScoped
public class Vd0033Repository extends BaseRepository {

	public List<Vd0033Entity> getMwmJavascript(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(Vd0033Entity.class, getSql("VD0033_01"), params);
	}

}
