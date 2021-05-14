package jp.co.dmm.customize.endpoint.batch.rtnpay;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払予約リクエスト
 */
public class RtnPayRequest extends BasePagingRequest {

	/** 対象年月 */
	public String targetDate;
	/** 組織コード */
	public String organizationCodeStart;
	/** 役職コード */
	public String postCodeStart;
	/** 会社コードリスト */
	public List<String> companyCdList;
	/** 会社別支払依頼プロセス定義コード */
	public Map<String, String[]> targetPaymentMap;

	/** データ */
	public RtnPayData data;
}
