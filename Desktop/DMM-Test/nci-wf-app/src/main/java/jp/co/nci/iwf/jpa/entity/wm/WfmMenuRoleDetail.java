package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/**
 * The persistent class for the WFM_MENU_ROLE_DETAIL database table.
 *
 */
@Entity
@Table(name="WFM_MENU_ROLE_DETAIL")
@NamedQuery(name="WfmMenuRoleDetail.findAll", query="SELECT w FROM WfmMenuRoleDetail w")
public class WfmMenuRoleDetail extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WfmMenuRoleDetailPK pk;

	@Column(name="CORPORATION_CODE_ACCESS")
	private String corporationCodeAccess;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	private Long id;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="MENU_ROLE_ASSIGNMENT_TYPE")
	private String menuRoleAssignmentType;

	@Column(name="ORGANIZATION_CODE_ACCESS")
	private String organizationCodeAccess;

	@Column(name="POST_CODE_ACCESS")
	private String postCodeAccess;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_ACCESS")
	private String userCodeAccess;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public WfmMenuRoleDetail() {
	}

	public WfmMenuRoleDetailPK getPk() {
		return this.pk;
	}

	public void setPk(WfmMenuRoleDetailPK pk) {
		this.pk = pk;
	}

	public String getCorporationCodeAccess() {
		return this.corporationCodeAccess;
	}

	public void setCorporationCodeAccess(String corporationCodeAccess) {
		this.corporationCodeAccess = corporationCodeAccess;
	}

	public String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getMenuRoleAssignmentType() {
		return this.menuRoleAssignmentType;
	}

	public void setMenuRoleAssignmentType(String menuRoleAssignmentType) {
		this.menuRoleAssignmentType = menuRoleAssignmentType;
	}

	public String getOrganizationCodeAccess() {
		return this.organizationCodeAccess;
	}

	public void setOrganizationCodeAccess(String organizationCodeAccess) {
		this.organizationCodeAccess = organizationCodeAccess;
	}

	public String getPostCodeAccess() {
		return this.postCodeAccess;
	}

	public void setPostCodeAccess(String postCodeAccess) {
		this.postCodeAccess = postCodeAccess;
	}

	public Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUserCodeAccess() {
		return this.userCodeAccess;
	}

	public void setUserCodeAccess(String userCodeAccess) {
		this.userCodeAccess = userCodeAccess;
	}

	public String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

}