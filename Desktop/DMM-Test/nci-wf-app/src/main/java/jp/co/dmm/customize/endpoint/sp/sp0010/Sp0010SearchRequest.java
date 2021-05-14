package jp.co.dmm.customize.endpoint.sp.sp0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 取引先一覧の検索リクエスト
 */
public class Sp0010SearchRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 会社名 */
	public String companyNm;
	/** 会社付加コード */
	public String companyAddedInfo;
	/** 取引先コード */
	public String splrCd;
	/** 取引先名称（漢字） */
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	public String splrNmKn;
	/** 取引先名称（英名） */
	public String splrNmE;
	/** 法人・個人区分（法人） */
	public boolean crpPrsTp1;
	/** 法人・個人区分（個人） */
	public boolean crpPrsTp2;
	/** 住所（都道府県）コード */
	public String adrPrfCd;
	/** 取引状況区分（利用前） */
	public boolean trdStsTp1;
	/** 取引状況区分（利用中） */
	public boolean trdStsTp2;
	/** 取引状況区分（利用停止） */
	public boolean trdStsTp3;
}
