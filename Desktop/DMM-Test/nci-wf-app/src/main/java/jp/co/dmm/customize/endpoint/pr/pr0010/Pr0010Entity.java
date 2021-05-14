package jp.co.dmm.customize.endpoint.pr.pr0010;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 管理_購入依頼の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Pr0010Entity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="PURRQST_NO")
	public String purrqstNo;
	@Column(name="COMPANY_CD")
	public String companyCd;
	@Column(name="PURRQST_NM")
	public String purrqstNm;
	@Column(name="PURRQST_AMT")
	public BigDecimal purrqstAmt;
	@Column(name="PRC_FLD_TP_NM")
	public String prcFldTpNm;
	@Column(name="SCREEN_CODE")
	public String screenCode;

}
