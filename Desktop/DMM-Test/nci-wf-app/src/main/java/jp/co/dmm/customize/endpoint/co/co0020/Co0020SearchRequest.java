package jp.co.dmm.customize.endpoint.co.co0020;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 経常支払マスタ一覧の検索リクエスト
 */
public class Co0020SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;

	/** 契約No */
	public String cntrctNo;
	/** 契約件名 */
	public String cntrctNm;

	/** 経常支払No */
	public String rtnPayNo;

	/** 取引先コード */
	public String splrCd;
	/** 取引先名称（漢字） */
	public String splrNmKj;

	/** 支払開始年月 */
	public String payStartTime;
	/** 支払終了年月 */
	public String payEndTime;

	/** 契約主体部署コード */
	public String cntrctrDptCd;
	/** 契約主体部署名称 */
	public String cntrctrDptNm;

	/** 部門コード */
	public String bumonCd;
	public String bumonNm;

	/** 申請者 */
	public String sbmtrCd;
	public String sbmtrNm;

	/** 申請日 */
	public Date sbmDptSDt;
	public Date sbmDptEDt;

	/** 支払サイト */
	public String paySiteCd;
	public String paySiteNm;

	/** 組織コード */
	public String orgnzCd;
	public String orgnzNm;

	/** 金額 */
	public Double payAmtMin;
	public Double payAmtMax;
}
