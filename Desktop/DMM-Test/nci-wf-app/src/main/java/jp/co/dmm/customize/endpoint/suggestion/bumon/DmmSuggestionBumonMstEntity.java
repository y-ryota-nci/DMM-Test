package jp.co.dmm.customize.endpoint.suggestion.bumon;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;

import jp.co.dmm.customize.jpa.entity.mw.BumonMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 部門エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class DmmSuggestionBumonMstEntity extends BaseJpaEntity {
	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public BumonMstPK id;

	@Column(name="AREA_CD")
	public String areaCd;

	@Column(name="BUMON_NM")
	public String bumonNm;

	@Column(name="CORPORATION_CODE_CREATED")
	public String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	public String corporationCodeUpdated;

	@Column(name="DLT_FG")
	public String dltFg;

	@Column(name="ENTRP_CD")
	public String entrpCd;

	@Column(name="ENTRP_TP_CD")
	public String entrpTpCd;

	@Column(name="IP_CREATED")
	public String ipCreated;

	@Column(name="IP_UPDATED")
	public String ipUpdated;

	@Column(name="SITE_CD")
	public String siteCd;

	@Column(name="TAB_CD")
	public String tabCd;

	@Column(name="TIMESTAMP_CREATED")
	public Timestamp timestampCreated;

	@Version
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;

	@Column(name="TP_CD")
	public String tpCd;

	@Column(name="USER_CODE_CREATED")
	public String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	public String userCodeUpdated;

	// BumonMst.classでselectした部門コードをsuggestion.js内で取得するためには
	// id.bumonCdとしなければならない。
	// これだとSuggestion.setupのpropertyに設定できないため
	// 「bumonCd」で取得可能なカラムを追加
	@Column(name="BUMON_CD")
	public String bumonCd;

	@Column(name="TAX_KND_CD")
	public String taxKndCd;

}
