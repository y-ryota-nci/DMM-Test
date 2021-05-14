package jp.co.dmm.customize.endpoint.md;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 取引先マスタインポート用関係先マスタエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MdExcelRltPrtEntity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NO")
	public long no;
	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 連番 */
	@Column(name="SQNO")
	public String sqno;
	/** 関係先名 */
	@Column(name="RLT_PRT_NM")
	public String rltPrtNm;
	/** 法人・個人区分 */
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	/** 国コード */
	@Column(name="LND_CD")
	public String lndCd;
	/** 生年月日 */
	@Temporal(TemporalType.DATE)
	@Column(name="BRTH_DT")
	public Date brthDt;
	/** 一致件数 */
	@Column(name="MTCH_CNT")
	public String mtchCnt;
	/** 一致プロファイルID */
	@Column(name="MTCH_PEID")
	public String mtchPeid;
	/** 判定 */
	@Column(name="JDG_TP")
	public String jdgTp;
	/** コメント */
	@Column(name="RLT_PRT_RMK")
	public String rltPrtRmk;

	/** エラー */
	public String errorText;
	/** 生年月日 文字列 */
	public String strBrthDt;

}
