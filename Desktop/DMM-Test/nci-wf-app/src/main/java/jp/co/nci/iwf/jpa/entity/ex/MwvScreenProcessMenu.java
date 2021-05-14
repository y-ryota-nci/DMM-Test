package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class MwvScreenProcessMenu extends BaseJpaEntity {
	@Id
	@Column(name="ROWNUM_ID")
	public long rownumId;

	@Column(name="SCREEN_PROCESS_MENU_ID")
	public Long screenProcessMenuId;

	@Column(name="MENU_ID")
	public long menuId;

	@Column(name="MENU_NAME")
	public String menuName;

	@Column(name="MENU_ITEM_CODE1")
	public String menuItemCode1;

	@Column(name="MENU_ITEM_CODE2")
	public String menuItemCode2;

	@Column(name="MENU_ITEM_CODE3")
	public String menuItemCode3;

	@Column(name="URL")
	public String url;

	@Column(name="SCREEN_PROCESS_ID")
	public Long screenProcessId;

	@Column(name="SCREEN_PROCESS_CODE")
	public String screenProcessCode;

	@Column(name="SCREEN_PROCESS_NAME")
	public String screenProcessName;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DESCRIPTION")
	public String description;

	@Column(name="PROCESS_DEF_CODE")
	public String processDefCode;

	@Column(name="PROCESS_DEF_DETAIL_CODE")
	public String processDefDetailCode;

	@Column(name="PROCESS_DEF_NAME")
	public String processDefName;

	@Column(name="SCREEN_ID")
	public Long screenId;

	@Column(name="SCREEN_CODE")
	public String screenCode;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="VALID_START_DATE")
	public java.sql.Date validStartDate;

	@Column(name="VALID_END_DATE")
	public java.sql.Date validEndDate;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="VERSION")
	public Long version;
}
