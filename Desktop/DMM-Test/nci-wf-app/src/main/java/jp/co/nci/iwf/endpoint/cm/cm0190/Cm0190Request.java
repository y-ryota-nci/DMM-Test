package jp.co.nci.iwf.endpoint.cm.cm0190;

import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 遷移先アクティビティ選択画面のリクエスト
 */
public class Cm0190Request extends BaseRequest {

	public String corporationCode;
	public long processId;
	public TrayType trayType;
}
