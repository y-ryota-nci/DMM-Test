package jp.co.nci.iwf.component.accesslog;

import java.io.Serializable;

public class ScreenInfo implements Serializable {
	/** 画面ID */
	private String screenId;
	/** アクション名 */
	private String actionName;
	/** 画面タイトル */
	private String title;



	public String getScreenId() {
		return screenId;
	}
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
