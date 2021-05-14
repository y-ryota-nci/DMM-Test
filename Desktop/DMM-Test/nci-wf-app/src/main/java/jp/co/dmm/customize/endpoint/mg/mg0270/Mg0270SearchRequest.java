package jp.co.dmm.customize.endpoint.mg.mg0270;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 予算科目マスタ検索リクエスト
 *
 */
public class Mg0270SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 予算科目コード */
	public String bgtItmCd;
	/** 予算科目名称 */
	public String bgtItmNm;
	/** BS/PL区分 */
	public String bsPlTp;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
