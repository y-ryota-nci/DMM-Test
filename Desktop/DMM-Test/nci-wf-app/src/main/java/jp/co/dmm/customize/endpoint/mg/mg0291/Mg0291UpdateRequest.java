package jp.co.dmm.customize.endpoint.mg.mg0291;

import java.math.BigDecimal;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 結合フロアマスタ更新リクエスト
 *
 */
public class Mg0291UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 結合フロアコード */
	public String bndFlrCd;
	/** 結合フロア名称 */
	public String bndFlrNm;
	/** ソート順 */
	public BigDecimal sortOrder;
	/** 削除フラグ */
	public String dltFg;
}
