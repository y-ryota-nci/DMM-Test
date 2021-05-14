package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/**
 * The persistent class for the WFM_MENU_V database table.
 *
 */
@Entity
@Table(name="WFM_MENU_V")
@NamedQuery(name="WfmMenuV.findAll", query="SELECT w FROM WfmMenuV w")
public class WfmMenuV extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ACTION_RESULT")
	private String actionResult;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_P")
	private String corporationCodeP;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	private String description;

	@Column(name="DETAIL_CORPORATION_CODE")
	private String detailCorporationCode;

	@Id
	private Long id;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITEM_CODE_1")
	private String itemCode1;

	@Column(name="ITEM_CODE_2")
	private String itemCode2;

	@Column(name="ITEM_CODE_3")
	private String itemCode3;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="MENU_ID")
	private BigDecimal menuId;

	@Column(name="MENU_NAME")
	private String menuName;

	@Column(name="MENU_TYPE")
	private String menuType;

	@Column(name="PROCESS_DEF_CODE")
	private String processDefCode;

	@Column(name="PROCESS_DEF_DETAIL_CODE")
	private String processDefDetailCode;

	@Column(name="SCREEN_ID")
	private String screenId;

	@Column(name="SCREEN_STATUS")
	private String screenStatus;

	@Column(name="SORT_ORDER")
	private BigDecimal sortOrder;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public WfmMenuV() {
	}

	public String getActionResult() {
		return this.actionResult;
	}

	public void setActionResult(String actionResult) {
		this.actionResult = actionResult;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeP() {
		return this.corporationCodeP;
	}

	public void setCorporationCodeP(String corporationCodeP) {
		this.corporationCodeP = corporationCodeP;
	}

	public String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetailCorporationCode() {
		return this.detailCorporationCode;
	}

	public void setDetailCorporationCode(String detailCorporationCode) {
		this.detailCorporationCode = detailCorporationCode;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getItemCode1() {
		return this.itemCode1;
	}

	public void setItemCode1(String itemCode1) {
		this.itemCode1 = itemCode1;
	}

	public String getItemCode2() {
		return this.itemCode2;
	}

	public void setItemCode2(String itemCode2) {
		this.itemCode2 = itemCode2;
	}

	public String getItemCode3() {
		return this.itemCode3;
	}

	public void setItemCode3(String itemCode3) {
		this.itemCode3 = itemCode3;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public BigDecimal getMenuId() {
		return this.menuId;
	}

	public void setMenuId(BigDecimal menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getProcessDefCode() {
		return this.processDefCode;
	}

	public void setProcessDefCode(String processDefCode) {
		this.processDefCode = processDefCode;
	}

	public String getProcessDefDetailCode() {
		return this.processDefDetailCode;
	}

	public void setProcessDefDetailCode(String processDefDetailCode) {
		this.processDefDetailCode = processDefDetailCode;
	}

	public String getScreenId() {
		return this.screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getScreenStatus() {
		return this.screenStatus;
	}

	public void setScreenStatus(String screenStatus) {
		this.screenStatus = screenStatus;
	}

	public BigDecimal getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(BigDecimal sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

}