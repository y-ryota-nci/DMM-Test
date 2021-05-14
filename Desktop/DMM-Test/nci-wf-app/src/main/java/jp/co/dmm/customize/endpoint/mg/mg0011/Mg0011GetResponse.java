package jp.co.dmm.customize.endpoint.mg.mg0011;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0011GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0011Entity entity;

	/** 在庫区分選択肢 */
	public List<OptionItem> stckTps;

	/** 調達部門選択肢 */
	public List<OptionItem> prcFldTps;

	/** 単位コード選択肢 */
	public List<OptionItem> untCds;

	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgNm;

	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
