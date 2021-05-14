package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_APPENDED_INFO database table.
 *
 */
@Entity
@Table(name="MWT_DOC_APPENDED_INFO")
@NamedQuery(name="MwtDocAppendedInfo.findAll", query="SELECT m FROM MwtDocAppendedInfo m")
public class MwtDocAppendedInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_APPENDED_ID")
	private long docAppendedId;

	@Column(name="APPENDED_DATE")
	private Timestamp appendedDate;

	@Column(name="CORPORATION_CODE_APPENDED")
	private String corporationCodeAppended;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="MEMORANDOM")
	private String memorandom;

	@Column(name="SEQ_NO")
	private Integer seqNo;

	@Column(name="USER_CODE_APPENDED")
	private String userCodeAppended;

	@Column(name="USER_NAME_APPENDED")
	private String userNameAppended;

	public MwtDocAppendedInfo() {
	}

	public long getDocAppendedId() {
		return this.docAppendedId;
	}

	public void setDocAppendedId(long docAppendedId) {
		this.docAppendedId = docAppendedId;
	}

	public Timestamp getAppendedDate() {
		return this.appendedDate;
	}

	public void setAppendedDate(Timestamp appendedDate) {
		this.appendedDate = appendedDate;
	}

	public String getCorporationCodeAppended() {
		return this.corporationCodeAppended;
	}

	public void setCorporationCodeAppended(String corporationCodeAppended) {
		this.corporationCodeAppended = corporationCodeAppended;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getMemorandom() {
		return this.memorandom;
	}

	public void setMemorandom(String memorandom) {
		this.memorandom = memorandom;
	}

	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getUserCodeAppended() {
		return this.userCodeAppended;
	}

	public void setUserCodeAppended(String userCodeAppended) {
		this.userCodeAppended = userCodeAppended;
	}

	public String getUserNameAppended() {
		return this.userNameAppended;
	}

	public void setUserNameAppended(String userNameAppended) {
		this.userNameAppended = userNameAppended;
	}

}