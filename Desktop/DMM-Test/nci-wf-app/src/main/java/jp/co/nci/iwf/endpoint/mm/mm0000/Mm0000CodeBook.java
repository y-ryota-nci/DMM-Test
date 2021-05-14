package jp.co.nci.iwf.endpoint.mm.mm0000;

interface Mm0000CodeBook {
	interface Type {
		String CORP = "corp";
		String ORG = "org";
		String USER = "user";
	}

	interface State {
		String OPENED = "opened";
		String DISABLED = "disabled";
		String SELECTED = "selected";
	}

	String DEFAULT_POST_ADDED_INFO = "DEFAULTS";
}
