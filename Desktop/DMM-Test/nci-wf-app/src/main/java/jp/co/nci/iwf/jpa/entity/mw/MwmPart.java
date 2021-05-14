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
 * The persistent class for the MWM_PARTS database table.
 *
 */
@Entity
@Table(name="MWM_PARTS", uniqueConstraints=@UniqueConstraint(columnNames={"CONTAINER_ID","PARTS_CODE"}))
@NamedQuery(name="MwmPart.findAll", query="SELECT m FROM MwmPart m")
public class MwmPart extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_ID")
	private long partsId;

	@Column(name="BG_COLOR_INPUT")
	private String bgColorInput;

	@Column(name="BG_COLOR_REFER")
	private String bgColorRefer;

	@Column(name="BG_HTML_CELL_NO")
	private Integer bgHtmlCellNo;

	@Column(name="BG_TRANSPARENT_FLAG")
	private String bgTransparentFlag;

	@Column(name="COL_LG")
	private Integer colLg;

	@Column(name="COL_MD")
	private Integer colMd;

	@Column(name="COL_SM")
	private Integer colSm;

	@Column(name="COL_XS")
	private Integer colXs;

	@Column(name="CONTAINER_ID")
	private Long containerId;

	@Column(name="COPY_TARGET_FLAG")
	private String copyTargetFlag;

	@Column(name="CSS_CLASS")
	private String cssClass;

	@Column(name="CSS_STYLE")
	private String cssStyle;

	private String description;

	@Lob
	@Column(name="EXT_INFO")
	private String extInfo;

	@Column(name="FONT_BOLD")
	private String fontBold;

	@Column(name="FONT_COLOR")
	private String fontColor;

	@Column(name="FONT_SIZE")
	private Integer fontSize;

	@Column(name="GRANT_TAB_INDEX_FLAG")
	private String grantTabIndexFlag;

	@Column(name="LABEL_TEXT")
	private String labelText;

	@Column(name="MOBILE_INVISIBLE_FLAG")
	private String mobileInvisibleFlag;

	@Column(name="PARTS_CODE")
	private String partsCode;

	@Column(name="PARTS_TYPE")
	private Integer partsType;

	@Column(name="RENDERING_METHOD")
	private Integer renderingMethod;

	@Column(name="REQUIRED_FLAG")
	private String requiredFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="BUSINESS_INFO_CODE")
	private String businessInfoCode;

	@Column(name="DOC_BUSINESS_INFO_CODE")
	private String docBusinessInfoCode;

	public MwmPart() {
	}

	public long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(long partsId) {
		this.partsId = partsId;
	}

	public String getBgColorInput() {
		return this.bgColorInput;
	}

	public void setBgColorInput(String bgColorInput) {
		this.bgColorInput = bgColorInput;
	}

	public String getBgColorRefer() {
		return this.bgColorRefer;
	}

	public void setBgColorRefer(String bgColorRefer) {
		this.bgColorRefer = bgColorRefer;
	}

	public Integer getBgHtmlCellNo() {
		return this.bgHtmlCellNo;
	}

	public void setBgHtmlCellNo(Integer bgHtmlCellNo) {
		this.bgHtmlCellNo = bgHtmlCellNo;
	}

	public String getBgTransparentFlag() {
		return this.bgTransparentFlag;
	}

	public void setBgTransparentFlag(String bgTransparentFlag) {
		this.bgTransparentFlag = bgTransparentFlag;
	}

	public Integer getColLg() {
		return this.colLg;
	}

	public void setColLg(Integer colLg) {
		this.colLg = colLg;
	}

	public Integer getColMd() {
		return this.colMd;
	}

	public void setColMd(Integer colMd) {
		this.colMd = colMd;
	}

	public Integer getColSm() {
		return this.colSm;
	}

	public void setColSm(Integer colSm) {
		this.colSm = colSm;
	}

	public Integer getColXs() {
		return this.colXs;
	}

	public void setColXs(Integer colXs) {
		this.colXs = colXs;
	}

	public Long getContainerId() {
		return this.containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public String getCopyTargetFlag() {
		return this.copyTargetFlag;
	}

	public void setCopyTargetFlag(String copyTargetFlag) {
		this.copyTargetFlag = copyTargetFlag;
	}

	public String getCssClass() {
		return this.cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssStyle() {
		return this.cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtInfo() {
		return this.extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public String getFontBold() {
		return this.fontBold;
	}

	public void setFontBold(String fontBold) {
		this.fontBold = fontBold;
	}

	public String getFontColor() {
		return this.fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public Integer getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public String getGrantTabIndexFlag() {
		return this.grantTabIndexFlag;
	}

	public void setGrantTabIndexFlag(String grantTabIndexFlag) {
		this.grantTabIndexFlag = grantTabIndexFlag;
	}

	public String getLabelText() {
		return this.labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public String getMobileInvisibleFlag() {
		return mobileInvisibleFlag;
	}

	public void setMobileInvisibleFlag(String mobileInvisibleFlag) {
		this.mobileInvisibleFlag = mobileInvisibleFlag;
	}

	public String getPartsCode() {
		return this.partsCode;
	}

	public void setPartsCode(String partsCode) {
		this.partsCode = partsCode;
	}

	public Integer getPartsType() {
		return this.partsType;
	}

	public void setPartsType(Integer partsType) {
		this.partsType = partsType;
	}

	public Integer getRenderingMethod() {
		return this.renderingMethod;
	}

	public void setRenderingMethod(Integer renderingMethod) {
		this.renderingMethod = renderingMethod;
	}

	public String getRequiredFlag() {
		return this.requiredFlag;
	}

	public void setRequiredFlag(String requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getBusinessInfoCode() {
		return this.businessInfoCode;
	}

	public void setBusinessInfoCode(String businessInfoCode) {
		this.businessInfoCode = businessInfoCode;
	}

	public String getDocBusinessInfoCode() {
		return docBusinessInfoCode;
	}

	public void setDocBusinessInfoCode(String docBusinessInfoCode) {
		this.docBusinessInfoCode = docBusinessInfoCode;
	}

}