package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.mw.MwtAttachFileWf;

public class AttachFileWfInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long rowNo;
	public Long attachFileWfId;
	public String corporationCode;
	public Long processId;
	public String fileName;
	public String comments;

	public AttachFileWfInfo() {}

	public AttachFileWfInfo(MwtAttachFileWf e) {
		this.attachFileWfId = e.getAttachFileWfId();
		this.corporationCode = e.getCorporationCode();
		this.processId = e.getProcessId();
		this.fileName = e.getFileName();
		this.comments = e.getComments();
	}

}
