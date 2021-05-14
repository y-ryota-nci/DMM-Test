package jp.co.nci.iwf.endpoint.dc.dc0131.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class ScreenDocLevelDefEntity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROW_ID")
	public long rowId;
	@Column(name="ID")
	public Long id;
	@Column(name="TYPE")
	public String type;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="PARENT_LEVEL_CODE")
	public String parentLevelCode;
	@Column(name="LEVEL_CODE")
	public String levelCode;
	@Column(name="LEVEL_NAME")
	public String levelName;
	@Column(name="EXPANSION_FLAG")
	public String expansionFlag;
	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	public Date validStartDate;
	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	public Date validEndDate;
	@Column(name="DELETE_FLAG")
	public String deleteFlag;

}
