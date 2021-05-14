package jp.co.dmm.customize.endpoint.mg.mg0250;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 部門関連マスタ検索レスポンス
 *
 */
public class Mg0250SearchResponse extends BasePagingResponse {

	/** 会社コード */
	public String companyCd;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;
}
