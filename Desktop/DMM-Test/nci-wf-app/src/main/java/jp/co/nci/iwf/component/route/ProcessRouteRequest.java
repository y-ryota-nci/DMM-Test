package jp.co.nci.iwf.component.route;

import java.util.Map;

import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 承認ルート情報取得の基底リクエスト.
 */
public class ProcessRouteRequest extends BaseRequest {

	/** 会社コード */
	public String corporationCode;
	/** プロセスID */
	public Long processId;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義コード枝番 */
	public String processDefDetailCode;
	/** アクティビティ定義コード */
	public String activityDefCode;
	/** 入力者情報 */
	public UserInfo processUserInfo;
	/** 起案担当者情報 */
	public UserInfo startUserInfo;
	/** 業務管理項目Map */
	public Map<String, String> bizInfos;
//	/** プロセス変数リスト */
//	public List<WftVariable> wftVariableList;
	/** シミュレーションか */
	public boolean simulation;
}
