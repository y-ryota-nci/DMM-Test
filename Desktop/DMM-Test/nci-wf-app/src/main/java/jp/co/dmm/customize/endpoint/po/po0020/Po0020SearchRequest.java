package jp.co.dmm.customize.endpoint.po.po0020;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 管理_定期発注マスタ一覧の検索条件リクエスト
 */
public class Po0020SearchRequest extends BasePagingRequest {
	/** 会社CD */
	public String companyCd;
	/** 契約書No */
	public String cntrctNo;
	/** 発注No */
	public String purordNo;
	/** 定期発注No */
	public String prdPurordNo;
	/** 取引先CD */
	public String splrCd;
	/** 取引先名称 */
	public String splrNmKj;
	/** 支払開始年月 */
	public String payStartTime;
	/** 支払終了年月 */
	public String payEndTime;
	/** 月次計上区分:未払計上 */
	public boolean mlAddTpNot;
	/** 月次計上区分:前払計上 */
	public boolean mlAddTpPre;
}
