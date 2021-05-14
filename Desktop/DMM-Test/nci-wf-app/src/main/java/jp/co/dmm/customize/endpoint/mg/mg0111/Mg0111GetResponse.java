package jp.co.dmm.customize.endpoint.mg.mg0111;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0111GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0111Entity entity;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFg;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;
}
