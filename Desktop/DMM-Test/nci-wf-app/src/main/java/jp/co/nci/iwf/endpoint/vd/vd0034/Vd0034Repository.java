package jp.co.nci.iwf.endpoint.vd.vd0034;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;

/**
 * 画面属性コピー設定画面リポジトリ
 */
@ApplicationScoped
public class Vd0034Repository extends BaseRepository {
	/** 指定コンテナと同一コンテナをルートコンテナとする画面一覧を抽出 */
	public List<MwvScreen> getScreens(Long containerId, String localeCode) {
		final Object[] params = { containerId, localeCode };
		List<MwvScreen> screens = select(MwvScreen.class, getSql("VD0034_01"), params);
		screens.forEach(s -> em.detach(s));
		return screens;
	}
}
