package jp.co.nci.iwf.jpa.entity.ex;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
public class MwmJavascriptEntityEx extends BaseJpaEntity {

	@Column(name="JAVASCRIPT_ID")
	private long javascriptId;

	@Column(name="FILE_NAME")
	private String fileName;

	@Id
	@Column(name="JAVASCRIPT_HISTORY_ID")
	private long javascriptHistoryId;

	@Lob
	private String script;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	public long getJavascriptId() {
		return javascriptId;
	}

	public void setJavascriptId(long javascriptId) {
		this.javascriptId = javascriptId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getJavascriptHistoryId() {
		return javascriptHistoryId;
	}

	public void setJavascriptHistoryId(long javascriptHistoryId) {
		this.javascriptHistoryId = javascriptHistoryId;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Timestamp getTimestampUpdated() {
		return timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

}
