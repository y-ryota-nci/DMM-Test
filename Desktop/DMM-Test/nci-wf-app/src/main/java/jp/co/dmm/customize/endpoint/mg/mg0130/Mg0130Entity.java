package jp.co.dmm.customize.endpoint.mg.mg0130;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 勘定科目マスタ一覧の検索結果エンティティ
 */

public class Mg0130Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 連番 */
	public Long sqno;

	public String accBrkdwnTp;

	public String accNm;

	public String accNmS;

	public String corporationCodeCreated;

	public String corporationCodeUpdated;

	public String dcTp;

	public String dltFg;


	public String taxCdSs;

	public String taxIptTp;

	public Timestamp timestampCreated;

	public Timestamp timestampUpdated;

	public String userCodeCreated;

	public String userCodeUpdated;

	public Date vdDtE;

	public Date vdDtS;

	/** 貸借区分名称 */
	public String dcTpNm;
	/** 勘定科目補助区分 */
	public String accBrkdwnTpNm;
	/** 税入力区分名称 */
	public String taxIptTpNm;
	/** 削除フラグ名称 */
	public String dltFgNm;

	/** 有効期間（開始） */
	public String vdDtSStr;
	/** 有効期間（終了） */
	public String vdDtEStr;

}
