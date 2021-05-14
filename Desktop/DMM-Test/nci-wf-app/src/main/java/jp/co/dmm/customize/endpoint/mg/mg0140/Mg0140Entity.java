package jp.co.dmm.customize.endpoint.mg.mg0140;

import java.io.Serializable;
import java.util.Date;

/**
 * 勘定科目補助マスタ一覧の検索結果エンティティ
 */

public class Mg0140Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目補助コード */
	public String accBrkdwnCd;
	/** 連番 */
	public Long sqno;
	public String accBrkdwnNm;
	public String accBrkdwnNmS;
	public String dltFg;
	public Date vdDtE;
	public Date vdDtS;

	/** 勘定科目名称 */
	public String accNm;
	/** 削除フラグ名称 */
	public String dltFgNm;

	/** 有効期間（開始） */
	public String vdDtSStr;
	/** 有効期間（終了） */
	public String vdDtEStr;

}
