package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

@Entity
public class MwmUserDisplayColumnEx extends MwmBaseJpaEntity {

	@Id
	@Column(name="USER_DISPLAY_COLUMN_ID")
	private Long userDisplayColumnId;
	@Column(name="USER_DISPLAY_ID")
	private Long userDisplayId;
	@Column(name="COLUMN_NAME")
	private String columnName;
	@Column(name="COLUMN_DISPLAY_FLAG")
	private String columnDisplayFlag;
	@Column(name="COLUMN_INDEX")
	private Long columnIndex;
	@Column(name="COLUMN_SORT_TYPE")
	private String columnSortType;
	@Column(name="COLUMN_SORT_ORDER")
	private Long columnSortOrder;
	@Column(name="DISPLAY_NAME")
	private String displayName;
	private String attrName;

	public Long getUserDisplayColumnId(){
		return this.userDisplayColumnId;
	}

	public void setUserDisplayColumnId(final Long val){
		this.userDisplayColumnId = val;
	}

	public Long getUserDisplayId(){
		return this.userDisplayId;
	}

	public void setUserDisplayId(final Long val){
		this.userDisplayId = val;
	}

	public String getColumnName(){
		return this.columnName;
	}

	public void setColumnName(final String val){
		this.columnName = val;
	}

	public String getColumnDisplayFlag(){
		return this.columnDisplayFlag;
	}

	public void setColumnDisplayFlag(final String val){
		this.columnDisplayFlag = val;
	}

	public Long getColumnIndex(){
		return this.columnIndex;
	}

	public void setColumnIndex(final Long val){
		this.columnIndex = val;
	}

	public String getColumnSortType(){
		return this.columnSortType;
	}

	public void setColumnSortType(final String val){
		this.columnSortType = val;
	}

	public Long getColumnSortOrder(){
		return this.columnSortOrder;
	}

	public void setColumnSortOrder(final Long val){
		this.columnSortOrder = val;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName セットする displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return attrName
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * @param attrName セットする attrName
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
}
