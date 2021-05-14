package jp.co.dmm.customize.endpoint.bd.bd0803;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 予算履歴メンテナンス画面用エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Bd0803Entity extends BaseJpaEntity {
	@Id
	@Column(name="ID")
	public long id;

	@Column(name="COMPANY_CD")
	public String companyCd;

	@Column(name="YR_CD")
	public String yrCd;

	@Column(name="ORGANIZATION_CODE")
	public String organizationCode;

	@Column(name="RCV_COST_PAY_TP")
	public String rcvCostPayTp;

	@Column(name="HST_VERSION")
	public long hstVersion;

	@Column(name="HST_DT")
	public java.sql.Date hstDt;

	@Column(name="HST_NM")
	public String hstNm;

	@Column(name="HST_RMK")
	public String hstRmk;

	@Column(name="USER_CODE_CREATED")
	public String userCodeCreated;

	@Column(name="USER_NAME_CREATED")
	public String userNameCreated;

	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;
}
