package jp.co.dmm.customize.endpoint.mg.mg0160;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 消費税マスタ検索レスポンス
 *
 */
public class Mg0160SearchResponse extends BasePagingResponse {
	/** 会社コード */
	public String companyCd;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;
}
