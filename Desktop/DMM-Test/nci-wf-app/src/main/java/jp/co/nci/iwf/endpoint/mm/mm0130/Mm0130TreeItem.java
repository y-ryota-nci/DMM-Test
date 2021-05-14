package jp.co.nci.iwf.endpoint.mm.mm0130;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jp.co.nci.iwf.jpa.entity.mw.MwmMenu;

/**
 * メニュー階層設定画面のツリーデータ
 */
public class Mm0130TreeItem implements Serializable, MmMenuName {

	/**  */
	private static final long serialVersionUID = 1L;

	public String id;
	public String parent;
	public String text;
	public String icon;
	public String type;
	public Map<String, Object> data;
	public Map<String, Boolean> state;
	public Map<String, String> a_attr;

	// 親ノード
	public Mm0130TreeItem(MwmMenu menu, boolean hasChild) {
		this.id = menu.getMenuItemCode1();
		this.parent = TreeItem.PARENT;
		this.text = menu.getMenuName();
		if (hasChild) {
			this.icon = Icon.BOOK;
			this.type = Type.BOOK;
		} else {
			this.icon = Icon.FILE;
			this.type = Type.FILE;
			this.a_attr = new HashMap<String, String>();
			this.a_attr.put("data-type", Type.FILE);
		}
		this.state = new HashMap<>();
		this.state.put(State.OPENED, false);
		this.data = new HashMap<>();
		this.data.put(DataKey.MENU_ID, String.valueOf(menu.getMenuId()));
		this.data.put(DataKey.MENU_NAME, menu.getMenuName());
	}

	// 子ノード（Lv2,Lv3）
	public Mm0130TreeItem(MwmMenu menu, String lvl, boolean hasChild) {
		if (Lvl.LEVEL2.equals(lvl)) {
			this.id = menu.getMenuItemCode1() + "_" + menu.getMenuItemCode2();
			this.parent = menu.getMenuItemCode1();
			if (hasChild) {
				this.icon = Icon.BOOK;
				this.type = Type.BOOK;
			} else {
				this.icon = Icon.FILE;
				this.type = Type.FILE;
				this.a_attr = new HashMap<String, String>();
				this.a_attr.put("data-type", Type.FILE);
			}
		} else 	if (Lvl.LEVEL3.equals(lvl)) {
			this.id = menu.getMenuItemCode1() + "_" + menu.getMenuItemCode2() + "_" + menu.getMenuItemCode3();
			this.parent = menu.getMenuItemCode1() + "_" + menu.getMenuItemCode2();
			this.icon = Icon.FILE;
			this.type = Type.FILE;
			this.state = new HashMap<>();
			this.a_attr = new HashMap<String, String>();
			this.a_attr.put("data-type", Type.FILE);
		}
		this.state = new HashMap<>();
		this.state.put(State.OPENED, false);
		this.text = menu.getMenuName();
		this.data = new HashMap<>();
		this.data.put(DataKey.MENU_ID, String.valueOf(menu.getMenuId()));
		this.data.put(DataKey.MENU_NAME, menu.getMenuName());
	}

}
