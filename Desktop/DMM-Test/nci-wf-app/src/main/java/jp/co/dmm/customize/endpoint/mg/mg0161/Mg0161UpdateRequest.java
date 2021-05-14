package jp.co.dmm.customize.endpoint.mg.mg0161;

import java.math.BigDecimal;
import java.util.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 消費税マスタ更新リクエスト
 *
 */
public class Mg0161UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 消費税コード */
	public String taxCd;
	/** 連番 */
	public Long sqno;
	/** 消費税名称 */
	public String taxNm;
	/** 消費税略称 */
	public String taxNmS;
	/** 消費税率 */
	public BigDecimal taxRto;
	/** 税処理区分 */
	public String taxTp;
	/** 税処理コード（SuperStream） */
	public String taxCdSs;
	/** 端数処理単位 */
	public String frcUnt;
	/** 端数処理区分 */
	public String frcTp;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目補助コード */
	public String accBrkdwnCd;
	/** 正残区分 */
	public String dcTp;
	/** 有効期間（開始） */
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;
	public String dltFg;
}
