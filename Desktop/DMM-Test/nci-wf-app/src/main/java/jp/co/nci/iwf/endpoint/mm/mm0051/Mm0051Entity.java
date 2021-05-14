package jp.co.nci.iwf.endpoint.mm.mm0051;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Mm0051Entity extends BaseJpaEntity {
	@Id
	@Column(name="PARTS_NUMBERING_FORMAT_ID")
	public long partsNumberingFormatId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="FORMAT_TYPE1")
	public String formatType1;

	@Column(name="FORMAT_TYPE2")
	public String formatType2;

	@Column(name="FORMAT_TYPE3")
	public String formatType3;

	@Column(name="FORMAT_TYPE4")
	public String formatType4;

	@Column(name="FORMAT_TYPE5")
	public String formatType5;

	@Column(name="FORMAT_TYPE6")
	public String formatType6;

	@Column(name="FORMAT_TYPE7")
	public String formatType7;

	@Column(name="FORMAT_TYPE8")
	public String formatType8;

	@Column(name="FORMAT_TYPE9")
	public String formatType9;

	@Column(name="FORMAT_VALUE1")
	public String formatValue1;

	@Column(name="FORMAT_VALUE2")
	public String formatValue2;

	@Column(name="FORMAT_VALUE3")
	public String formatValue3;

	@Column(name="FORMAT_VALUE4")
	public String formatValue4;

	@Column(name="FORMAT_VALUE5")
	public String formatValue5;

	@Column(name="FORMAT_VALUE6")
	public String formatValue6;

	@Column(name="FORMAT_VALUE7")
	public String formatValue7;

	@Column(name="FORMAT_VALUE8")
	public String formatValue8;

	@Column(name="FORMAT_VALUE9")
	public String formatValue9;

	@Column(name="GROUPING_FLAG1")
	public String groupingFlag1;

	@Column(name="GROUPING_FLAG2")
	public String groupingFlag2;

	@Column(name="GROUPING_FLAG3")
	public String groupingFlag3;

	@Column(name="GROUPING_FLAG4")
	public String groupingFlag4;

	@Column(name="GROUPING_FLAG5")
	public String groupingFlag5;

	@Column(name="GROUPING_FLAG6")
	public String groupingFlag6;

	@Column(name="GROUPING_FLAG7")
	public String groupingFlag7;

	@Column(name="GROUPING_FLAG8")
	public String groupingFlag8;

	@Column(name="GROUPING_FLAG9")
	public String groupingFlag9;

	@Column(name="NUMBERING_FORMAT")
	public String numberingFormat;

	@Column(name="PARTS_NUMBERING_FORMAT_CODE")
	public String partsNumberingFormatCode;

	@Column(name="PARTS_NUMBERING_FORMAT_NAME")
	public String partsNumberingFormatName;

	@Column(name="VERSION")
	public Long version;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="DELETE_FLAG_NAME")
	public String deleteFlagName;
}
