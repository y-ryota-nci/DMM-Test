package jp.co.dmm.customize.endpoint.bd.bd0803;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 予算履歴メンテナンスの初期化リクエスト
 */
public class Bd0803Request extends BasePagingRequest {
	/** 年度 */
	public String yrCd;
	/** 本部 */
	public String organizationCodeLv2;
	/** 部・室 */
	public String organizationCodeLv3;
	/** 検収／支払基準 */
	public String rcvCostPayTp;
	/** BS／PL区分 */
	public String bsplTp;

	/** 選択行(削除用) */
	public List<Bd0803Entity> selects;
}
