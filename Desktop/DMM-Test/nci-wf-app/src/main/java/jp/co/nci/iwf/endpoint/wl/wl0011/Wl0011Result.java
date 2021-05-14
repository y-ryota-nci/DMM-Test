package jp.co.nci.iwf.endpoint.wl.wl0011;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Wl0011Result extends BaseJpaEntity {

	@Id
	@Column(name="TRAY_CONFIG_RESULT_ID")
	public long trayConfigResultId;

	@Column(name="ALIGN_TYPE")
	public String alignType;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="BUSINESS_INFO_CODE")
	public String businessInfoCode;

	@Column(name="BUSINESS_INFO_NAME")
	public String businessInfoName;

	@Column(name="COL_WIDTH")
	public Integer colWidth;

	@Column(name="INITIAL_SORT_DESC_FLAG")
	public String initialSortDescFlag;

	@Column(name="INITIAL_SORT_FLAG")
	public String initialSortFlag;

	@Column(name="LINK_FLAG")
	public String linkFlag;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="VERSION")
	public Long version;
}
