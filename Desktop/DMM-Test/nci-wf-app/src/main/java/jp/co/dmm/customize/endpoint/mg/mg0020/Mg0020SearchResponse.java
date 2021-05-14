package jp.co.dmm.customize.endpoint.mg.mg0020;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;


public class Mg0020SearchResponse extends BasePagingResponse {
	/** 会社コード */
	public String companyCd;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;
}
