package jp.co.nci.iwf.component.tray;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ワークリストなどトレイ関連の画面用レスポンスの基底クラス
 */
public class BaseTrayResponse extends BasePagingResponse {

	/** プロセス定義の選択肢 */
	private List<OptionItem> processDefs;
	/** 業務プロセス状態の選択肢 */
	private List<OptionItem> businessProcessStatus;


	/** プロセス定義の選択肢 */
	public List<OptionItem> getProcessDefs() {
		return processDefs;
	}
	/** プロセス定義の選択肢 */
	public void setProcessDefs(List<OptionItem> processDefs) {
		this.processDefs = processDefs;
	}

	/** 業務プロセス状態の選択肢 */
	public List<OptionItem> getBusinessProcessStatus() {
		return businessProcessStatus;
	}
	/** 業務プロセス状態の選択肢 */
	public void setBusinessProcessStatus(List<OptionItem> businessProcessStatus) {
		this.businessProcessStatus = businessProcessStatus;
	}

}
