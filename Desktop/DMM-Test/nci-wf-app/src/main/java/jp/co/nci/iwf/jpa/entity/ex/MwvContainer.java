package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class MwvContainer extends BaseJpaEntity {
	@Id
	@Column(name="CONTAINER_ID")
	public long containerId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="CONTAINER_CODE")
	public String containerCode;

	@Column(name="CONTAINER_NAME")
	public String containerName;

	@Column(name="TABLE_NAME")
	public String tableName;

	@Column(name="CORPORATION_NAME")
	public String corporationName;

	@Column(name="TABLE_SYNC_TIMESTAMP")
	public String tableSyncTimestamp;

	@Column(name="TABLE_MODIFIED_TIMESTAMP")
	public String tableModifiedTimestamp;

	@Column(name="SYNC_TABLE")
	public String syncTable;

	@Column(name="SYNC_TABLE_FLAG")
	public String syncTableFlag;

	@Column(name="LOCALE_CODE")
	public String localeCode;

	@Version
	@Column(name="version")
	public Long version;
}
