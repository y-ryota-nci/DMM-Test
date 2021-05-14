package jp.co.nci.iwf.endpoint.vd.vd0310.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import jp.co.nci.integrated_workflow.model.custom.WftProcessRelationEx;

/**
 * 決裁関連文書ブロックのエンティティ
 */
public class ApprovalRelationInfo implements Serializable {
	/** 会社コード */
	public String corporationCode;
	/** 連携元プロセスID */
	public Long processId;
	/** プロセスインスタンス連携ID */
	public Long processRelationId;

	/** 連携先プロセス会社コード */
	public String corporationCodeRelation;
	/** 連携先プロセスID */
	public Long processIdRelation;
	/** プロセスインスタンス連携区分 */
	public String processRelationType;

	public String businessProcessStatusName;
	public String applicationNo;
	public String approvalNo;
	public String subject;
	public String processDefName;
	public Date applicationDate;
	public Date approvalDate;
	public String organizationNameProcess;
	public String userNameProxyProcess;
	/** 最終更新日時（プロセス関連情報の） */
	public Timestamp timestampUpdated;
	/** 最終更新日時（プロセス情報の） */
	public Timestamp timestampUpdatedProcess;
	/** 最終更新日時（プロセス情報の）をLong値に変換したもの */
	public Long timestampUpdatedProcessLong;


	/** コンストラクタ */
	public ApprovalRelationInfo() {}

	/** コンストラクタ */
	public ApprovalRelationInfo(WftProcessRelationEx src) {
		// 連携元
		corporationCode = src.getCorporationCode();
		processId = src.getProcessId();
		processRelationId = src.getProcessRelationId();
		timestampUpdated = src.getTimestampUpdated();
		// 連携先
		processRelationType = src.getProcessRelationType();
		corporationCodeRelation = src.getCorporationCodeRelation();
		processIdRelation = src.getProcessIdRelation();
		timestampUpdatedProcess = src.getTimestampUpdatedProcess();
		timestampUpdatedProcessLong = src.getTimestampUpdatedProcess().getTime();

		applicationNo = src.getApplicationNo();
		applicationDate = src.getApplicationDate();
		subject = src.getSubject();
		processDefName = src.getProcessDefName();
		businessProcessStatusName = src.getBusinessProcessStatusName();
		organizationNameProcess = src.getOrganizationNameProcess();
		userNameProxyProcess = src.getUserNameProxyProcess();
	}
}
