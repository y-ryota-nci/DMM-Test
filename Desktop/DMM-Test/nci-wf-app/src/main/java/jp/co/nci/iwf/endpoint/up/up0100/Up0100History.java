package jp.co.nci.iwf.endpoint.up.up0100;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * アップロードファイル登録情報
 */
@Entity
@Access(AccessType.FIELD)
public class Up0100History extends BaseJpaEntity {
	@Id
	@Column(name="UPLOAD_FILE_REGISTERED_ID")
	public long uploadFileRegisteredId;

	@Column(name="LATEST_FLAG")
	public String latestFlag;

	@Lob
	@Column(name="REGISTERED_CONFIG")
	public String registeredConfig;

	@Column(name="REGISTERED_CORPORATION_CODE")
	public String registeredCorporationCode;

	@Column(name="REGISTERED_DATETIME")
	public Timestamp registeredDatetime;

	@Column(name="REGISTERED_USER_ADDED_INFO")
	public String registeredUserAddedInfo;

	@Column(name="REGISTERED_USER_CODE")
	public String registeredUserCode;

	@Column(name="REGISTERED_USER_NAME")
	public String registeredUserName;

	@Column(name="UPLOAD_FILE_ID")
	public Long uploadFileId;
}
