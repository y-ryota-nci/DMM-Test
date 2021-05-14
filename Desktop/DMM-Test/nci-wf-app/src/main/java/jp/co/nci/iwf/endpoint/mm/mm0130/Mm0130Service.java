package jp.co.nci.iwf.endpoint.mm.mm0130;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * メニュー編集サービス
 */
@BizLogic
public class Mm0130Service extends MmBaseService<WfmInformationSharerDef> implements CodeMaster {

	@Inject
	private Mm0130Repository repository;

	/**
	 * 初期化
	 *
	 * @param req
	 * @return
	 */
	public Mm0130InitResponse init(Mm0130InitRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		// 初期検索
		final Mm0130InitResponse res = createResponse(Mm0130InitResponse.class, req);

		// メニュー一覧取得
		List<MwmMenu> menuList = repository.getMenu(localeCode);
		// 子ノードを持っているマップを作成し、ツリー表示時のアイコン判定に使用する
		Map<String, Map<String, Set<String>>> childMap = createChildMap(menuList);

		String menuItemCode1 = "";
		String menuItemCode2 = "";
		res.treeItems = new ArrayList<Mm0130TreeItem>();
		// 階層別メニュー情報の追加
		for (MwmMenu src : menuList) {
			// 親ノード
			if (!menuItemCode1.equals(src.getMenuItemCode1())) {
				res.treeItems.add(new Mm0130TreeItem(src, hasChild("1", src, childMap)));
				menuItemCode2 = "";
			// 子ノードLv2
			} else if (!menuItemCode2.equals(src.getMenuItemCode2())) {
				res.treeItems.add(new Mm0130TreeItem(src, "2", hasChild("2", src, childMap)));
				menuItemCode2 = src.getMenuItemCode2();
			// 子ノードLv3
			} else {
				res.treeItems.add(new Mm0130TreeItem(src, "3", false));
				menuItemCode2 = src.getMenuItemCode2();
			}
			menuItemCode1 = src.getMenuItemCode1();
		}

		res.success = true;
		return res;
	}

	/**
	 * 子が存在するかチェックするためのマップ作成
	 * @param menuList
	 * @return
	 * MwmMenuのURLで判定するとたまに矛盾があるため、ノードと同じ構成のMapを作成して判定する
	 */
	private Map<String, Map<String, Set<String>>> createChildMap(List<MwmMenu> menuList) {
		Map<String, Map<String, Set<String>>> childMap = new HashMap<String, Map<String, Set<String>>>();
		for (MwmMenu src : menuList) {
			if (childMap.containsKey(src.getMenuItemCode1())) {
				Map<String, Set<String>> childMap2 = childMap.get(src.getMenuItemCode1());
				if (childMap2.containsKey(src.getMenuItemCode2())) {
					childMap.get(src.getMenuItemCode1()).get(src.getMenuItemCode2()).add(src.getMenuItemCode3());
				} else {
					Set<String> set = new HashSet<String>();
					set.add(src.getMenuItemCode3());
					childMap.get(src.getMenuItemCode1()).put(src.getMenuItemCode2(), set);
				}
			} else {
				Map<String, Set<String>> map = new HashMap<String, Set<String>>();
				Set<String> set = new HashSet<String>();
				set.add(src.getMenuItemCode3());
				map.put(src.getMenuItemCode2(), set);
				childMap.put(src.getMenuItemCode1(), map);
			}
		}
		return childMap;
	}

	/**
	 * ノードに子が存在するか
	 * @param lvl 1 or 2
	 * @param src MwmMenu
	 * @param childMap
	 * @return
	 */
	private boolean hasChild(String lvl, MwmMenu src, Map<String, Map<String, Set<String>>> childMap) {
		boolean result = false;
		Map<String, Set<String>> map = childMap.get(src.getMenuItemCode1());
		if ("1".equals(lvl)) {
			for (String code2 : map.keySet()) {
				int srcCode2 = Integer.valueOf(src.getMenuItemCode2());
				int keyCode2 = Integer.valueOf(code2);
				if (srcCode2 < keyCode2) {
					result = true;
					break;
				}
			}
		} else if ("2".equals(lvl)) {
			for (String code3 : map.get(src.getMenuItemCode2())) {
				int srcCode3 = Integer.valueOf(src.getMenuItemCode3());
				int keyCode3 = Integer.valueOf(code3);
				if (srcCode3 < keyCode3) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 更新処理
	 *
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0130SaveResponse update(Mm0130SaveRequest req) {
		final Mm0130SaveResponse res = createResponse(Mm0130SaveResponse.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Long menuId = Long.valueOf(req.menuId);
		final String menuName = req.menuName;
		final List<MwmMenu> menuList = repository.getMenu(localeCode);

		for (MwmMenu menu : menuList) {
			if (menu.getMenuId() == menuId) {
				if (!menu.getMenuName().equals(menuName)) {
					menu.setMenuName(menuName);
					repository.updateMwmMenu(menu, localeCode);
				}
				break;
			}
		}
		// 更新したメニュー名称をセット
		res.menuName = menuName;
		res.success = true;
		return res;
	}


}
