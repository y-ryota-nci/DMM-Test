package jp.co.dmm.customize.endpoint.mg.mg0160;

import java.math.BigDecimal;
import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 消費税マスタ検索リクエスト
 *
 */
public class Mg0160SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 消費税コード */
	public String taxCd;
	/** 消費税名称 */
	public String taxNm;
	/** 消費税率(開始) */
	public BigDecimal taxRtoFrom;
	/** 消費税率(終了) */
	public BigDecimal taxRtoTo;
	/** 税処理区分 */
	public boolean taxTp0;
	public boolean taxTp1;
	public boolean taxTp2;
	/** 有効期間（開始） */
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
