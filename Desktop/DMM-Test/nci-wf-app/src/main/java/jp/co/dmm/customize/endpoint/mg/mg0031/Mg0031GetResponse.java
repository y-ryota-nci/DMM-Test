package jp.co.dmm.customize.endpoint.mg.mg0031;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0031GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0031Entity entity;

	/** 資産区分選択肢 */
	public List<OptionItem> asstTps;

	/** 経費区分選択肢 */
	public List<OptionItem> cstTps;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgNm;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

	/** 課税対象区分 */
	public List<OptionItem> taxSbjTps;

}
