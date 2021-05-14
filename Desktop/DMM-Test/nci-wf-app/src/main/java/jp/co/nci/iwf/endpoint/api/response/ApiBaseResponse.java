package jp.co.nci.iwf.endpoint.api.response;

import java.math.BigDecimal;

import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.iwf.jersey.base.BaseResponse;

public class ApiBaseResponse extends BaseResponse {

	/** 戻りコード. */
	private BigDecimal returnCode = ReturnCode.SUCCESS;
	/** メッセージ. */
	private String returnMessage;
	/** メッセージ(拡張). */
	private String returnMessageExt;

	public BigDecimal getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(BigDecimal returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public String getReturnMessageExt() {
		return returnMessageExt;
	}
	public void setReturnMessageExt(String returnMessageExt) {
		this.returnMessageExt = returnMessageExt;
	}

}
