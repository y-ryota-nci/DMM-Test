package jp.co.dmm.customize.endpoint.mg.mg0091;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0091GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0091Entity entity;

	/** 銀行口座種別選択肢 */
	public List<OptionItem> bnkaccTpNm;
	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgNm;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
