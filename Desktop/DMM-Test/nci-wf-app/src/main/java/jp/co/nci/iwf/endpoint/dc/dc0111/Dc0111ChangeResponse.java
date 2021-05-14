package jp.co.nci.iwf.endpoint.dc.dc0111;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 画面変更時のレスポンス.
 */
public class Dc0111ChangeResponse extends BaseResponse {

	/** */
	private static final long serialVersionUID = 1L;

	/** WF連携先画面の選択肢 */
	public List<OptionItem> screenProcessCodes;
}
