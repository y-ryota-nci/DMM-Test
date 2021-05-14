package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 取引先口座情報チェックリクエスト
 */
public class Sp0010AccountCheckRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 取引先コード */
	public String splrCd;
	/** 振込先口座コードリスト */
	public List<String> payeeBnkaccCdList;
}
