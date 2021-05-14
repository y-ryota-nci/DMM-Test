package jp.co.nci.iwf.endpoint.vd.vd0010;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * コンテナ一覧の検索結果行
 */
@Entity(name="MWV_CONTAINER")
@Access(AccessType.FIELD)
public class Vd0010Entity extends BaseJpaEntity {
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

	@Column(name="LOCALE_CODE")
	public String localeCode;

	@Version
	@Column(name="version")
	public Long version;

}
