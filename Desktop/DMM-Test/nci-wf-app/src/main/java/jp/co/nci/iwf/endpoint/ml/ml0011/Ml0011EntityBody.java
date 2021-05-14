package jp.co.nci.iwf.endpoint.ml.ml0011;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * メールテンプレート編集のテンプレート本文情報
 */
@Entity
@Access(AccessType.FIELD)
public class Ml0011EntityBody extends BaseJpaEntity {

	@Id
	@Column(name="MAIL_TEMPLATE_BODY_ID")
	public Long mailTemplateBodyId;

	@Column(name="LOCALE_CODE")
	public String localeCode;

	public String localeName;

	@Lob
	@Column(name="MAIL_BODY")
	public String mailBody;

	@Column(name="MAIL_SUBJECT")
	public String mailSubject;

	@Column(name="MAIL_TEMPLATE_HEADER_ID")
	public Long mailTemplateHeaderId;

	@Column(name="VERSION")
	public Long version;

	/** コンストラクタ */
	public Ml0011EntityBody() {}
}
