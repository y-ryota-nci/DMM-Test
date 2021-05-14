package jp.co.nci.iwf.endpoint.vd.vd0060;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 選択肢一覧の検索リクエスト
 */
public class Vd0060SearchRequest extends BasePagingRequest {

	/** 企業コード */
	public String corporationCode;
	/** 選択肢コード */
	public String optionCode;
	/** 選択肢名 */
	public String optionName;
}
