package jp.co.dmm.customize.endpoint.mg.mg0311;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 住所マスタ取得レスポンス
 *
 */
public class Mg0311GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0311Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
