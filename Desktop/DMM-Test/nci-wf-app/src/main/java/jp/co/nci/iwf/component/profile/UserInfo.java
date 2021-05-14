package jp.co.nci.iwf.component.profile;

import java.io.Serializable;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.util.MiscUtils;

public class UserInfo implements Serializable {
	/** 会社コード */
	private String corporationCode;
	/** 会社名称 */
	private String corporationName;
//	/** 会社付加情報 */
//	private String corporationAddedInfo;
	/** 組織コード(主務) */
	private String organizationCode;
	/** 組織名称(主務) */
	private String organizationName;
	/** 組織付加情報(主務) */
	private String organizationAddedInfo;
	/** 役職コード(主務) */
	private String postCode;
	/** 役職名称(主務) */
	private String postName;
	/** 役職付加情報(主務) */
	private String postAddedInfo;
	/** ユーザコード */
	private String userCode;
	/** ユーザ氏名 */
	private String userName;
	/** ユーザ略称 */
	private String userNameAbbr;
	/** ユーザ付加情報 */
	private String userAddedInfo;
	/** 電話番号 */
	private String telNum;
	/** 携帯電話番号 */
	private String telNumCel;
	/** メールアドレス */
	private String mailAddress;
	/** 拡張情報01（所在地コード） */
	private String extendedInfo01;
	/** 所在地 */
	private String sbmtrAddr;
	/** 組織レベル3,4,5からみた組織レベル3の組織コード（上位組織コード）。組織レベル3,4,5以外のときはnull */
	private String organizationCodeUp3;
	/** 組織レベル5のときのみ組織コードが入る。それ以外はnull */
	private String organizationCode5;
	/** 組織レベル5のときのみ組織名が入る。それ以外はnull */
	private String organizationName5;
	/** 支払業務コード */
	private String payApplCd;

	/** デフォルトコンストラクタ */
	public UserInfo() {
	}

	/**
	 * コンストラクタ。ログイン者をもとにインスタンス化
	 * @param login ログイン者情報
	 */
	public UserInfo(LoginInfo login) {
		MiscUtils.copyProperties(login, this);
	}

	/** 会社コード */
	public String getCorporationCode() {
		return corporationCode;
	}
	/** 会社コード */
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	/** 会社名称 */
	public String getCorporationName() {
		return corporationName;
	}
	/** 会社名称 */
	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}
//
//	/** 会社付加情報 */
//	public String getCorporationAddedInfo() {
//		return corporationAddedInfo;
//	}
//	/** 会社付加情報 */
//	public void setCorporationAddedInfo(String corporationAddedInfo) {
//		this.corporationAddedInfo = corporationAddedInfo;
//	}

	/** 組織コード */
	public String getOrganizationCode() {
		return organizationCode;
	}
	/** 組織コード */
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	/** 組織名称 */
	public String getOrganizationName() {
		return organizationName;
	}
	/** 組織名称 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	/** 組織付加情報 */
	public String getOrganizationAddedInfo() {
		return organizationAddedInfo;
	}
	/** 組織付加情報 */
	public void setOrganizationAddedInfo(String organizationAddedInfo) {
		this.organizationAddedInfo = organizationAddedInfo;
	}

	/** 役職コード */
	public String getPostCode() {
		return postCode;
	}
	/** 役職コード */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/** 役職名称 */
	public String getPostName() {
		return postName;
	}
	/** 役職名称 */
	public void setPostName(String postName) {
		this.postName = postName;
	}

	/** 役職付加情報 */
	public String getPostAddedInfo() {
		return postAddedInfo;
	}
	/** 役職付加情報 */
	public void setPostAddedInfo(String postAddedInfo) {
		this.postAddedInfo = postAddedInfo;
	}

	/** ユーザコード */
	public String getUserCode() {
		return userCode;
	}
	/** ユーザコード */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/** ユーザ氏名 */
	public String getUserName() {
		return userName;
	}
	/** ユーザ氏名 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** ユーザ略称 */
	public String getUserNameAbbr() {
		return userNameAbbr;
	}
	/** ユーザ略称 */
	public void setUserNameAbbr(String userNameAbbr) {
		this.userNameAbbr = userNameAbbr;
	}

	/** ユーザ付加情報 */
	public String getUserAddedInfo() {
		return userAddedInfo;
	}
	/** ユーザ付加情報 */
	public void setUserAddedInfo(String userAddedInfo) {
		this.userAddedInfo = userAddedInfo;
	}

	/** 電話番号 */
	public String getTelNum() {
		return telNum;
	}
	/** 電話番号 */
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	/** 携帯電話番号 */
	public String getTelNumCel() {
		return telNumCel;
	}
	/** 携帯電話番号 */
	public void setTelNumCel(String telNumCel) {
		this.telNumCel = telNumCel;
	}

	/** メールアドレス */
	public String getMailAddress() {
		return mailAddress;
	}
	/** メールアドレス */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/** 拡張情報01 */
	public String getExtendedInfo01() {
		return extendedInfo01;
	}
	/** 拡張情報01 */
	public void setExtendedInfo01(String extendedInfo01) {
		this.extendedInfo01 = extendedInfo01;
	}

	/** 所在地 */
	public String getSbmtrAddr() {
		return sbmtrAddr;
	}
	/** 所在地 */
	public void setSbmtrAddr(String sbmtrAddr) {
		this.sbmtrAddr = sbmtrAddr;
	}

	/** 組織レベル3,4,5からみた組織レベル3の組織コード（上位組織コード）。組織レベル3,4,5以外のときはnull */
	public String getOrganizationCodeUp3() {
		return organizationCodeUp3;
	}
	/** 組織レベル3,4,5からみた組織レベル3の組織コード（上位組織コード）。組織レベル3,4,5以外のときはnull */
	public void setOrganizationCodeUp3(String organizationCodeUp3) {
		this.organizationCodeUp3 = organizationCodeUp3;
	}

	/** 組織レベル5のときのみ組織コードが入る。それ以外はnull */
	public String getOrganizationCode5() {
		return organizationCode5;
	}
	/** 組織レベル5のときのみ組織コードが入る。それ以外はnull */
	public void setOrganizationCode5(String organizationCode5) {
		this.organizationCode5 = organizationCode5;
	}

	/** 組織レベル5のときのみ組織名が入る。それ以外はnull */
	public String getOrganizationName5() {
		return organizationName5;
	}
	/** 組織レベル5のときのみ組織名が入る。それ以外はnull */
	public void setOrganizationName5(String organizationName5) {
		this.organizationName5 = organizationName5;
	}

	/** 支払業務コード */
	public String getPayApplCd() {
		return payApplCd;
	}
	/** 支払業務コード */
	public void setPayApplCd(String payApplCd) {
		this.payApplCd = payApplCd;
	}

	@Override
	public String toString() {
		return new StringBuilder(32)
				.append(corporationCode).append("/")
				.append(userCode).append(" ")
				.append(userName)
				.toString();
	}
}
