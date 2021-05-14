package jp.co.nci.iwf.endpoint.mm.mm0440;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ユーザ一覧画面の検索リクエスト
 */
public class Mm0440Request extends BasePagingRequest {

	public String corporationCode;
	public String userAddedInfo;
	public String userName;
	public String mailAddress;
	public String telNum;
	public String telNumCel;
	public String deleteFlag;
	public Date validEndDate;
	public Date validStartDate;
	public String defaultLocaleCode;
}
