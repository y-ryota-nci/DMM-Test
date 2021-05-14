package jp.co.nci.iwf.endpoint.mm.mm0003;

import java.sql.Date;
import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 【プロファイル管理】ユーザ編集画面のユーザ更新レスポンス
 */
public class Mm0003UpdateUserRequest extends BaseRequest {
	/** 更新対象ユーザ */
	public WfmUser user;
	/** 設定されているユーザ所属情報 */
	public List<WfcUserBelong>  belongs;
	/** 基準日 */
	public Date baseDate;

	public Object menuRoles;
	public Object authTransfers;
	public Object reverseAuthTransfers;
}
