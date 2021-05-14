package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 取引先口座情報チェック結果レスポンス
 */
public class Sp0010GetcompanyResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 会社情報 */
	public List<String> companyList;

	/** 選択済み会社 */
	public String companyCds;
}
