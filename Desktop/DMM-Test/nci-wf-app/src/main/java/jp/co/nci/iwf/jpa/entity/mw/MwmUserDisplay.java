package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MWM_USER_DISPLAY database table.
 *
 */
@Entity
@Table(name="MWM_USER_DISPLAY")
@NamedQuery(name="MwmUserDisplay.findAll", query="SELECT m FROM MwmUserDisplay m")
public class MwmUserDisplay extends MwmBaseJpaEntity implements Serializable {

	@Id
	@Column(name="USER_DISPLAY_ID")
	private Long userDisplayId;
	@Column(name="CORPORATION_CODE")
	private String corporationCode;
	@Column(name="USER_CODE")
	private String userCode;
	@Column(name="SCREEN_TYPE")
	private String screenType;
	@Column(name="USER_DISPLAY_NAME")
	private String userDisplayName;
	@Column(name="DEFAULT_FLAG")
	private String defaultFlag;
	@Column(name="PAGE_SIZE")
	private Long pageSize;
	@Column(name="SORT_ORDER")
	private Long sortOrder;

	public Long getUserDisplayId(){
		return this.userDisplayId;
	}

	public void setUserDisplayId(final Long val){
		this.userDisplayId = val;
	}

	public String getCorporationCode(){
		return this.corporationCode;
	}

	public void setCorporationCode(final String val){
		this.corporationCode = val;
	}

	public String getUserCode(){
		return this.userCode;
	}

	public void setUserCode(final String val){
		this.userCode = val;
	}

	public String getScreenType(){
		return this.screenType;
	}

	public void setScreenType(final String val){
		this.screenType = val;
	}

	public String getUserDisplayName(){
		return this.userDisplayName;
	}

	public void setUserDisplayName(final String val){
		this.userDisplayName = val;
	}

	public String getDefaultFlag(){
		return this.defaultFlag;
	}

	public void setDefaultFlag(final String val){
		this.defaultFlag = val;
	}

	public Long getPageSize(){
		return this.pageSize;
	}

	public void setPageSize(final Long val){
		this.pageSize = val;
	}

	public Long getSortOrder(){
		return this.sortOrder;
	}

	public void setSortOrder(final Long val){
		this.sortOrder = val;
	}

}
