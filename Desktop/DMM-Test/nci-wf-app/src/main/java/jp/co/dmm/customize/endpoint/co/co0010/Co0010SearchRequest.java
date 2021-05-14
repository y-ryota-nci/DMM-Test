package jp.co.dmm.customize.endpoint.co.co0010;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 契約一覧の検索リクエスト
 */
public class Co0010SearchRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 契約NO */
	public String cntrctNo;
	/** 契約件名 */
	public String cntrctNm;
	/** 契約期間（開始） */
	public Date cntrctPrdSDt;
	/** 契約期間（終了） */
	public Date cntrctPrdEDt;
	/** 取引先名称（漢字） */
	public String splrNmKj;
	/** 依頼種別 */
	public String cntrctshtFrmt;
}
