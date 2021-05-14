package jp.co.dmm.customize.endpoint.co.co0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 契約一覧の検索結果レスポンス
 */
public class Co0010SearchResponse extends BasePagingResponse {

	/** 会社コード */
	public String companyCd;
	/** 依頼種別の選択肢 */
	public List<OptionItem> cntrctshtFrmts;
}
