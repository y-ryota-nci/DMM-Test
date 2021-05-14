package jp.co.nci.iwf.endpoint.wl.wl0100;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 起案担当者選択画面の検索リクエスト
 */
public class Wl0100Request extends BasePagingRequest {
	/** 企業コード */
	public String corporationCode;
	/** 入力担当者のユーザコード */
	public String userCodeTransfer;

	/** プロセス定義の企業コード */
	public String corporationCodeP;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義明細コード */
	public String processDefDetailCode;

	/** 起案担当者のユーザコード */
	public String userCode;
	/** 起案担当者のユーザID */
	public String userAddedInfo;
	/** 起案担当者のユーザ名 */
	public String userName;
	/** 起案担当者の組織コード */
	public String organizationCode;
	/** 起案担当者の組織ID */
	public String organizationAddedInfo;
	/** 起案担当者の組織名 */
	public String organizationName;
	/** 起案担当者の組織階層名 */
	public String organizationTreeName;
	/** 起案担当者の役職コード */
	public String postCode;
	/** 起案担当者の役職ID */
	public String postAddedInfo;
	/** 起案担当者の役職名 */
	public String postName;
	/** 主務兼務区分 */
	public String jobType;
}
