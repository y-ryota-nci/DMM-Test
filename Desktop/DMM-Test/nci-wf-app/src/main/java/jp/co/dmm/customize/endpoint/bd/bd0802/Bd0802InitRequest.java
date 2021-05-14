package jp.co.dmm.customize.endpoint.bd.bd0802;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 予算計画履歴作成画面の初期化リクエスト
 */
public class Bd0802InitRequest extends BaseRequest {
	/** 年度 */
	public String yrCd;
	/** 本部 */
	public String organizationCodeLv2;
	/** 部・室 */
	public String organizationCodeLv3;
	/** 検収／支払基準 */
	public String rcvCostPayTp;
	/** B/S／P/L区分 */
	public String bsplTp;
}
