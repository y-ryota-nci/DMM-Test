package jp.co.nci.iwf.endpoint.mm.mm0040;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;

/**
 * 業務管理項目名称設定のレスポンス
 */
public class Mm0040Response extends BaseResponse {

	/** 表示条件フラグの選択肢 */
	public List<OptionItem> validFlags;
	/** 画面パーツ入力可否フラグの選択肢 */
	public List<OptionItem> screenPartsInputFlags;
	/** データ型の選択肢 */
	public List<OptionItem> dataTypes;
	/** 文書管理項目名称リスト. */
	public List<MwmDocBusinessInfoName> businessInfoNames;
}
