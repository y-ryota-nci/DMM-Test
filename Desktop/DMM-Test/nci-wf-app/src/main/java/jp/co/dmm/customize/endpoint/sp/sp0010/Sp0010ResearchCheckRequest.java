package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 取引先情報名寄せチェックの検索リクエスト
 */
public class Sp0010ResearchCheckRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 取引先コード */
	public String splrCd;
	/** 取引先名称（漢字） */
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	public String splrNmKn;
	/** 取引先名称（英名） */
	public String splrNmE;
	/** 住所（都道府県）コード */
	public String adrPrfCd;
	/** 会社コードリスト */
	public String companyCds;
	/** 振込先銀行リスト */
	public List<String> bnkaccList;
}
