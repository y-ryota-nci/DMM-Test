package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jp.co.nci.iwf.component.CodeBook.TrayType;


/**
 * The persistent class for the MWM_TRAY_CONFIG_PERSON database table.
 *
 */
@Entity
@Table(name="MWM_TRAY_CONFIG_PERSON", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "USER_CODE", "TRAY_TYPE"}))
@NamedQuery(name="MwmTrayConfigPerson.findAll", query="SELECT m FROM MwmTrayConfigPerson m")
public class MwmTrayConfigPerson extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TRAY_CONFIG_PERSONALIZE_ID")
	private long trayConfigPersonalizeId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="TRAY_CONFIG_ID")
	private Long trayConfigId;

	@Enumerated(EnumType.STRING)
	@Column(name="TRAY_TYPE")
	private TrayType trayType;

	@Column(name="USER_CODE")
	private String userCode;

	public MwmTrayConfigPerson() {
	}

	public long getTrayConfigPersonalizeId() {
		return this.trayConfigPersonalizeId;
	}

	public void setTrayConfigPersonalizeId(long trayConfigPersonalizeId) {
		this.trayConfigPersonalizeId = trayConfigPersonalizeId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getTrayConfigId() {
		return this.trayConfigId;
	}

	public void setTrayConfigId(Long trayConfigId) {
		this.trayConfigId = trayConfigId;
	}

	public TrayType getTrayType() {
		return trayType;
	}

	public void setTrayType(TrayType trayType) {
		this.trayType = trayType;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}