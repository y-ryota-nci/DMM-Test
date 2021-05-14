package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_JAVASCRIPT_HISTORY database table.
 *
 */
@Entity
@Table(name="MWM_JAVASCRIPT_HISTORY", uniqueConstraints=@UniqueConstraint(columnNames={"JAVASCRIPT_ID", "HISTORY_NO"}))
@NamedQuery(name="MwmJavascriptHistory.findAll", query="SELECT m FROM MwmJavascriptHistory m")
public class MwmJavascriptHistory extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="JAVASCRIPT_HISTORY_ID")
	private long javascriptHistoryId;

	@Column(name="HISTORY_NO")
	private Integer historyNo;

	@Column(name="JAVASCRIPT_ID")
	private Long javascriptId;

	@Lob
	private String script;

	public MwmJavascriptHistory() {
	}

	public long getJavascriptHistoryId() {
		return this.javascriptHistoryId;
	}

	public void setJavascriptHistoryId(long javascriptHistoryId) {
		this.javascriptHistoryId = javascriptHistoryId;
	}

	public Integer getHistoryNo() {
		return this.historyNo;
	}

	public void setHistoryNo(Integer historyNo) {
		this.historyNo = historyNo;
	}

	public Long getJavascriptId() {
		return this.javascriptId;
	}

	public void setJavascriptId(Long javascriptId) {
		this.javascriptId = javascriptId;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}