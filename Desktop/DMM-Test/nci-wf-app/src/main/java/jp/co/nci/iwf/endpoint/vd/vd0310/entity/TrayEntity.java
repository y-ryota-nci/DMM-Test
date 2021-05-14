package jp.co.nci.iwf.endpoint.vd.vd0310.entity;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class TrayEntity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	public Long id;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="PROCESS_ID")
	public Long processId;
	@Column(name="PROCESS_ID_AGGREGATION")
	public Long processIdAggregation;
	@Column(name="SCREEN_PROCESS_ID")
	public Long screenProcessId;
	@Column(name="SCREEN_PROCESS_CODE")
	public String screenProcessCode;
	@Column(name="SCREEN_PROCESS_NAME")
	public String screenProcessName;
	@Column(name="SCREEN_ID")
	public Long screenId;
	@Column(name="SCREEN_NAME")
	public String screenName;
	@Column(name="PROCESS_STATUS")
	public String processStatus;
	@Column(name="APPROVAL_STATUS")
	public String approvalStatus;
	@Column(name="APPROVAL_NO")
	public String approvalNo;
	@Column(name="SUBJECT")
	public String subject;
	@Column(name="APPLICATION_NO")
	public String applicationNo;
	@Column(name="APPLICATION_STATUS")
	public String applicationStatus;
	@Column(name="APPLICATION_DATE")
	public Timestamp applicationDate;
	@Column(name="BUSINESS_PROCESS_STATUS")
	public String businessProcessStatus;
	@Column(name="BUSINESS_PROCESS_STATUS_NAME")
	public String businessProcessStatusName;
	@Column(name="ACTIVITY_ID")
	public Long activityId;
	@Column(name="PROCESS_DEF_CODE")
	public String processDefCode;
	@Column(name="PROCESS_DEF_DETAIL_CODE")
	public String processDefDetailCode;
	@Column(name="PROCESS_DEF_NAME")
	public String processDefName;
	@Column(name="ACTIVITY_DEF_CODE")
	public String activityDefCode;
	@Column(name="ACTIVITY_DEF_NAME")
	public String activityDefName;
	@Column(name="DC_ID")
	public Long dcId;
	@Column(name="CORPORATION_CODE_START")
	public String corporationCodeStart;
	@Column(name="ORGANIZATION_CODE_START")
	public String organizationCodeStart;
	@Column(name="ORGANIZATION_NAME_START")
	public String organizationNameStart;
	@Column(name="ORG_CODE_UP_3_START")
	public String orgCodeUp3Start;
	@Column(name="ORG_CODE_5_START")
	public String orgCode5Start;
	@Column(name="ORG_NAME_5_START")
	public String orgName5Start;
	@Column(name="POST_CODE_START")
	public String postCodeStart;
	@Column(name="POST_NAME_START")
	public String postNameStart;
	@Column(name="USER_CODE_START")
	public String userCodeStart;
	@Column(name="USER_NAME_START")
	public String userNameStart;
	@Column(name="SBMTR_ADDR_CD_START")
	public String sbmtrAddrCdStart;
	@Column(name="SBMTR_ADDR_START")
	public String sbmtrAddrStart;
	@Column(name="CORPORATION_CODE_PROCESS")
	public String corporationCodeProcess;
	@Column(name="ORGANIZATION_CODE_PROCESS")
	public String organizationCodeProcess;
	@Column(name="ORGANIZATION_NAME_PROCESS")
	public String organizationNameProcess;
	@Column(name="ORG_CODE_UP_3_PROCESS")
	public String orgCodeUp3Process;
	@Column(name="ORG_CODE_5_PROCESS")
	public String orgCode5Process;
	@Column(name="ORG_NAME_5_PROCESS")
	public String orgName5Process;
	@Column(name="POST_CODE_PROCESS")
	public String postCodeProcess;
	@Column(name="POST_NAME_PROCESS")
	public String postNameProcess;
	@Column(name="USER_CODE_PROCESS")
	public String userCodeProcess;
	@Column(name="USER_NAME_PROCESS")
	public String userNameProcess;
	@Column(name="SBMTR_ADDR_CD_PROCESS")
	public String sbmtrAddrCdProcess;
	@Column(name="SBMTR_ADDR_PROCESS")
	public String sbmtrAddrProcess;
	@Column(name="START_ACTIVITY_DEF_CODE")
	public String startActivityDefCode;
	@Column(name="COMMENT_DISPLAY_FLAG")
	public String commentDisplayFlag;
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;
	@Column(name="PAY_APPL_CD")
	public String payApplCd;
}
