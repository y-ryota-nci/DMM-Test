package jp.co.dmm.customize.endpoint.bd.bd0801;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 予算入力画面の検索リクエスト
 */
public class Bd0801SearchRequest extends BasePagingRequest {
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
	/** B/S／P/L */
	public String bsplTp;

	/** 入力内容 */
	public List<Bd0801Entity> inputs;
}
