package jp.co.dmm.customize.endpoint.mg.mg0191;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0191GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0191Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgNm;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

	/** サイトコード選択肢 */
	public List<OptionItem> siteCdItems;

	/** 分類コード選択肢 */
	public List<OptionItem> tpCdItems;

	/** 消費税種類コード選択肢 */
	public List<OptionItem> taxKndCdItems;

}
