package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_TABLE_AUTHORITY database table.
 *
 */
@Entity
@Table(name="MWM_TABLE_AUTHORITY", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "MENU_ROLE_CODE", "TABLE_ID"}))
@NamedQuery(name="MwmTableAuthority.findAll", query="SELECT m FROM MwmTableAuthority m")
public class MwmTableAuthority extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TABLE_AUTHORITY_ID")
	private long tableAuthorityId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MENU_ROLE_CODE")
	private String menuRoleCode;

	@Column(name="TABLE_ID")
	private Long tableId;

	public MwmTableAuthority() {
	}

	public long getTableAuthorityId() {
		return this.tableAuthorityId;
	}

	public void setTableAuthorityId(long tableAuthorityId) {
		this.tableAuthorityId = tableAuthorityId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getMenuRoleCode() {
		return this.menuRoleCode;
	}

	public void setMenuRoleCode(String menuRoleCode) {
		this.menuRoleCode = menuRoleCode;
	}

	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

}