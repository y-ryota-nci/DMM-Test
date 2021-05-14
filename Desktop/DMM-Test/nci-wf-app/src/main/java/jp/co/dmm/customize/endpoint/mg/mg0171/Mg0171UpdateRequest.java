package jp.co.dmm.customize.endpoint.mg.mg0171;

import java.math.BigDecimal;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 通貨マスタ更新リクエスト
 *
 */
public class Mg0171UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 通貨コード */
	public String mnyCd;
	/** 通貨名称 */
	public String mnyNm;
	/** 通貨記号 */
	public String mnyMrk;
	/** 小数点桁数 */
	public BigDecimal rdxpntGdt;
	/** ソート順 */
	public BigDecimal sortOrder;
	/** 削除フラグ */
	public String dltFg;
}
