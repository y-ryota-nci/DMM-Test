package jp.co.nci.iwf.designer.parts.runtime;

import java.util.Map;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.DesignerCodeBook.DefaultUserSelect;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleUserSelect;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignUserSelect;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【実行時】ユーザ選択パーツ
 */
public class PartsUserSelect extends PartsBase<PartsDesignUserSelect> implements RoleUserSelect, DefaultUserSelect {

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignUserSelect d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		// 必須入力
		if (checkRequired && d.requiredFlag) {
			// 企業コード＋ユーザコードを必須とする
			if (MiscUtils.isEmpty(values.get(CORPORATION_CODE))
					|| MiscUtils.isEmpty(values.get(USER_CODE))) {

				String role = d.userNameDisplay ? USER_NAME
						: d.userNameAbbrDisplay ? USER_NAME_ABBR
						: d.userAddedInfoDisplay ? USER_ADDED_INFO
						: d.telNumDisplay ? TEL_NUM
						: d.telNumCelDisplay ? TEL_NUM_CEL
						: d.mailAddressDisplay ? MAIL_ADDRESS : null;
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
			PartsDesignUserSelect design = (PartsDesignUserSelect)d;
			final UserInfo user = LoginInfo.get();
			// 会社コード、ユーザコードは必ず設定
			values.put(CORPORATION_CODE, user.getCorporationCode());
			values.put(USER_CODE, user.getUserCode());
			if (design.userNameDisplay)
				values.put(USER_NAME, user.getUserName());
			if (design.userAddedInfoDisplay)
				values.put(USER_ADDED_INFO, user.getUserAddedInfo());
			if (design.userNameAbbrDisplay)
				values.put(USER_NAME_ABBR, user.getUserNameAbbr());
			if (design.telNumDisplay)
				values.put(TEL_NUM, user.getTelNum());
			if (design.telNumCelDisplay)
				values.put(TEL_NUM_CEL, user.getTelNumCel());
			if (design.mailAddressDisplay)
				values.put(MAIL_ADDRESS, user.getMailAddress());
		}
	}
}
