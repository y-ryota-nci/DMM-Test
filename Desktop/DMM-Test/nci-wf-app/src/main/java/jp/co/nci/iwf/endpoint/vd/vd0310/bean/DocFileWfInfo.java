package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileWf;

public class DocFileWfInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long rowNo;
	public Long docFileWfId;
	public String corporationCode;
	public Long processId;
	public String fileName;
	public Integer fileSize;
	public String comments;

	public DocFileWfInfo() {}

	public DocFileWfInfo(MwtDocFileWf e) {
		this.docFileWfId = e.getDocFileWfId();
		this.corporationCode = e.getCorporationCode();
		this.processId = e.getProcessId();
		this.fileName = e.getFileName();
		this.fileSize = e.getFileSize();
		this.comments = e.getComments();
	}

}
