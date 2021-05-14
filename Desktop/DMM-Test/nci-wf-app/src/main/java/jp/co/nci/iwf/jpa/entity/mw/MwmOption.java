package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_OPTION database table.
 *
 */
@Entity
@Table(name="MWM_OPTION", uniqueConstraints=@UniqueConstraint(columnNames={"OPTION_CODE","CORPORATION_CODE"}))
@NamedQuery(name="MwmOption.findAll", query="SELECT m FROM MwmOption m")
public class MwmOption extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OPTION_ID")
	private long optionId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="OPTION_CODE")
	private String optionCode;

	@Column(name="OPTION_NAME")
	private String optionName;

	public MwmOption() {
	}

	public long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getOptionCode() {
		return this.optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public String getOptionName() {
		return this.optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

}