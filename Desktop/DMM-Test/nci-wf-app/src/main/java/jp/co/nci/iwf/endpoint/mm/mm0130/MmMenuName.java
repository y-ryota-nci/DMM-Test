package jp.co.nci.iwf.endpoint.mm.mm0130;

public interface MmMenuName {

	interface TreeItem {
		String ID = "home";
		String PARENT = "#";
		String TEXT = "";
	}

	interface Lvl {
		String LEVEL1 = "1";
		String LEVEL2 = "2";
		String LEVEL3 = "3";
	}

	interface Icon {
		String HOME = "fa fa-home";
		String LIST = "fa fa-list";
		String BOOK = "fa fa-folder-open";
		String FILE = "fa fa-file";
	}

	interface Type {
		String HOME = "home";
		String BOOK = "book";
		String FILE = "file";
	}

	interface State {
		String OPENED = "opened";
		String DISABLED = "disabled";
		String SELECTED = "selected";
	}

	interface DataKey {
		String MENU_ID = "menuId";
		String MENU_NAME = "menuName";
	}
}
