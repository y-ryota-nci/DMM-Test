package jp.co.nci.iwf.endpoint.dc.dc0100.bean;

import java.io.Serializable;

import jp.co.nci.iwf.jpa.entity.ex.MwtAttachFileDocEx;
import jp.co.nci.iwf.jpa.entity.ex.MwtDocFileDataEx;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocFileData;

public class AttachFileDocInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long rowNo;
	public Long attachFileDocId;
	public Long version;
	public Long docId;
	public String fileName;
	public String comments;
	public Long docFileDataId;
	public String corporationCodeCreated;
	public String userCodeCreated;
	public String timestampCreated;
	public String corporationCodeUpdated;
	public String userCodeUpdated;
	public String timestampUpdated;

	public AttachFileDocInfo() {}

	public AttachFileDocInfo(MwtAttachFileDocEx e) {
		this.attachFileDocId = e.getAttachFileDocId();
		this.version = e.getVersion();
		this.docId = e.getDocId();
		this.fileName = e.getFileName();
		this.comments = e.getComments();
		this.docFileDataId = e.getDocFileDataId();
	}

	/** コンストラクタ. */
	public AttachFileDocInfo(MwtDocFileData e) {
		this.docFileDataId = e.getDocFileDataId();
		this.fileName = e.getFileName();
	}

	/** コンストラクタ. */
	public AttachFileDocInfo(MwtDocFileDataEx e) {
		this.docFileDataId = e.getDocFileDataId();
		this.fileName = e.getFileName();
		this.corporationCodeCreated = e.getCorporationCodeCreated();
		this.userCodeCreated = e.getUserCodeCreated();
		this.corporationCodeUpdated = e.getCorporationCodeUpdated();
		this.userCodeUpdated = e.getUserCodeUpdated();
	}
}
