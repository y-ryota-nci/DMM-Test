package jp.co.dmm.customize.endpoint.mg.mg0321;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 消費税関連マスタ取得レスポンス
 *
 */
public class Mg0321GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0321Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

	/**消費税種類コード選択肢 */
	public List<OptionItem> taxKndCdItems;

	/**消費税種別選択肢 */
	public List<OptionItem> taxSpcItems;

}
