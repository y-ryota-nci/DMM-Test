package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class StampInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public StampInfo() {}
	public StampInfo(WfLatestHistoryInfo ll) {
		this.sealNameAction = ll.sealNameAction;
		this.sealExecutionDate = ll.executionDate;
		this.sealNameUser = ll.sealNameUser;
	}

	public String sealNameAction;
	public Timestamp sealExecutionDate;
	public String sealNameUser;

}
