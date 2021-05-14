package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ルート作成レスポンス
 */
public class Mm0302InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	// 共通選択肢
	public List<OptionItem> executionTermUnitTypes;

	// プロセス用選択肢
	public List<OptionItem> processOpeTypes;

	// 比較条件式用選択肢
	public List<OptionItem> operatorTypes;

	// アクティビティ用選択肢
	public List<OptionItem> skipRuleTypes;
	public List<OptionItem> activityKindTypes;
	public List<OptionItem> activityEndTypes;
	public List<OptionItem> activityTypes;
	public List<OptionItem> waitTypes;
	public List<OptionItem> dcIds;

	// 参加者用選択肢
	public List<WfmAssignRole> assignRoles;
	// 参加者変更用選択肢
	public List<WfmChangeRole> changeRoles;

	// アクション用選択肢
	public List<WfmAction> actions;
	public List<OptionItem> actionDefTypes;

	// 遷移先用選択肢
	public List<OptionItem> businessProcessStatuses;
	public List<OptionItem> businessActivityStatuses;

	// 機能用選択肢
	public List<WfmFunction> functions;
	public List<OptionItem> executionTimingTypes;

	public Mm0302Templates templates;
	public Mm0302Contents contents;

}