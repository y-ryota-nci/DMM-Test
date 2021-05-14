package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_NUMBERING database table.
 *
 */
@Entity
@Table(name="MWM_NUMBERING")
@NamedQuery(name="MwmNumbering.findAll", query="SELECT m FROM MwmNumbering m")
public class MwmNumbering implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MwmNumberingPK id;

	@Column(name="CURRENT_VALUE")
	private Long currentValue;


	public MwmNumbering() {
	}

	public MwmNumberingPK getId() {
		return this.id;
	}

	public void setId(MwmNumberingPK id) {
		this.id = id;
	}

	public Long getCurrentValue() {
		return this.currentValue;
	}

	public void setCurrentValue(Long currentValue) {
		this.currentValue = currentValue;
	}
}