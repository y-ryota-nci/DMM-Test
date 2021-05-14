package jp.co.nci.iwf.endpoint.vd.vd0123;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;

@ApplicationScoped
public class Vd0123Repository extends BaseRepository {

	/**
	 * Javascriptファイル定義を抽出
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<Vd0123Entity> getMwmJavascript(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(Vd0123Entity.class, getSql("VD0123_01"), params);
	}

}
