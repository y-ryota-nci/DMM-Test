package jp.co.nci.iwf.endpoint.dc.dc0070;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * テンプレート一覧結果Entity.
 */
@Entity
@Access(AccessType.FIELD)
public class Dc0070Entity extends BaseJpaEntity {

	@Id
	@Column(name="META_TEMPLATE_ID")
	public long metaTemplateId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="META_TEMPLATE_CODE")
	public String metaTemplateCode;

	@Column(name="META_TEMPLATE_NAME")
	public String metaTemplateName;

	@Version
	@Column(name="version")
	public Long version;

}
