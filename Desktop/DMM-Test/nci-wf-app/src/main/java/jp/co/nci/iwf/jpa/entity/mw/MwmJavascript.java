package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_JAVASCRIPT database table.
 *
 */
@Entity
@Table(name="MWM_JAVASCRIPT", uniqueConstraints=@UniqueConstraint(columnNames={"FILE_NAME", "CORPORATION_CODE"}))
@NamedQuery(name="MwmJavascript.findAll", query="SELECT m FROM MwmJavascript m")
public class MwmJavascript extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="JAVASCRIPT_ID")
	private long javascriptId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="FILE_NAME")
	private String fileName;

	private String remarks;

	public MwmJavascript() {
	}

	public long getJavascriptId() {
		return this.javascriptId;
	}

	public void setJavascriptId(long javascriptId) {
		this.javascriptId = javascriptId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}