package jp.co.nci.iwf.endpoint.na;

public interface NaCodeBook {

	interface TreeItem {
		String ID = "home";
		String PARENT = "#";
		String TEXT = "";
	}

	interface Icon {
		String HOME = "glyphicon glyphicon-home";
		String LIST = "glyphicon glyphicon-list";
		String BOOK = "glyphicon glyphicon-folder-open";
		String FILE = "glyphicon glyphicon-file";
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
		String SCREEN_PROCESS_LEVEL_ID = "screenProcessLevelId";
		String EXPANSION_FLAG = "expansionFlag";
		String SCREEN_PROCESS_ID = "screenProcessId";
		String VALID_START_DATE = "validStartDate";
		String VALID_END_DATE = "validEndDate";
		String SORT_ORDER = "sortOrder";
		String NEW_APPLY_DISPLAY_FLAG = "newApplyDisplayFlag";
	}
}
