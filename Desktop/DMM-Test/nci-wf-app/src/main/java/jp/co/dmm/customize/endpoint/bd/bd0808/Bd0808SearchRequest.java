package jp.co.dmm.customize.endpoint.bd.bd0808;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 特定組織指定バージョン予算／実績分析画面の検索リクエスト
 */
public class Bd0808SearchRequest extends BasePagingRequest {
	/** 会社CD */
	public String companyCd;
	/** 年度 */
	public String yrCd;
	/** 本部 */
	public String organizationCodeLv2;
	/** 部・室 */
	public String organizationCodeLv3;
	/** 検収／支払基準 */
	public String rcvCostPayTp;
	/** 予算科目CD */
	public String bgtItmCd;
	/** 履歴バージョン */
	public Integer hstVersion;
}
