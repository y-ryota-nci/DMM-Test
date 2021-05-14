package jp.co.nci.iwf.component.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * メニューBean
 */
public class MenuEntry implements Serializable {
	/** リンク用URL */
	private String url;
	/** ラベル */
	private String label;
	/** 子要素 */
	private List<MenuEntry> children = new ArrayList<>();
	/** リンクのtarget */
	private String target;

	/** コンストラクタ */
	public MenuEntry() {}

	/** コンストラクタ */
	public MenuEntry(String url, String label) {
		this.url = url;
		this.label = label;
		this.target = "_self";
	}

	/** コンストラクタ */
	public MenuEntry(String url, String label, String target) {
		this.url = url;
		this.label = label;
		this.target = target;
	}

	/** リンク用URL */
	public String getUrl() {
		return url;
	}
	/** リンク用URL */
	public void setUrl(String url) {
		this.url = url;
	}

	/** ラベル */
	public String getLabel() {
		return label;
	}
	/** ラベル */
	public void setLabel(String label) {
		this.label = label;
	}

	/** 子要素 */
	public List<MenuEntry> getChildren() {
		return children;
	}
	/** 子要素 */
	public void setChildren(List<MenuEntry> children) {
		this.children = children;
	}

	/** リンクのtarget */
	public String getTarget() {
		return target;
	}
	/** リンクのtarget */
	public void setTarget(String target) {
		this.target = target;
	}
}
