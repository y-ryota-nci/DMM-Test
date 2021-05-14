package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_ACCESS_LOG_DETAIL database table.
 *
 */
@Entity
@Table(name="MWT_ACCESS_LOG_DETAIL")
@NamedQuery(name="MwtAccessLogDetail.findAll", query="SELECT m FROM MwtAccessLogDetail m")
public class MwtAccessLogDetail extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESS_LOG_DETAIL_ID")
	private long accessLogDetailId;

	@Column(name="ACCESS_LOG_ID")
	private Long accessLogId;

	@Column(name="ACCESS_TIME")
	private Timestamp accessTime;

	@Lob
	@Column(name="KEY_VALUE")
	private String keyValue;

	public MwtAccessLogDetail() {
	}

	public long getAccessLogDetailId() {
		return this.accessLogDetailId;
	}

	public void setAccessLogDetailId(long accessLogDetailId) {
		this.accessLogDetailId = accessLogDetailId;
	}

	public Long getAccessLogId() {
		return this.accessLogId;
	}

	public void setAccessLogId(Long accessLogId) {
		this.accessLogId = accessLogId;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public String getKeyValue() {
		return this.keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

}