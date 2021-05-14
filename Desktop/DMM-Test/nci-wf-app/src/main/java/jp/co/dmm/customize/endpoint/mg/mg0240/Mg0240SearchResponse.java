package jp.co.dmm.customize.endpoint.mg.mg0240;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 支払サイトマスタ検索レスポンス
 *
 */
public class Mg0240SearchResponse extends BasePagingResponse {

	/** 会社コード */
	public String companyCd;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;
	/** 支払サイト（月） */
	public List<OptionItem> paySiteMOpts;
	/** 支払サイト（日） */
	public List<OptionItem> paySiteNOpts;
}
