package jp.co.nci.iwf.endpoint.vd.vd0062;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

public class Vd0062Response extends BaseResponse {

	/** パーツ選択肢マスタ */
	public MwmOption entity;
	/** パーツ選択肢項目マスタ一覧 */
	public List<MwmOptionItem> items;
}
