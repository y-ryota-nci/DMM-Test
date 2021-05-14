package jp.co.nci.iwf.endpoint.wl.wl0037;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.tray.BaseTrayResponse;

/**
 * 一括承認一覧用レスポンス
 */
public class Wl0037Response extends BaseTrayResponse {

	/** */
	private static final long serialVersionUID = 1L;

	/** 代理ユーザ. */
	public List<OptionItem> proxyUserList;
}
