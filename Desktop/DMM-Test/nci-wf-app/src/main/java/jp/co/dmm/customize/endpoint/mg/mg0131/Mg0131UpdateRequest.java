package jp.co.dmm.customize.endpoint.mg.mg0131;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0131UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 会社付加コード */
	public String companyAddedInfo;
	/** 勘定科目コード */
	public String accCd;
	/** 連番 */
	public String sqno;
	/** 勘定科目名称 */
	public String accNm;
	/** 勘定科目略称 */
	public String accNmS;
	/** 貸借区分 */
	public String dcTp;
	/** 勘定科目補助区分 */
	public String accBrkdwnTp;
	/** 税処理コード */
	public String taxCdSs;
	/** 税入力区分 */
	public String taxIptTp;
	/** 有効期間（開始） */
	@Temporal(TemporalType.DATE)
	public Date vdDtS;
	/** 有効期間（終了） */
	@Temporal(TemporalType.DATE)
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
