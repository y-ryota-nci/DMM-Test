package jp.co.dmm.customize.endpoint.mg.mg0141;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0141GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0141Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgNm;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
