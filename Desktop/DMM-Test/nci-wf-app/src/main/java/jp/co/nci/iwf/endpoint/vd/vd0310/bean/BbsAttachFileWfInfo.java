package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.mw.MwtBbsAttachFileWf;

public class BbsAttachFileWfInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long bbsAttachFileWfId;
	public String fileType;
	public String fileName;

	public BbsAttachFileWfInfo() {}

	public BbsAttachFileWfInfo(MwtBbsAttachFileWf e) {
		this.bbsAttachFileWfId = e.getBbsAttachFileWfId();
		this.fileType = e.getFileType();
		this.fileName = e.getFileName();
	}

}
