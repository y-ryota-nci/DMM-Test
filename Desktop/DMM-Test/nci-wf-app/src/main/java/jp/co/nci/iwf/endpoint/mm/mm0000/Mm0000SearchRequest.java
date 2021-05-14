package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロファイル管理画面の検索リクエスト
 */
public class Mm0000SearchRequest extends BaseRequest {
	/** 企業コード */
	public String corporationCode;
	/** 組織コード */
	public String organizationCodeUp;
	/** 名称 */
	public String name;
	/** 種別（組織/ユーザ） */
	public boolean searchUser;
	/** 基準日 */
	public Date baseDate;
	/** 検索種別 */
	public String type;
}
