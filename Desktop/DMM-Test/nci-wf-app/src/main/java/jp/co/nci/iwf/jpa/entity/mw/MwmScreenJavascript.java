package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN_JAVASCRIPT database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_JAVASCRIPT", uniqueConstraints=@UniqueConstraint(columnNames={"SCREEN_ID", "JAVASCRIPT_ID"}))
@NamedQuery(name="MwmScreenJavascript.findAll", query="SELECT m FROM MwmScreenJavascript m")
public class MwmScreenJavascript extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_JAVASCRIPT_ID")
	private long screenJavascriptId;

	@Column(name="JAVASCRIPT_ID")
	private Long javascriptId;

	@Column(name="SCREEN_ID")
	private Long screenId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmScreenJavascript() {
	}

	public long getScreenJavascriptId() {
		return this.screenJavascriptId;
	}

	public void setScreenJavascriptId(long screenJavascriptId) {
		this.screenJavascriptId = screenJavascriptId;
	}

	public Long getJavascriptId() {
		return this.javascriptId;
	}

	public void setJavascriptId(Long javascriptId) {
		this.javascriptId = javascriptId;
	}

	public Long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}