package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WftInformationSharer;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.component.route.ActivityEntity;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.AttachFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.DocFileWfInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.PullbackInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.PullforwardInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.SendbackInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ApprovalRelationInfo;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 申請・承認画面の更新系リクエスト
 */
public class Vd0310ExecuteRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	// ロード時の申請・承認画面情報
	public Vd0310Contents contents;

	// API実行
//	public String actionType;
//	public String actionCode;
//	public String actionName;
	public ActionInfo actionInfo;
	public SendbackInfo sendbackInfo;
	public PullbackInfo pullbackInfo;
	public PullforwardInfo pullforwardInfo;

	public UserInfo startUserInfo;

//	// 引戻し・引上げ用
//	public String targetCorporationCode;
//	public String targetProcessDefCode;
//	public String targetProcessDefDetailCode;
//	public String targetActivityDefCode;

//	/** (差戻し用)アクション定義の連番 */
//	public Long seqNoActionDef;

	/** (承認時の)コメント */
	public String actionComment;

	/** 追加したWF添付ファイルのIDリスト */
	public List<AttachFileWfInfo> additionAttachFileWfList;
	/** 削除したWF添付ファイルのIDリスト */
	public List<Long> removeAttachFileWfIdList;

	/** 追加した参照者情報 */
	public List<WftInformationSharer> additionInformationSharerList;
	/** 削除した参照者情報 */
	public List<WftInformationSharer> removeInformationSharerList;

	/** (変更があった)承認ルート情報 */
	public List<ActivityEntity> changeRouteList;

	/** HtmlIdをキーとした実行時パーツMap */
	public Map<String, PartsBase<?>> runtimeMap = new LinkedHashMap<>();
	/** 決裁関連文書情報 */
	public List<ApprovalRelationInfo> approvalRelationList;

	/** 追加したWF文書ファイルのIDリスト */
	public List<DocFileWfInfo> additionDocFileWfList;
	/** 削除したWF文書ファイルのIDリスト */
	public List<Long> removeDocFileWfIdList;

}
