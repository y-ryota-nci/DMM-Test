package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 外部Javascript一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Vd0050Entity extends BaseJpaEntity {

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="FILE_NAME")
	public String fileName;

	@Id
	@Column(name="JAVASCRIPT_HISTORY_ID")
	public Long javascriptHistoryId;

	@Column(name="JAVASCRIPT_ID")
	public Long javascriptId;

	@Column(name="HISTORY_NO")
	public Integer historyNo;

	@Column(name="REMARKS")
	public String remarks;

	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="VERSION")
	public Long version;

	/** 使用中の画面 */
	@Transient
	public String screenInUse;
	/** 使用中のコンテナ */
	@Transient
	public String containerInUse;
}
