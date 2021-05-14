package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.authenticate.OrganizationInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.DefaultOrganizationSelect;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleOrganizationSelect;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOrganizationSelect;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】組織選択パーツ
 */
public class PartsOrganizationSelect extends PartsBase<PartsDesignOrganizationSelect> implements RoleOrganizationSelect, DefaultOrganizationSelect {

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignOrganizationSelect d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		// 必須入力
		if (checkRequired && d.requiredFlag) {
			// 企業コード＋組織コードを必須とする
			if (MiscUtils.isEmpty(values.get(CORPORATION_CODE))
					|| MiscUtils.isEmpty(values.get(ORGANIZATION_CODE))) {

				String role = d.organizationNameDisplay ? ORGANIZATION_NAME
						: d.organizationNameAbbrDisplay ? ORGANIZATION_NAME_ABBR
						: d.organizationAddedInfoDisplay ? ORGANIZATION_ADDED_INFO
						: d.telNumDisplay ? TEL_NUM
						: d.faxNumDisplay ? FAX_NUM
						: d.addressDisplay ? ADDRESS : null;
				return new PartsValidationResult(htmlId, role, d.labelText, MessageCd.MSG0074);
			}
		}
		// 入力値の型や桁数はマスタから引くものなので、特に要らないだろう...

		return null;
	}


	/** 現状の値をクリアしてデフォルト値をセット */
	@Override
	public void clearAndSetDefaultValue(PartsDesign d) {
		values.clear();

		// デフォルト値
		if (LOGIN_USER.equals(d.defaultValue)) {
			PartsDesignOrganizationSelect design = (PartsDesignOrganizationSelect)d;
			final OrganizationInfo org = LoginInfo.get().getMainOrganizationInfo();
			// 会社コード、組織コードは必ず設定
			values.put(CORPORATION_CODE, LoginInfo.get().getCorporationCode());
			values.put(ORGANIZATION_CODE, org.getOrganizationCode());
			if (design.organizationNameDisplay)
				values.put(ORGANIZATION_NAME, org.getOrganizationName());
			if (design.organizationAddedInfoDisplay)
				values.put(ORGANIZATION_ADDED_INFO, org.getOrganizationAddedInfo());
			if (design.organizationNameAbbrDisplay)
				values.put(ORGANIZATION_NAME_ABBR, org.getOrganizationNameAbbr());
			if (design.telNumDisplay)
				values.put(TEL_NUM, org.getTelNum());
			if (design.faxNumDisplay)
				values.put(ADDRESS, org.getAddress());
			if (design.addressDisplay)
				values.put(FAX_NUM, org.getFaxNum());
		}
	}
}
