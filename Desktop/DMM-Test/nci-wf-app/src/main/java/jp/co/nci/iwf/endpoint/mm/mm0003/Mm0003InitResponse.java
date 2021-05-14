package jp.co.nci.iwf.endpoint.mm.mm0003;

import java.sql.Date;
import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfcUserBelong;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 【プロファイル管理】ユーザ編集画面の初期化レスポンス
 */
public class Mm0003InitResponse extends BaseResponse {
	/** ユーザ情報 */
	public WfmUser user;
	/** ユーザ所属情報 */
	public List<WfcUserBelong> belongs;
	/** 基準日 */
	public Date baseDate;
	/** 言語の選択肢 */
	public List<OptionItem> localeCodes;
}
