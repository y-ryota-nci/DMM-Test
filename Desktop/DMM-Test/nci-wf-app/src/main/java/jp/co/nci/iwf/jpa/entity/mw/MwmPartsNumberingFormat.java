package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_NUMBERING_FORMAT database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_NUMBERING_FORMAT", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE","PARTS_NUMBERING_FORMAT_CODE"}))
@NamedQuery(name="MwmPartsNumberingFormat.findAll", query="SELECT m FROM MwmPartsNumberingFormat m")
public class MwmPartsNumberingFormat extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_NUMBERING_FORMAT_ID")
	private long partsNumberingFormatId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="FORMAT_TYPE1")
	private String formatType1;

	@Column(name="FORMAT_TYPE2")
	private String formatType2;

	@Column(name="FORMAT_TYPE3")
	private String formatType3;

	@Column(name="FORMAT_TYPE4")
	private String formatType4;

	@Column(name="FORMAT_TYPE5")
	private String formatType5;

	@Column(name="FORMAT_TYPE6")
	private String formatType6;

	@Column(name="FORMAT_TYPE7")
	private String formatType7;

	@Column(name="FORMAT_TYPE8")
	private String formatType8;

	@Column(name="FORMAT_TYPE9")
	private String formatType9;

	@Column(name="FORMAT_VALUE1")
	private String formatValue1;

	@Column(name="FORMAT_VALUE2")
	private String formatValue2;

	@Column(name="FORMAT_VALUE3")
	private String formatValue3;

	@Column(name="FORMAT_VALUE4")
	private String formatValue4;

	@Column(name="FORMAT_VALUE5")
	private String formatValue5;

	@Column(name="FORMAT_VALUE6")
	private String formatValue6;

	@Column(name="FORMAT_VALUE7")
	private String formatValue7;

	@Column(name="FORMAT_VALUE8")
	private String formatValue8;

	@Column(name="FORMAT_VALUE9")
	private String formatValue9;

	@Column(name="GROUPING_FLAG1")
	private String groupingFlag1;

	@Column(name="GROUPING_FLAG2")
	private String groupingFlag2;

	@Column(name="GROUPING_FLAG3")
	private String groupingFlag3;

	@Column(name="GROUPING_FLAG4")
	private String groupingFlag4;

	@Column(name="GROUPING_FLAG5")
	private String groupingFlag5;

	@Column(name="GROUPING_FLAG6")
	private String groupingFlag6;

	@Column(name="GROUPING_FLAG7")
	private String groupingFlag7;

	@Column(name="GROUPING_FLAG8")
	private String groupingFlag8;

	@Column(name="GROUPING_FLAG9")
	private String groupingFlag9;

	@Column(name="NUMBERING_FORMAT")
	private String numberingFormat;

	@Column(name="PARTS_NUMBERING_FORMAT_CODE")
	private String partsNumberingFormatCode;

	@Column(name="PARTS_NUMBERING_FORMAT_NAME")
	private String partsNumberingFormatName;

	public MwmPartsNumberingFormat() {
	}

	public long getPartsNumberingFormatId() {
		return this.partsNumberingFormatId;
	}

	public void setPartsNumberingFormatId(long partsNumberingFormatId) {
		this.partsNumberingFormatId = partsNumberingFormatId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getFormatType1() {
		return this.formatType1;
	}

	public void setFormatType1(String formatType1) {
		this.formatType1 = formatType1;
	}

	public String getFormatType2() {
		return this.formatType2;
	}

	public void setFormatType2(String formatType2) {
		this.formatType2 = formatType2;
	}

	public String getFormatType3() {
		return this.formatType3;
	}

	public void setFormatType3(String formatType3) {
		this.formatType3 = formatType3;
	}

	public String getFormatType4() {
		return this.formatType4;
	}

	public void setFormatType4(String formatType4) {
		this.formatType4 = formatType4;
	}

	public String getFormatType5() {
		return this.formatType5;
	}

	public void setFormatType5(String formatType5) {
		this.formatType5 = formatType5;
	}

	public String getFormatType6() {
		return this.formatType6;
	}

	public void setFormatType6(String formatType6) {
		this.formatType6 = formatType6;
	}

	public String getFormatType7() {
		return this.formatType7;
	}

	public void setFormatType7(String formatType7) {
		this.formatType7 = formatType7;
	}

	public String getFormatType8() {
		return this.formatType8;
	}

	public void setFormatType8(String formatType8) {
		this.formatType8 = formatType8;
	}

	public String getFormatType9() {
		return this.formatType9;
	}

	public void setFormatType9(String formatType9) {
		this.formatType9 = formatType9;
	}

	public String getFormatValue1() {
		return this.formatValue1;
	}

	public void setFormatValue1(String formatValue1) {
		this.formatValue1 = formatValue1;
	}

	public String getFormatValue2() {
		return this.formatValue2;
	}

	public void setFormatValue2(String formatValue2) {
		this.formatValue2 = formatValue2;
	}

	public String getFormatValue3() {
		return this.formatValue3;
	}

	public void setFormatValue3(String formatValue3) {
		this.formatValue3 = formatValue3;
	}

	public String getFormatValue4() {
		return this.formatValue4;
	}

	public void setFormatValue4(String formatValue4) {
		this.formatValue4 = formatValue4;
	}

	public String getFormatValue5() {
		return this.formatValue5;
	}

	public void setFormatValue5(String formatValue5) {
		this.formatValue5 = formatValue5;
	}

	public String getFormatValue6() {
		return this.formatValue6;
	}

	public void setFormatValue6(String formatValue6) {
		this.formatValue6 = formatValue6;
	}

	public String getFormatValue7() {
		return this.formatValue7;
	}

	public void setFormatValue7(String formatValue7) {
		this.formatValue7 = formatValue7;
	}

	public String getFormatValue8() {
		return this.formatValue8;
	}

	public void setFormatValue8(String formatValue8) {
		this.formatValue8 = formatValue8;
	}

	public String getFormatValue9() {
		return this.formatValue9;
	}

	public void setFormatValue9(String formatValue9) {
		this.formatValue9 = formatValue9;
	}

	public String getGroupingFlag1() {
		return this.groupingFlag1;
	}

	public void setGroupingFlag1(String groupingFlag1) {
		this.groupingFlag1 = groupingFlag1;
	}

	public String getGroupingFlag2() {
		return this.groupingFlag2;
	}

	public void setGroupingFlag2(String groupingFlag2) {
		this.groupingFlag2 = groupingFlag2;
	}

	public String getGroupingFlag3() {
		return this.groupingFlag3;
	}

	public void setGroupingFlag3(String groupingFlag3) {
		this.groupingFlag3 = groupingFlag3;
	}

	public String getGroupingFlag4() {
		return this.groupingFlag4;
	}

	public void setGroupingFlag4(String groupingFlag4) {
		this.groupingFlag4 = groupingFlag4;
	}

	public String getGroupingFlag5() {
		return this.groupingFlag5;
	}

	public void setGroupingFlag5(String groupingFlag5) {
		this.groupingFlag5 = groupingFlag5;
	}

	public String getGroupingFlag6() {
		return this.groupingFlag6;
	}

	public void setGroupingFlag6(String groupingFlag6) {
		this.groupingFlag6 = groupingFlag6;
	}

	public String getGroupingFlag7() {
		return this.groupingFlag7;
	}

	public void setGroupingFlag7(String groupingFlag7) {
		this.groupingFlag7 = groupingFlag7;
	}

	public String getGroupingFlag8() {
		return this.groupingFlag8;
	}

	public void setGroupingFlag8(String groupingFlag8) {
		this.groupingFlag8 = groupingFlag8;
	}

	public String getGroupingFlag9() {
		return this.groupingFlag9;
	}

	public void setGroupingFlag9(String groupingFlag9) {
		this.groupingFlag9 = groupingFlag9;
	}

	public String getNumberingFormat() {
		return this.numberingFormat;
	}

	public void setNumberingFormat(String numberingFormat) {
		this.numberingFormat = numberingFormat;
	}

	public String getPartsNumberingFormatCode() {
		return this.partsNumberingFormatCode;
	}

	public void setPartsNumberingFormatCode(String partsNumberingFormatCode) {
		this.partsNumberingFormatCode = partsNumberingFormatCode;
	}

	public String getPartsNumberingFormatName() {
		return this.partsNumberingFormatName;
	}

	public void setPartsNumberingFormatName(String partsNumberingFormatName) {
		this.partsNumberingFormatName = partsNumberingFormatName;
	}

}