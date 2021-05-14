package jp.co.nci.iwf.endpoint.mm.mm0003;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Mm0003User implements Serializable {
	/** '会社コード(CORPORATION_CODE). */
	public String corporationCode;
	/** 'ユーザコード(USER_CODE). */
	public String userCode;
	/** 'ユーザ名称(USER_NAME). */
	public String userName;
	/** 'ユーザ名称(USER_NAME_ABBR). */
	public String userNameAbbr;
	/** '郵便番号(POST_NUM). */
	public String postNum;
	/** '住所(ADDRESS). */
	public String address;
	/** '電話番号(TEL_NUM). */
	public String telNum;
	/** '電話番号(TEL_NUM_CEL). */
	public String telNumCel;
	/** 'メールアドレス(MAIL_ADDRESS). */
	public String mailAddress;
	/** 'ユーザ付加情報(USER_ADDED_INFO). */
	public String userAddedInfo;
	/** '印章名称(SEAL_NAME). */
	public String sealName;
	/** '拡張情報(EXTENDED_INFO_01). */
	public String extendedInfo01;
	/** '拡張情報(EXTENDED_INFO_02). */
	public String extendedInfo02;
	/** '拡張情報(EXTENDED_INFO_03). */
	public String extendedInfo03;
	/** '拡張情報(EXTENDED_INFO_04). */
	public String extendedInfo04;
	/** '拡張情報(EXTENDED_INFO_05). */
	public String extendedInfo05;
	/** '有効期間開始年月日(VALID_START_DATE). */
	public Date validStartDate;
	/** '有効期間終了年月日(VALID_END_DATE). */
	public Date validEndDate;
	/** '削除フラグ(DELETE_FLAG). */
	public String deleteFlag;
	/** 'ID(ID). */
	public Long id;
	/** 'タイムスタンプ(TIMESTAMP_UPDATED). */
	public Timestamp timestampUpdated;

}
