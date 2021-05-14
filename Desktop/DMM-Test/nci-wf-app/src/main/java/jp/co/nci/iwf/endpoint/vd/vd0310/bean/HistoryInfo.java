package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;
import jp.co.nci.iwf.util.MiscUtils;

public class HistoryInfo implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	/** 企業コード */
	public String corporationCode;
	/** プロセスID */
	public Long processId;
	/** アクティビティID */
	public Long activityId;
	/** 処理者：企業コード */
	public String corporationCodeOperation;
	/** 処理者：組織コード */
	public String organizationCodeOperation;
	/** 処理者：組織名 */
	public String organizationNameOperation;
	/** 処理者：役職コード */
	public String postCodeOperation;
	/** 処理者：役職名 */
	public String postNameOperation;
	/** 処理者：ユーザコード */
	public String userCodeOperation;
	/** 処理者：ユーザ氏名 */
	public String userNameOperation;
	/** 処理内容（アクティビティ名＋アクション名） */
	public String processing;
	/** 処理者：処理日 */
	public String executionDate;

	/** 代理者：企業コード */
	public String corporationCodeProxy;
	/** 代理者：ユーザコード */
	public String userCodeProxy;
	/** （承認時の）コメント */
	public String actionComment;

	/** コンストラクタ */
	public HistoryInfo() {

	}

	/** コンストラクタ */
	public HistoryInfo(WfvActionHistory h) {
		this.corporationCode = h.getCorporationCode();
		this.processId = h.getProcessId();
		this.activityId = h.getActivityId();
		this.corporationCodeOperation = h.getCorporationCodeOpActivity();
		this.organizationCodeOperation = h.getOrganizationCodeOpeActivity();
		this.organizationNameOperation = h.getOrganizationNameOpeActivity();
		this.postCodeOperation = h.getPostCodeOpeActivity();
		this.postNameOperation = h.getPostNameOpeActivity();
		this.userCodeOperation = h.getUserCodeOperationActivity();
		this.userNameOperation = h.getUserNameOperationActivity();
		this.corporationCodeProxy = h.getUserCodeProxyActivity();
		this.userCodeProxy = h.getUserCodeProxyActivity();

		// 処理＝アクティビティ定義名＋"(" + アクション名 + ")"
		this.processing = String.format("%s(%s)", h.getActivityDefName(), MiscUtils.defaults(h.getActionName(), ""));
		this.actionComment = MiscUtils.escapeHtmlBR(h.getActionComment());
		if (h.getExecutionDate() != null) {
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			this.executionDate = df.format(h.getExecutionDate());
		}
	}
}
