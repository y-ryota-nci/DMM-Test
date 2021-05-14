package jp.co.nci.iwf.endpoint.vd.vd0051;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Vd0051Entity extends MwmBaseJpaEntity {

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="FILE_NAME")
	public String fileName;

	@Id
	@Column(name="JAVASCRIPT_HISTORY_ID")
	public Long javascriptHistoryId;

	@Column(name="JAVASCRIPT_ID")
	public Long javascriptId;

	@Column(name="HISTORY_NO")
	public Integer historyNo;

	@Lob
	public String script;

	public String remarks;
}
