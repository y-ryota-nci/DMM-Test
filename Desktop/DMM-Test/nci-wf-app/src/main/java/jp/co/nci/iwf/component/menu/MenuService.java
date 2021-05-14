package jp.co.nci.iwf.component.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.HtmlResourceService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * メニューサービス
 */
@ApplicationScoped
public class MenuService extends BaseService implements CodeBook {
	@Inject private HtmlResourceService htmlCache;

	/** フッターHTMLファイルを読み込んで、そのHTMLを返す */
	public String getFooterHtml() {
		return htmlCache.getContents("footer.html");
	}

	/** メニューHTMLファイルを読み込んで、そのHTMLを返す */
	public String getMenuHtml() {
		// キャッシュにあるか？
		return htmlCache.getContents("menu.html");
	}

	/** メニューデータ生成 */
	public List<MenuEntry> getUserMenus(List<MwmMenu> accessibleMenus) {
		final List<MenuEntry> menus = new ArrayList<MenuEntry>();
		accessibleMenus.stream()
		.collect(Collectors.groupingBy(MwmMenu :: getMenuItemCode1))
		.entrySet()
		.stream()
		.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
		.map(Map.Entry::getValue)
		.forEach(accMenus -> {
			MenuEntry menu = new MenuEntry(accMenus.get(0).getUrl(), accMenus.get(0).getMenuName());
			menus.add(menu);

			accMenus.stream()
			.filter(accMenu -> !"0000".equals(accMenu.getMenuItemCode2()))
			.collect(Collectors.groupingBy(MwmMenu :: getMenuItemCode2))
			.entrySet()
			.stream()
			.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
			.map(Map.Entry::getValue)
			.forEach(accMenus2 -> {
				MenuEntry menu2 = new MenuEntry(accMenus2.get(0).getUrl(), accMenus2.get(0).getMenuName());
				menu.getChildren().add(menu2);

				accMenus2.stream()
				.sorted((e1, e2) -> (int)(e1.getMenuId() - e2.getMenuId()))
				.filter(accMenu2 -> !"0000".equals(accMenu2.getMenuItemCode3()))
				.forEach(accMenu3 -> {
//					System.out.println(accMenu3.getMenuName());
					menu2.getChildren().add(new MenuEntry(accMenu3.getUrl(), accMenu3.getMenuName()));
				});
			});
		});
		return menus;
	}
}
