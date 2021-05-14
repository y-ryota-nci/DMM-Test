package jp.co.nci.iwf.endpoint.up.up0100;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * アップロード履歴画面の検索結果行
 */
@Entity
@Access(AccessType.FIELD)
public class Up0100Entity extends BaseJpaEntity {
	public static final long serialVersionUID = 1L;

	@Id
	@Column(name="UPLOAD_FILE_ID")
	public long uploadFileId;

	@Column(name="FILE_APP_VERSION")
	public String fileAppVersion;

	@Column(name="FILE_CORPORATION_CODE")
	public String fileCorporationCode;

	@Column(name="FILE_CORPORATION_NAME")
	public String fileCorporationName;

	@Column(name="FILE_DB_STRING")
	public String fileDbString;

	@Column(name="FILE_HOST_IP_ADDR")
	public String fileHostIpAddr;

	@Column(name="FILE_HOST_NAME")
	public String fileHostName;

	@Column(name="FILE_NAME")
	public String fileName;

	@Column(name="FILE_SIZE")
	public Integer fileSize;

	@Column(name="FILE_TIMESTAMP")
	public Timestamp fileTimestamp;

	@Column(name="REGISTERED_FLAG")
	public String registeredFlag;

	@Column(name="REGISTERED_FLAG_NAME")
	public String registeredFlagName;

	@Column(name="UPLOAD_CORPORATION_CODE")
	public String uploadCorporationCode;

	@Column(name="UPLOAD_DATETIME")
	public Timestamp uploadDatetime;

	@Column(name="UPLOAD_KIND")
	public String uploadKind;

	@Column(name="UPLOAD_KIND_NAME")
	public String uploadKindName;
}
