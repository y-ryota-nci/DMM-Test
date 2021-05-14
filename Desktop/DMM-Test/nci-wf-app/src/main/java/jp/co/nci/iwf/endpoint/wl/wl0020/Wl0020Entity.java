package jp.co.nci.iwf.endpoint.wl.wl0020;

import java.io.Serializable;

import jp.co.nci.iwf.component.CodeBook.TrayType;

/**
 * 個人用トレイ選択画面のエンティティ
 */
public class Wl0020Entity implements Serializable {
	public Long trayConfigPersonalizeId;
	public TrayType trayType;
	public String trayTypeName;
	public Long trayConfigId;
}
