package jp.co.dmm.customize.endpoint.mg.mg0131;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 勘定科目マスタ設定画面エンティティ
 */

public class Mg0131Entity implements Serializable {
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

	public String ipCreated;

	public String ipUpdated;

	public String taxCdSs;

	public String taxIptTp;

	public Timestamp timestampCreated;

	public Timestamp timestampUpdated;

	public String userCodeCreated;

	public String userCodeUpdated;

	public Date vdDtE;

	public Date vdDtS;

}
