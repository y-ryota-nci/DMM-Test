package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_WF_RELATION database table.
 *
 */
@Entity
@Table(name="MWT_DOC_WF_RELATION")
@NamedQuery(name="MwtDocWfRelation.findAll", query="SELECT m FROM MwtDocWfRelation m")
public class MwtDocWfRelation extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_WF_RELATION_ID")
	private long docWfRelationId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="PROCESS_ID")
	private Long processId;

	@Column(name="RELATION_TYPE")
	private String relationType;

	public MwtDocWfRelation() {
	}

	public long getDocWfRelationId() {
		return this.docWfRelationId;
	}

	public void setDocWfRelationId(long docWfRelationId) {
		this.docWfRelationId = docWfRelationId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

}