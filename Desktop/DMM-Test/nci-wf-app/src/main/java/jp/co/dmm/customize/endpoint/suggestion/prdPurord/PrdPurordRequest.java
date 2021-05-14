package jp.co.dmm.customize.endpoint.suggestion.prdPurord;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/** 定期発注条件登録のリクエスト */
public class PrdPurordRequest extends BasePagingRequest {
	/** 会社CD */
	public String companyCd;
	/** 発注予約No */
	public Long prdPurordNo;
	/** 支払サイトコード */
	public String paySiteCd;
}
