package jp.co.nci.iwf.endpoint.ml.ml0020;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;

/**
 * メール環境設定のエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Ml0020Entity extends BaseJpaEntity {

	public Ml0020Entity() {
	}

	public Ml0020Entity(MwmMailConfig c) {
		this.mailConfigId = c.getMailConfigId();
		this.configCode = c.getConfigCode();
		this.configValue = c.getConfigValue();
		this.remarks = c.getRemarks();
		this.version = c.getVersion();
	}


	@Id
	@Column(name="MAIL_CONFIG_ID")
	public Long mailConfigId;

	@Column(name="CONFIG_CODE")
	public String configCode;

	@Column(name="CONFIG_VALUE")
	public String configValue;

	public String remarks;

	@Column(name="version")
	public Long version;
}
