package jp.co.dmm.customize.endpoint.mg.mg0141;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 勘定科目補助マスタ設定画面エンティティ
 */

public class Mg0141Entity implements Serializable {
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

	public String corporationCodeCreated;

	public String corporationCodeUpdated;

	public String dltFg;

	public String ipCreated;

	public String ipUpdated;

	public Timestamp timestampCreated;

	public Timestamp timestampUpdated;

	public String userCodeCreated;

	public String userCodeUpdated;

	public Date vdDtE;

	public Date vdDtS;


	/** 勘定科目名称 */
	public String accNm;

}
