package jp.co.dmm.customize.endpoint.mg.mg0261;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ｸﾚｶ口座マスタ取得レスポンス
 *
 */
public class Mg0261GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0261Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

	/** 口座引落日 */
	public List<OptionItem> bnkaccChrgDts;

}
