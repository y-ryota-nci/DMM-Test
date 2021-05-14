package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Bl0010AssignedUser extends MwmBaseJpaEntity implements Serializable {
	@Id
	@Column(name="ID")
	public Long id;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="USER_CODE")
	public String userCode;

	@Column(name="MAIL_ADDRESS")
	public String mailAddress;

	@Column(name="USER_NAME")
	public String userName;

	@Column(name="LOCALE_CODE")
	public String localeCode;

	@Column(name="DEFAULT_LOCALE_CODE")
	public String defaultLocaleCode;
}
