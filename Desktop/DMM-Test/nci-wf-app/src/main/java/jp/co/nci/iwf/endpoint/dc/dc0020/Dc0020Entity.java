package jp.co.nci.iwf.endpoint.dc.dc0020;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Dc0020Entity extends BaseJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROW_ID")
	public long rowId;
	public Long id;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="CONTENTS_TYPE")
	public String contentsType;
	@Column(name="TITLE")
	public String title;
	@Column(name="DOC_FOLDER_ID")
	public String docFolderId;
	@Column(name="LOCK_FLAG")
	public String lockFlag;
	@Column(name="LOCK_TIMESTAMP")
	public String lockTimestamp;
	@Column(name="LOCK_USER_NAME")
	public String lockUserName;
	@Column(name="PUBLISH_TIMESTAMP")
	public String publishTimestamp;
	@Column(name="PUBLISH_USER_NAME")
	public String publishUserName;
	@Column(name="LOCK_USER_FLAG")
	public String lockUserFlag;
	@Column(name="AUTH_REFER")
	public String authRefer;
	@Column(name="AUTH_DOWNLOAD")
	public String authDownload;
	@Column(name="AUTH_EDIT")
	public String authEdit;
	@Column(name="AUTH_DELETE")
	public String authDelete;
	@Column(name="AUTH_COPY")
	public String authCopy;
	@Column(name="AUTH_MOVE")
	public String authMove;
	@Column(name="AUTH_PRINT")
	public String authPrint;
	@Column(name="WF_APPLYING")
	public String wfApplying;
	@Column(name="VERSION")
	public Long version;
	@Column(name="DOC_FILES")
	public String docFiles;
}