package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;

/**
 * 文書WF連携情報.
 */
public class DocWfRelationInfo implements Serializable {

	/** 会社コード */
	public String corporationCode;
	/** プロセスID */
	public Long processId;
	/** 文書ステータス */
	public String businessProcessStatus;
	/** 件名 */
	public String subject;
	/** 申請番号 */
	public String applicationNo;
	/** 文書種別 */
	public String processDefName;
	/** 申請組織 */
	public String applyOrganizationName;
	/** 申請者 */
	public String applyUserName;
	/** 申請日 */
	public Date applicationDate;
	/** 更新日時 */
	public Timestamp timestampUpdatedProcess;

	/** コンストラクタ(デフォルト). */
	public DocWfRelationInfo() {
	}

	/** コンストラクタ. */
	public DocWfRelationInfo(WfvActionHistory e) {
		this.corporationCode = e.getCorporationCode();
		this.processId = e.getProcessId();
		this.businessProcessStatus = e.getBusinessProcessStatusName();
		this.subject = e.getSubject();
		this.applicationNo = e.getApplicationNo();
		this.processDefName = e.getProcessDefName();
		this.applyOrganizationName = e.getOrganizationNameProcess();
		this.applyUserName = e.getUserNameProxyProcess();
		this.applicationDate = e.getApplicationDate();
		this.timestampUpdatedProcess = e.getTimestampUpdatedProcess();
	}
}
