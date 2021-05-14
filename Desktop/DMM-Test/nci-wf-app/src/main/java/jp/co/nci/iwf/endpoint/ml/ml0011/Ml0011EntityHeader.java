package jp.co.nci.iwf.endpoint.ml.ml0011;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 *  メールテンプレート編集のテンプレートヘッダ情報
 */
@Entity
@Access(AccessType.FIELD)
public class Ml0011EntityHeader extends BaseJpaEntity {

	@Column(name="MAIL_TEMPLATE_HEADER_ID")
	public Long mailTemplateHeaderId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Id
	@Column(name="MAIL_TEMPLATE_FILE_ID")
	public Long mailTemplateFileId;

	@Column(name="RETURN_TO")
	public String returnTo;

	@Column(name="SEND_BCC")
	public String sendBcc;

	@Column(name="SEND_CC")
	public String sendCc;

	@Column(name="SEND_FROM")
	public String sendFrom;

	@Column(name="SEND_FROM_PERSONAL")
	public String sendFromPersonal;

	@Column(name="SEND_TO")
	public String sendTo;

	@Column(name="MAIL_TEMPLATE_FILENAME")
	public String mailTemplateFilename;

	@Column(name="MAIL_TEMPLATE_FILE_VERSION")
	public Long mailTemplateFileVersion;

	@Column(name="REMARKS")
	public String remarks;

	@Column(name="VERSION")
	public Long version;

}
