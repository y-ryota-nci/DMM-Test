package jp.co.dmm.customize.endpoint.mg.mg0030;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0030SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	public String orgnzNm;
	/** 費目コード（1） */
	public String itmExpsCd1;
	public String itmExpsNm1;
	/** 費目コード（2） */
	public String itmExpsCd2;
	public String itmExpsNm2;
	/** 仕訳コード */
	public String jrnCd;
	public String jrnNm;


	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
