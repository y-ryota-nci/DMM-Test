package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.TrayEntity;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ブロック：申請情報サービス
 */
@BizLogic
public class Bl0001Service extends BaseService implements CodeMaster {

	/**
	 * 入力者情報を取得
	 * @param trayEntity
	 * @return
	 */
	public UserInfo getProcessUserInfo(TrayEntity trayEntity) {
		UserInfo userInfo = new UserInfo();
		if (isEmpty(trayEntity.processId)) {
			copyProperties(sessionHolder.getLoginInfo(), userInfo);
		} else {
			userInfo.setCorporationCode(trayEntity.corporationCodeProcess);
			userInfo.setOrganizationCode(trayEntity.organizationCodeProcess);
			userInfo.setOrganizationName(trayEntity.organizationNameProcess);
			userInfo.setOrganizationCodeUp3(trayEntity.orgCodeUp3Process);
			userInfo.setOrganizationCode5(trayEntity.orgCode5Process);
			userInfo.setOrganizationName5(trayEntity.orgName5Process);
			userInfo.setPostCode(trayEntity.postCodeProcess);
			userInfo.setPostName(trayEntity.postNameProcess);
			userInfo.setUserCode(trayEntity.userCodeProcess);
			userInfo.setUserName(trayEntity.userNameProcess);
			userInfo.setExtendedInfo01(trayEntity.sbmtrAddrCdProcess);
			userInfo.setSbmtrAddr(trayEntity.sbmtrAddrProcess);
		}
		return userInfo;
	}

	/**
	 * 起案担当者情報を取得
	 * @param trayEntity
	 * @return
	 */
	public UserInfo getStartUserInfo(TrayEntity trayEntity) {
		UserInfo userInfo = new UserInfo();
		if (isEmpty(trayEntity.processId)) {
			copyProperties(sessionHolder.getLoginInfo(), userInfo);
		} else {
			userInfo.setCorporationCode(trayEntity.corporationCodeStart);
			userInfo.setOrganizationCode(trayEntity.organizationCodeStart);
			userInfo.setOrganizationName(trayEntity.organizationNameStart);
			userInfo.setOrganizationCodeUp3(trayEntity.orgCodeUp3Start);
			userInfo.setOrganizationCode5(trayEntity.orgCode5Start);
			userInfo.setOrganizationName5(trayEntity.orgName5Start);
			userInfo.setPostCode(trayEntity.postCodeStart);
			userInfo.setPostName(trayEntity.postNameStart);
			userInfo.setUserCode(trayEntity.userCodeStart);
			userInfo.setUserName(trayEntity.userNameStart);
			userInfo.setExtendedInfo01(trayEntity.sbmtrAddrCdStart);
			userInfo.setSbmtrAddr(trayEntity.sbmtrAddrStart);
			userInfo.setPayApplCd(trayEntity.payApplCd);
		}
		return userInfo;
	}

}
