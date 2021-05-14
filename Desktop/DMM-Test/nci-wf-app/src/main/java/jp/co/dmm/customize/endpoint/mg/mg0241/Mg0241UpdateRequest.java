package jp.co.dmm.customize.endpoint.mg.mg0241;

import java.math.BigDecimal;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 支払サイトマスタ更新リクエスト
 *
 */
public class Mg0241UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払サイトコード */
	public String paySiteCd;
	/** 支払サイト名称 */
	public String paySiteNm;
	/** 支払サイト（月） */
	public String paySiteM;
	/** 支払サイト（日） */
	public String paySiteN;
	/** ソート順 */
	public BigDecimal sortOrder;
	/** 削除フラグ */
	public String dltFg;
}
