package jp.co.dmm.customize.endpoint.mg.mg0101;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0101GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0101Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
