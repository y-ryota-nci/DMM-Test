package jp.co.dmm.customize.endpoint.mg.mg0320;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 消費税関連マスタ検索レスポンス
 *
 */
public class Mg0320SearchResponse extends BasePagingResponse {
	/** 会社コード */
	public String companyCd;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;

	/** 消費税種類コード選択肢 */
	public List<OptionItem> taxKndCdItems;

	/** 消費税種別選択肢 */
	public List<OptionItem> taxSpcItems;
}
