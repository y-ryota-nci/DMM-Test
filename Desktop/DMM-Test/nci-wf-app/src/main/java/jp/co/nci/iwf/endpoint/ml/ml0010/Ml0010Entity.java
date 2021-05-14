package jp.co.nci.iwf.endpoint.ml.ml0010;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * メールテンプレート一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Ml0010Entity extends BaseJpaEntity {

	@Id
	@Column(name="MAIL_TEMPLATE_FILE_ID")
	public long mailTemplateFileId;

	@Column(name="MAIL_TEMPLATE_FILENAME")
	public String mailTemplateFilename;

	@Column(name="REMARKS")
	public String remarks;

	@Column(name="MAIL_TEMPLATE_HEADER_ID")
	public Long mailTemplateHeaderId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="SEND_FROM")
	public String sendFrom;

	@Column(name="MAIL_SUBJECT")
	public String mailSubject;

	@Column(name="VERSION")
	public Long version;

}
