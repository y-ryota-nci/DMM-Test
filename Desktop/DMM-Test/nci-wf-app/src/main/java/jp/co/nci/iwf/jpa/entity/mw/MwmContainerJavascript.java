package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_CONTAINER_JAVASCRIPT database table.
 *
 */
@Entity
@Table(name="MWM_CONTAINER_JAVASCRIPT", uniqueConstraints=@UniqueConstraint(columnNames={"CONTAINER_ID", "JAVASCRIPT_ID"}))
@NamedQuery(name="MwmContainerJavascript.findAll", query="SELECT m FROM MwmContainerJavascript m")
public class MwmContainerJavascript extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CONTAINER_JAVASCRIPT_ID")
	private long containerJavascriptId;

	@Column(name="CONTAINER_ID")
	private Long containerId;

	@Column(name="JAVASCRIPT_ID")
	private Long javascriptId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmContainerJavascript() {
	}

	public long getContainerJavascriptId() {
		return this.containerJavascriptId;
	}

	public void setContainerJavascriptId(long containerJavascriptId) {
		this.containerJavascriptId = containerJavascriptId;
	}

	public Long getContainerId() {
		return this.containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Long getJavascriptId() {
		return this.javascriptId;
	}

	public void setJavascriptId(Long javascriptId) {
		this.javascriptId = javascriptId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}