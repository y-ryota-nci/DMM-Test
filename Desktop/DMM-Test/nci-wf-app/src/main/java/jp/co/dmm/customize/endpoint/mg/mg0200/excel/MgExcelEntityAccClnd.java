package jp.co.dmm.customize.endpoint.mg.mg0200.excel;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityAccClnd extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 日付 */
	@Temporal(TemporalType.DATE)
	@Column(name="CLND_DT")
	public java.util.Date clndDt;
	/** 曜日 */
	@Column(name="CLND_DAY")
	public String clndDay;
	/** 曜日名称 */
	@Column(name="CLND_DAY_NM")
	public String clndDayNm;
	/** 休日区分 */
	@Column(name="HLDAY_TP")
	public String hldayTp;
	/** 休日区分名称 */
	@Column(name="HLDAY_TP_NM")
	public String hldayTpNm;
	/** 銀行休日区分 */
	@Column(name="BNK_HLDAY_TP")
	public String bnkHldayTp;
	/** 銀行休日区分名称 */
	@Column(name="BNK_HLDAY_TP_NM")
	public String bnkHldayTpNm;
	/** 決済区分(購買) */
	@Column(name="STL_TP_PUR")
	public String stlTpPur;
	/** 決済区分(購買)名称 */
	@Column(name="STL_TP_PUR_NM")
	public String stlTpPurNm;
	/** 決済区分(債務) */
	@Column(name="STL_TP_FNCOBL")
	public String stlTpFncobl;
	/** 決済区分(債務)名称 */
	@Column(name="STL_TP_FNCOBL_NM")
	public String stlTpFncoblNm;
	/** 決済区分(財務) */
	@Column(name="STL_TP_FNCAFF")
	public String stlTpFncaff;
	/** 決済区分(財務)名称 */
	@Column(name="STL_TP_FNCAFF_NM")
	public String stlTpFncaffNm;
	/** 月次締め時間 */
	@Column(name="ML_CLS_TM")
	public String mlClsTm;
	/** 月次締め時間(Format) */
	@Column(name="ML_CLS_TM_FMT")
	public String mlClsTmFmt;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 作成会社コード */
	@Column(name="CORPORATION_CODE_CREATED")
	public String corporationCodeCreated;
	/** 作成ユーザコード */
	@Column(name="USER_CODE_CREATED")
	public String userCodeCreated;
	/** 作成ユーザIPアドレス */
	@Column(name="IP_CREATED")
	public String ipCreated;
	/** 作成日時 */
	@Column(name="TIMESTAMP_CREATED")
	public Timestamp timestampCreated;
	/** 更新会社コード */
	@Column(name="CORPORATION_CODE_UPDATED")
	public String corporationCodeUpdated;
	/** 更新ユーザコード */
	@Column(name="USER_CODE_UPDATED")
	public String userCodeUpdated;
	/** 更新ユーザIPアドレス */
	@Column(name="IP_UPDATED")
	public String ipUpdated;
	/** 更新日時 */
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;

	/** 有効開始日付 文字列 */
	public String strClndDt;
}
