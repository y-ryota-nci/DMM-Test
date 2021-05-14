package jp.co.nci.iwf.endpoint.mm.mm0130;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * メニュー編集画面リポジトリ
 */
@ApplicationScoped
public class Mm0130Repository extends BaseRepository {

	/** 多言語対応サービス */
	@Inject private MultilingalService multi;

	/**
	 * メニューを抽出
	 * @param localeCode
	 * @return
	 */
	public List<MwmMenu> getMenu(String localeCode) {

		final List<String> params = new ArrayList<>();
		String sql = getSql("MM0130_01");
		params.add(localeCode);

		return select(MwmMenu.class, sql, params.toArray());
	}

	/**
	 * メニュー名称の更新
	 * @param menu
	 * @param localeCode
	 */
	public void updateMwmMenu(MwmMenu menu, String localeCode) {

		em.merge(menu);
		// 多言語
		multi.save("MWM_MENU", menu.getMenuId(), "MENU_NAME", localeCode, menu.getMenuName());
	}
}
