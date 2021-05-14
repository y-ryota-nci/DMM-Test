package jp.co.nci.iwf.endpoint.dc.dc0100.entity;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
@Cacheable(false)
public class DocFileInfoEntity extends BaseJpaEntity {

	@Id
	@Column(name="DOC_FILE_ID")
	public Long docFileId;
	@Column(name="VERSION")
	public Long version;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="DOC_FILE_NUM")
	public String docFileNum;
	@Column(name="DOC_ID")
	public Long docId;
	@Column(name="MAJOR_VERSION")
	public Integer majorVersion;
	@Column(name="MINOR_VERSION")
	public Integer minorVersion;
	@Column(name="COMMENTS")
	public String comments;
	@Column(name="LOCK_FLAG")
	public String lockFlag;
	@Column(name="LOCK_TIMESTAMP")
	public Timestamp lockTimestamp;
	@Column(name="LOCK_CORPORATION_CODE")
	public String lockCorporationCode;
	@Column(name="LOCK_CORPORATION_NAME")
	public String lockCorporationName;
	@Column(name="LOCK_USER_CODE")
	public String lockUserCode;
	@Column(name="LOCK_USER_NAME")
	public String lockUserName;
	@Column(name="DOC_FILE_DATA_ID")
	public long docFileDataId;
	@Column(name="USER_NAME_CREATED")
	public String userNameCreated;
	@Column(name="USER_NAME_UPDATED")
	public String userNameUpdated;
	@Column(name="TIMESTAMP_CREATED")
	public Timestamp timestampCreated;
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;
	@Column(name="FILE_NAME")
	public String fileName;
	@Column(name="title")
	public String title;
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
	@Column(name="DOC_FILE_WF_ID")
	public Long docFileWfId;
}
