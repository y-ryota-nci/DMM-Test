package jp.co.nci.iwf.endpoint.mm.mm0500;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * マスター初期値設定エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Mm0500Entity extends BaseJpaEntity {
	@Id
	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="CORPORATION_NAME")
	public String corporationName;

	@Column(name="LOCALE_CODE")
	public String localeCode;

	@Column(name="WFM_LOOKUP_TYPE_ALL")
	@JsonIgnore
	public Integer wfmLookupTypeAll;

	@Column(name="WFM_LOOKUP_TYPE_COUNT")
	@JsonIgnore
	public Integer wfmLookupTypeCount;

	@Column(name="WFM_LOOKUP_ALL")
	@JsonIgnore
	public Integer wfmLookupAll;

	@Column(name="WFM_LOOKUP_COUNT")
	@JsonIgnore
	public Integer wfmLookupCount;

	@Column(name="WFM_ACTION_ALL")
	@JsonIgnore
	public Integer wfmActionAll;

	@Column(name="WFM_ACTION_COUNT")
	@JsonIgnore
	public Integer wfmActionCount;

	@Column(name="WFM_FUNCTION_ALL")
	@JsonIgnore
	public Integer wfmFunctionAll;

	@Column(name="WFM_FUNCTION_COUNT")
	@JsonIgnore
	public Integer wfmFunctionCount;

	@Column(name="MWM_LOOKUP_GROUP_ALL")
	@JsonIgnore
	public Integer mwmLookupGroupAll;

	@Column(name="MWM_LOOKUP_GROUP_COUNT")
	@JsonIgnore
	public Integer mwmLookupGroupCount;

	@Column(name="MWM_LOOKUP_ALL")
	@JsonIgnore
	public Integer mwmLookupAll;

	@Column(name="MWM_LOOKUP_COUNT")
	@JsonIgnore
	public Integer mwmLookupCount;

	@Column(name="MWM_BUSINESS_INFO_NAME_ALL")
	@JsonIgnore
	public Integer mwmBusinessInfoNameAll;

	@Column(name="MWM_BUSINESS_INFO_NAME_COUNT")
	@JsonIgnore
	public Integer mwmBusinessInfoNameCount;

	public String wfmLookupType;

	public String wfmLookup;

	public String wfmAction;

	public String wfmFunction;

	public String mwmLookupGroup;

	public String mwmLookup;

	public String businessInfo;

	@Column(name="MWM_DOC_BUSINESS_INFO_NAME_ALL")
	@JsonIgnore
	public Integer mwmDocBusinessInfoNameAll;

	@Column(name="MWM_DOC_BUSINESS_INFO_NAME_COUNT")
	@JsonIgnore
	public Integer mwmDocBusinessInfoNameCount;

	public String docBusinessInfo;

	@Column(name="MWM_TRAY_CONFIG_ALL")
	@JsonIgnore
	public Integer mwmTrayConfigAll;

	@Column(name="MWM_TRAY_CONFIG_COUNT")
	@JsonIgnore
	public Integer mwmTrayConfigCount;

	public String trayConfig;

	@Column(name="MWM_DOC_TRAY_CONFIG_ALL")
	@JsonIgnore
	public Integer mwmDocTrayConfigAll;

	@Column(name="MWM_DOC_TRAY_CONFIG_COUNT")
	@JsonIgnore
	public Integer mwmDocTrayConfigCount;

	public String docTrayConfig;

	@Column(name="MWM_MAIL_VARIABLE_ALL")
	@JsonIgnore
	public Integer mwmMailVariableAll;

	@Column(name="MWM_MAIL_VARIABLE_COUNT")
	@JsonIgnore
	public Integer mwmMailVariableCount;

	public String mailVaribale;

	@Column(name="MWM_OPTION_ALL")
	@JsonIgnore
	public Integer MwmOptionAll;

	@Column(name="MWM_OPTION_COUNT")
	@JsonIgnore
	public Integer MwmOptionCount;

	public String optionSetting;


	@Column(name="MWM_OPTION_ITEM_ALL")
	@JsonIgnore
	public Integer MwmOptionItemAll;

	@Column(name="MWM_OPTION_ITEM_COUNT")
	@JsonIgnore
	public Integer MwmOptionItemCount;

	public String optionItemSetting;

	public boolean selectable;
}
