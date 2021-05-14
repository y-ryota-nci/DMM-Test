package jp.co.dmm.customize.endpoint.sp.sp0040;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 反社情報一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Sp0040Entity extends BaseJpaEntity implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RUNTIME_ID")
	public Long runtimeId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="PROCESS_ID")
	public Long processId;
	@Column(name="PARENT_RUNTIME_ID")
	public Long parentRuntimeId;
	@Column(name="SORT_ORDER")
	public Integer sortOrder;
	@Column(name="COMPANY_CD")
	public String companyCd;
	@Column(name="SPLR_CD")
	public String splrCd;
	@Column(name="PEID")
	public String peid;
	@Column(name="MTCH_NM")
	public String mtchNm;
	@Column(name="LND_CD")
	public String lndCd;
	@Column(name="LND_NM")
	public String lndNm;
	@Column(name="LND_CD_DJII")
	public String lndCdDjii;
	@Column(name="GND_TP")
	public String gndTp;
	@Column(name="BRTH_DT")
	public String brthDt;

}
