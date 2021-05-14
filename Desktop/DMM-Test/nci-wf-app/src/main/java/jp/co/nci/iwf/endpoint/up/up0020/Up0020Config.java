package jp.co.nci.iwf.endpoint.up.up0020;

import java.io.Serializable;

import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;

/**
 * プロセス定義アップロード時の設定
 */
public class Up0020Config implements Serializable {
	/** 企業コード */
	public String corporationCode;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義明細コード */
	public String processDefDetailCode;
	/** プロセス定義名称 */
	public String processDefName;

	/** 新しいプロセス定義コード */
	public String newProcessDefCode;
	/** 新しいプロセス定義明細コード */
	public String newProcessDefDetailCode;

	/** 代理者情報を取り込むか */
	public boolean isAuthTransfer;
	/** 参加者ロールを取り込むか */
	public boolean isAssignRole;
	/** 参加者ロール構成を取り込むか */
	public boolean isAssignRoleDetail;
	/** 参加者変更ロールは取込対象か */
	public boolean isChangeRole;
	/** 参加者変更ロール構成は取込対象か */
	public boolean isChangeRoleDetail;

	public Up0020Config() {
	}

	public Up0020Config(WfmProcessDef proc) {
		this.corporationCode = proc.getCorporationCode();
		this.processDefCode = proc.getProcessDefCode();
		this.processDefDetailCode = proc.getProcessDefDetailCode();
		this.processDefName = proc.getProcessDefName();
		this.newProcessDefCode = this.processDefCode;
		this.newProcessDefDetailCode = this.processDefDetailCode;
	}

}
