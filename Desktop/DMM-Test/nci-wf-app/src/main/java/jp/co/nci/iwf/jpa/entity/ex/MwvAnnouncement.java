package jp.co.nci.iwf.jpa.entity.ex;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * お知らせ情報エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MwvAnnouncement extends BaseJpaEntity {

	@Id
	@Column(name="ROW_ID")
	public long rowId;

	@Column(name="ANNOUNCEMENT_ID")
	public Long announcementId;

	@Column(name="\"CONTENTS\"")
	public String contents;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="LOCALE_CODE")
	public String localeCode;

	@Column(name="LOCALE_NAME")
	public String localeName;

	@Column(name="LINK_TITLE")
	public String linkTitle;

	@Column(name="LINK_URL")
	public String linkUrl;

	// 手動で値を設定すること
	@Transient
	public java.sql.Date publicationDate;
	// 手動で値を設定すること
	@Transient
	public String labelNew;

	public String remarks;

	public String subject;

	@Column(name="TIMESTAMP_END")
	public Timestamp timestampEnd;

	@Column(name="TIMESTAMP_START")
	public Timestamp timestampStart;

	@Column(name="VERSION")
	public Long version;
}
