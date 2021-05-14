package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 取引先一覧の検索結果レスポンス
 */
public class Sp0010SearchResponse extends BasePagingResponse {

	/** 都道府県選択肢 */
	public List<OptionItem> adrPrfCds;

	/** HDフラグ */
	public String hdFlag;

	/** 会社コード */
	public String companyCd;
	public String companyNm;
	public String companyAddedInfo;

}
