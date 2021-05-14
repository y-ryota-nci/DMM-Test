package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_META_TEMPLATE_DEF database table.
 *
 */
@Entity
@Table(name="MWM_META_TEMPLATE_DEF")
@NamedQuery(name="MwmMetaTemplateDef.findAll", query="SELECT m FROM MwmMetaTemplateDef m")
public class MwmMetaTemplateDef extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="META_TEMPLATE_ID")
	private long metaTemplateId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="META_TEMPLATE_CODE")
	private String metaTemplateCode;

	@Column(name="META_TEMPLATE_NAME")
	private String metaTemplateName;

	public MwmMetaTemplateDef() {
	}

	public long getMetaTemplateId() {
		return this.metaTemplateId;
	}

	public void setMetaTemplateId(long metaTemplateId) {
		this.metaTemplateId = metaTemplateId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getMetaTemplateCode() {
		return this.metaTemplateCode;
	}

	public void setMetaTemplateCode(String metaTemplateCode) {
		this.metaTemplateCode = metaTemplateCode;
	}

	public String getMetaTemplateName() {
		return this.metaTemplateName;
	}

	public void setMetaTemplateName(String metaTemplateName) {
		this.metaTemplateName = metaTemplateName;
	}

}