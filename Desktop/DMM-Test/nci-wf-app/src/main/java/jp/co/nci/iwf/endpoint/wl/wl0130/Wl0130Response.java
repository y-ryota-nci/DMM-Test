package jp.co.nci.iwf.endpoint.wl.wl0130;

import java.util.List;

import jp.co.nci.iwf.component.tray.BaseTrayResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmUserDisplayColumnEx;
import jp.co.nci.iwf.jpa.entity.ex.MwmUserDisplayConditionEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmUserDisplay;

/**
 * ワークリスト用レスポンス
 */
public class Wl0130Response extends BaseTrayResponse {

	public List<MwmUserDisplay> userDisplayList;

	public List<MwmUserDisplayConditionEx> userDisplayConditionList;
	public List<MwmUserDisplayColumnEx> userDisplayColumnList;
}
