package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_META_INFO database table.
 *
 */
@Entity
@Table(name="MWT_DOC_META_INFO")
@NamedQuery(name="MwtDocMetaInfo.findAll", query="SELECT m FROM MwtDocMetaInfo m")
public class MwtDocMetaInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_META_ID")
	private long docMetaId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="META_TEMPLATE_DETAIL_ID")
	private Long metaTemplateDetailId;

	@Column(name="META_TEMPLATE_ID")
	private Long metaTemplateId;

	@Column(name="META_VALUE1")
	private String metaValue1;

	@Column(name="META_VALUE2")
	private String metaValue2;

	@Column(name="META_VALUE3")
	private String metaValue3;

	@Column(name="META_VALUE4")
	private String metaValue4;

	@Column(name="META_VALUE5")
	private String metaValue5;

	public MwtDocMetaInfo() {
	}

	public long getDocMetaId() {
		return this.docMetaId;
	}

	public void setDocMetaId(long docMetaId) {
		this.docMetaId = docMetaId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getMetaTemplateDetailId() {
		return this.metaTemplateDetailId;
	}

	public void setMetaTemplateDetailId(Long metaTemplateDetailId) {
		this.metaTemplateDetailId = metaTemplateDetailId;
	}

	public Long getMetaTemplateId() {
		return this.metaTemplateId;
	}

	public void setMetaTemplateId(Long metaTemplateId) {
		this.metaTemplateId = metaTemplateId;
	}

	public String getMetaValue1() {
		return this.metaValue1;
	}

	public void setMetaValue1(String metaValue1) {
		this.metaValue1 = metaValue1;
	}

	public String getMetaValue2() {
		return this.metaValue2;
	}

	public void setMetaValue2(String metaValue2) {
		this.metaValue2 = metaValue2;
	}

	public String getMetaValue3() {
		return this.metaValue3;
	}

	public void setMetaValue3(String metaValue3) {
		this.metaValue3 = metaValue3;
	}

	public String getMetaValue4() {
		return this.metaValue4;
	}

	public void setMetaValue4(String metaValue4) {
		this.metaValue4 = metaValue4;
	}

	public String getMetaValue5() {
		return this.metaValue5;
	}

	public void setMetaValue5(String metaValue5) {
		this.metaValue5 = metaValue5;
	}

}