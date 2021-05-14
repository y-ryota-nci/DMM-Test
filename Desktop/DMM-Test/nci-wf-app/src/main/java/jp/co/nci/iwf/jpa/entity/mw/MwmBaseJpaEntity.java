package jp.co.nci.iwf.jpa.entity.mw;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * MW*_系テーブルの共通列を定義した基底クラス
 */
@MappedSuperclass
public abstract class MwmBaseJpaEntity extends BaseJpaEntity implements MwCommonColumns {

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Version
	@Column(name="\"VERSION\"")
	private Long version;

	public final String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public final void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public final String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public final void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public final String getDeleteFlag() {
		return this.deleteFlag;
	}

	public final void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public final Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public final void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public final Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public final void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public final String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public final void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public final String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public final void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public final Long getVersion() {
		return this.version;
	}

	public final void setVersion(Long version) {
		this.version = version;
	}
}
