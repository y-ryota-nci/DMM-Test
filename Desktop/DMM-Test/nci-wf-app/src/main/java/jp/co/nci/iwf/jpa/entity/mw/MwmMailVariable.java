package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MAIL_VARIABLE database table.
 *
 */
@Entity
@Table(name="MWM_MAIL_VARIABLE", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "MAIL_VARIABLE_CODE"}))
@NamedQuery(name="MwmMailVariable.findAll", query="SELECT m FROM MwmMailVariable m")
public class MwmMailVariable extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAIL_VARIABLE_ID")
	private long mailVariableId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MAIL_VARIABLE_CODE")
	private String mailVariableCode;

	@Column(name="MAIL_VARIABLE_LABEL")
	private String mailVariableLabel;

	@Column(name="MAIL_VARIABLE_VALUE")
	private String mailVariableValue;

	public MwmMailVariable() {
	}

	public long getMailVariableId() {
		return this.mailVariableId;
	}

	public void setMailVariableId(long mailVariableId) {
		this.mailVariableId = mailVariableId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getMailVariableCode() {
		return this.mailVariableCode;
	}

	public void setMailVariableCode(String mailVariableCode) {
		this.mailVariableCode = mailVariableCode;
	}

	public String getMailVariableLabel() {
		return mailVariableLabel;
	}

	public void setMailVariableLabel(String mailVariableLabel) {
		this.mailVariableLabel = mailVariableLabel;
	}

	public String getMailVariableValue() {
		return this.mailVariableValue;
	}

	public void setMailVariableValue(String mailVariableValue) {
		this.mailVariableValue = mailVariableValue;
	}

}
