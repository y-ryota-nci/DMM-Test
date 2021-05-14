package jp.co.dmm.customize.endpoint.mg.mg0160;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消費税マスタ一覧の検索結果エンティティ
 */
public class Mg0160Entity implements Serializable {

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
	/** 税処理区分名称 */
	public String taxTpNm;
	/** 税処理コード（SuperStream） */
	public String taxCdSs;
	/** 端数処理単位 */
	public String frcUnt;
	/** 端数処理区分 */
	public String frcTp;
	/** 端数処理区分名称 */
	public String frcNm;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目名称 */
	public String accNm;
	/** 勘定科目補助コード */
	public String accBrkdwnCd;
	/** 勘定科目補助名称 */
	public String accBrkdwnNm;
	/** 正残区分 */
	public String dcTp;
	/** 正残区分名称 */
	public String dcTpNm;
	/** 有効期間（開始） */
	public Date vdDtS;
	/** 有効期間（終了） */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
	/** 削除フラグ名称 */
	public String dltFgNm;

	/** 有効期間（開始） */
	public String vdDtSStr;
	/** 有効期間（終了） */
	public String vdDtEStr;

}
