package jp.co.dmm.customize.endpoint.bd.bd0809;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 予算分析明細確認画面エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Bd0809Entity extends BaseJpaEntity {
	@Id
	@Column(name="ID")
	public long id;

	@Column(name="COMPANY_CD")
	public String companyCd;

	@Column(name="APPLICATION_NO")
	public String applicationNo;

	@Column(name="APPLICATION_DTL_NO")
	public Integer applicationDtlNo;

	@Column(name="SUBJECT")
	public String subject;

	@Column(name="SPLR_CD")
	public String splrCd;

	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;

	@Column(name="ITM_CD")
	public String itmCd;

	@Column(name="ITM_NM")
	public String itmNm;

	@Column(name="ITMEXPS_CD1")
	public String itmexpsCd1;

	@Column(name="ITMEXPS_NM1")
	public String itmexpsNm1;

	@Column(name="ITMEXPS_CD2")
	public String itmexpsCd2;

	@Column(name="ITMEXPS_NM2")
	public String itmexpsNm2;

	@Column(name="BUMON_CD")
	public String bumonCd;

	@Column(name="AMT")
	public BigDecimal amt;

	@Column(name="CST_ADD_YM")
	public String cstAddYm;

	@Column(name="ORGANIZATION_CODE_LV3")
	public String organizationCodeLv3;

	@Column(name="BGT_ITM_CD")
	public String bgtItmCd;

	@Column(name="SBMTR_CD")
	public String sbmtrCd;

	@Column(name="SBMTR_NM")
	public String sbmtrNm;

	@Column(name="SBMT_DPT_NM")
	public String sbmtDptNm;
}
