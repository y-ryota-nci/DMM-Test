package jp.co.dmm.customize.endpoint.mg.mg0271;

import java.math.BigDecimal;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 予算科目マスタ更新リクエスト
 *
 */
public class Mg0271UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 予算科目コード */
	public String bgtItmCd;
	/** 予算科目名称 */
	public String bgtItmNm;
	/** BS/BL区分 */
	public String bsPlTp;
	/** ソート順 */
	public BigDecimal sortOrder;
	/** 削除フラグ */
	public String dltFg;
}
