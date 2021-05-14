package jp.co.dmm.customize.endpoint.mg.mg0131;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

public class Mg0131GetResponse extends BasePagingResponse {

	/** 対象のEntity */
	public Mg0131Entity entity;

	/** 貸借区分選択肢 */
	public List<OptionItem> dcTpNm;
	/** 勘定科目補助区分選択肢 */
	public List<OptionItem> accBrkdwnTpNm;
	/** 税入力区分選択肢 */
	public List<OptionItem> taxIptTpNm;
	/** 削除フラグ選択肢 */
	public List<OptionItem> dltFgNm;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;

}
