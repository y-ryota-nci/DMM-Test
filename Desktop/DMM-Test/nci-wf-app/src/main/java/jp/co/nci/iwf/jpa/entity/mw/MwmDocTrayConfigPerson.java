package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jp.co.nci.iwf.endpoint.dc.DcCodeBook.DocTrayType;


/**
 * The persistent class for the MWM_DOC_TRAY_CONFIG_PERSON database table.
 *
 */
@Entity
@Table(name="MWM_DOC_TRAY_CONFIG_PERSON", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "USER_CODE", "DOC_TRAY_TYPE"}))
@NamedQuery(name="MwmDocTrayConfigPerson.findAll", query="SELECT m FROM MwmDocTrayConfigPerson m")
public class MwmDocTrayConfigPerson extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_TRAY_CONFIG_PERSONALIZE_ID")
	private long docTrayConfigPersonalizeId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_TRAY_CONFIG_ID")
	private Long docTrayConfigId;

	@Enumerated(EnumType.STRING)
	@Column(name="DOC_TRAY_TYPE")
	private DocTrayType docTrayType;

	@Column(name="USER_CODE")
	private String userCode;

	public MwmDocTrayConfigPerson() {
	}

	public long getDocTrayConfigPersonalizeId() {
		return this.docTrayConfigPersonalizeId;
	}

	public void setDocTrayConfigPersonalizeId(long docTrayConfigPersonalizeId) {
		this.docTrayConfigPersonalizeId = docTrayConfigPersonalizeId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getDocTrayConfigId() {
		return this.docTrayConfigId;
	}

	public void setDocTrayConfigId(Long docTrayConfigId) {
		this.docTrayConfigId = docTrayConfigId;
	}

	public DocTrayType getDocTrayType() {
		return this.docTrayType;
	}

	public void setDocTrayType(DocTrayType docTrayType) {
		this.docTrayType = docTrayType;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}