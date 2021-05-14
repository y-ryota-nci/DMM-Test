package jp.co.nci.iwf.endpoint.cm.cm0060;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.tray.BaseTrayRequest;

/**
 * 関連文書選択画面の検索リクエスト
 */
public class Cm0060SearchRequest extends BaseTrayRequest {
	public String corporationCode;
	public Long processId;
	public TrayType trayType;
	public String notInProcessIds;
}
