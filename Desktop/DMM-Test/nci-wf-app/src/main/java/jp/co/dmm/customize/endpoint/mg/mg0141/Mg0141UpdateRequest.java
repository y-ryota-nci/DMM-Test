package jp.co.dmm.customize.endpoint.mg.mg0141;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0141UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 会社付加コード */
	public String companyAddedInfo;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目補助コード */
	public String accBrkdwnCd;
	/** 連番 */
	public String sqno;
	/** 勘定科目補助名称 */
	public String accBrkdwnNm;
	/** 勘定科目補助略称 */
	public String accBrkdwnNmS;
	/** 有効期間（開始） */
	@Temporal(TemporalType.DATE)
	public Date vdDtS;
	/** 有効期間（終了） */
	@Temporal(TemporalType.DATE)
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
}
