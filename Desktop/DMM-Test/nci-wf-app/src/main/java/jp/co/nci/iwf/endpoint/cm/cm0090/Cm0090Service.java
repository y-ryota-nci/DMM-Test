package jp.co.nci.iwf.endpoint.cm.cm0090;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.BelongType;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfcChangeRoleUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignRoleOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.cm.CmBaseService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 *参加者ロール検索サービス
 */
@BizLogic
public class Cm0090Service extends CmBaseService<WfcChangeRoleUser> {
	@Inject
	private WfInstanceWrapper wf;

	/** 文書フォルダや文書情報に設定する権限において選択可能な参加者ロール(ロール構成)の所属区分値 */
	private static final Set<String> BELONG_TYPES_4_DOC = new HashSet<>(
			Arrays.asList(BelongType.ALL_OF_CORP, BelongType.ORGANIZATION, BelongType.ORG_POST, BelongType.POST,  BelongType.USER)
	);

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Cm0090Request req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Cm0090Response search(Cm0090Request req) {
		SearchWfmAssignRoleInParam inParam = new SearchWfmAssignRoleInParam();
		inParam.setCorporationCode(req.corporationCode);
		inParam.setAssignRoleCode(req.assignRoleCode);
		inParam.setAssignRoleName(req.assignRoleName);
		inParam.setValidStartDate(today());
		inParam.setDeleteFlag(DeleteFlag.OFF);
		inParam.setOrderBy(toOrderBy(req, "R."));
		inParam.setPageNo(req.pageNo);
		inParam.setPageSize(req.pageSize);
		// 文書管理用の参加者ロール検索の場合、所属区分の絞込み条件を追加
		if (eq(CommonFlag.ON, req.docFlag)) {
			inParam.setBelongTypes(BELONG_TYPES_4_DOC);
		}
		SearchWfmAssignRoleOutParam outParam = wf.searchWfmAssignRole(inParam);

		// 総件数でページ番号等を補正しつつ、レスポンスを生成
		final Cm0090Response res = createResponse(Cm0090Response.class, req, outParam.getCount());
		res.allCount = outParam.getCount();
		res.results = outParam.getAssignRoles();
		res.success = true;
		return res;
	}

}
