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
 * The persistent class for the WFM_ASSIGN_ROLE_DETAIL database table.
 *
 */
@Entity
@Table(name="WFM_ASSIGN_ROLE_DETAIL")
@NamedQuery(name="WfmAssignRoleDetail.findAll", query="SELECT w FROM WfmAssignRoleDetail w")
public class WfmAssignRoleDetail extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WfmAssignRoleDetailPK pk;

	@Column(name="ASSIGNMENT_CONSTANT_1")
	private String assignmentConstant1;

	@Column(name="ASSIGNMENT_CONSTANT_2")
	private String assignmentConstant2;

	@Column(name="ASSIGNMENT_CONSTANT_3")
	private String assignmentConstant3;

	@Column(name="ASSIGNMENT_CONSTANT_4")
	private String assignmentConstant4;

	@Column(name="ASSIGNMENT_CONSTANT_5")
	private String assignmentConstant5;

	@Column(name="BELONG_TYPE")
	private String belongType;

	@Column(name="CORPORATION_CODE_ASSIGNED")
	private String corporationCodeAssigned;

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

	@Column(name="ORGANIZATION_CODE_ASSIGNED")
	private String organizationCodeAssigned;

	@Column(name="POST_CODE_ASSIGNED")
	private String postCodeAssigned;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_ASSIGNED")
	private String userCodeAssigned;

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

	public WfmAssignRoleDetail() {
	}

	public WfmAssignRoleDetailPK getPk() {
		return this.pk;
	}

	public void setPk(WfmAssignRoleDetailPK pk) {
		this.pk = pk;
	}

	public String getAssignmentConstant1() {
		return assignmentConstant1;
	}

	public void setAssignmentConstant1(String assignmentConstant1) {
		this.assignmentConstant1 = assignmentConstant1;
	}

	public String getAssignmentConstant2() {
		return assignmentConstant2;
	}

	public void setAssignmentConstant2(String assignmentConstant2) {
		this.assignmentConstant2 = assignmentConstant2;
	}

	public String getAssignmentConstant3() {
		return assignmentConstant3;
	}

	public void setAssignmentConstant3(String assignmentConstant3) {
		this.assignmentConstant3 = assignmentConstant3;
	}

	public String getAssignmentConstant4() {
		return assignmentConstant4;
	}

	public void setAssignmentConstant4(String assignmentConstant4) {
		this.assignmentConstant4 = assignmentConstant4;
	}

	public String getAssignmentConstant5() {
		return assignmentConstant5;
	}

	public void setAssignmentConstant5(String assignmentConstant5) {
		this.assignmentConstant5 = assignmentConstant5;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	public String getCorporationCodeAssigned() {
		return corporationCodeAssigned;
	}

	public void setCorporationCodeAssigned(String corporationCodeAssigned) {
		this.corporationCodeAssigned = corporationCodeAssigned;
	}

	public String getCorporationCodeCreated() {
		return corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeUpdated() {
		return corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpCreated() {
		return ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getOrganizationCodeAssigned() {
		return organizationCodeAssigned;
	}

	public void setOrganizationCodeAssigned(String organizationCodeAssigned) {
		this.organizationCodeAssigned = organizationCodeAssigned;
	}

	public String getPostCodeAssigned() {
		return postCodeAssigned;
	}

	public void setPostCodeAssigned(String postCodeAssigned) {
		this.postCodeAssigned = postCodeAssigned;
	}

	public Timestamp getTimestampCreated() {
		return timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUserCodeAssigned() {
		return userCodeAssigned;
	}

	public void setUserCodeAssigned(String userCodeAssigned) {
		this.userCodeAssigned = userCodeAssigned;
	}

	public String getUserCodeCreated() {
		return userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public Date getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}
}