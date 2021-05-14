package jp.co.dmm.customize.endpoint.mg.mg0270;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 予算科目マスタ検索レスポンス
 *
 */
public class Mg0270SearchResponse extends BasePagingResponse {
	/** 会社コード */
	public String companyCd;
	/** 会社選択肢 */
	public List<OptionItem> companyItems;
	/** BS/PL区分 */
	public List<OptionItem> bsPlTps;
}
