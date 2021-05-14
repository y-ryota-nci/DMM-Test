package jp.co.nci.iwf.endpoint.mm.mm0120;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Mm0120Entity extends BaseJpaEntity {

	@Id
	@Column(name="ROW_ID")
	public long rowId;

	@Column(name="ANNOUNCEMENT_ID")
	public long announcementId;

	@Column(name="\"CONTENTS\"")
	public String contents;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="LINK_TITLE")
	public String linkTitle;

	public String remarks;

	public String subject;

	@Column(name="TIMESTAMP_END")
	public String timestampEnd;

	@Column(name="TIMESTAMP_START")
	public String timestampStart;

	@Column(name="VERSION")
	public Long version;

	@Transient
	public String corporation;
}
