package jp.co.nci.iwf.jpa.entity.ex;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * プロセス掲示板エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MwtProcessBbsEx extends MwmBaseJpaEntity {

	@Id
	@Column(name="PROCESS_BBS_ID")
	public long processBbsId;

	@Column(name="\"CONTENTS\"")
	public String contents;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="CORPORATION_CODE_SUBMIT")
	public String corporationCodeSubmit;

	@Column(name="LOCALE_CODE")
	public String localeCode;

	@Column(name="ORGANIZATION_CODE_SUBMIT")
	public String organizationCodeSubmit;

	@Column(name="ORGANIZATION_NAME_SUBMIT")
	public String organizationNameSubmit;

	@Column(name="POST_CODE_SUBMIT")
	public String postCodeSubmit;

	@Column(name="POST_NAME_SUBMIT")
	public String postNameSubmit;

	@Column(name="PROCESS_BBS_ID_UP")
	public Long processBbsIdUp;

	@Column(name="PROCESS_BBS_MAIL_TYPE")
	public String processBbsMailType;

	@Column(name="PROCESS_ID")
	public Long processId;

	@Column(name="TIMESTAMP_SUBMIT")
	public Timestamp timestampSubmit;

	@Column(name="USER_CODE_SUBMIT")
	public String userCodeSubmit;

	@Column(name="USER_NAME_SUBMIT")
	public String userNameSubmit;


}
