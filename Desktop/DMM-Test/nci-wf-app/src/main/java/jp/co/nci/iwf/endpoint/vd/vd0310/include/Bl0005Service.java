package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.AppendProcessRelationInParam;
import jp.co.nci.integrated_workflow.api.param.input.RemoveProcessRelationInParam;
import jp.co.nci.integrated_workflow.api.param.output.AppendProcessRelationOutParam;
import jp.co.nci.integrated_workflow.api.param.output.RemoveProcessRelationOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.base.WftProcessRelation;
import jp.co.nci.integrated_workflow.model.base.impl.WftProcessRelationImpl;
import jp.co.nci.integrated_workflow.param.input.SearchWftProcessRelationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.ApprovalRelationInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ブロック：決裁関連文書ブロック用サービス
 */
@BizLogic
public class Bl0005Service extends BaseService implements CodeMaster {
	@Inject private WfInstanceWrapper wf;

	/**
	 * 決裁関連文書一覧を取得
	 * @param req
	 * @return
	 */
	public Bl0005Response getApprovalRelationList(Bl0005Request req) {
		final Bl0005Response res = createResponse(Bl0005Response.class, req);
		res.approvalRelationList = getApprovalRelationList(req.corporationCode, req.processId);
		res.success = true;
		return res;
	}

	/**
	 * 決裁関連文書一覧を取得
	 * @param corporationCode 会社コード
	 * @param processId プロセスID
	 * @return 決裁関連文書一覧
	 */
	public List<ApprovalRelationInfo> getApprovalRelationList(String corporationCode,Long processId){
		if (isEmpty(corporationCode) || processId == null)
			return null;

		final SearchWftProcessRelationInParam in = new SearchWftProcessRelationInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);
		in.setProcessRelationType(ProcessRelationType.RELATION);

		// 決裁関連テーブル検索
		return wf.searchWftProcessRelation(in).getResultList().stream()
				.map(e -> new ApprovalRelationInfo(e))
				.collect(Collectors.toList());
	}

	/**
	 * 決裁関連文書を更新
	 * @param req
	 * @param res
	 */
	public void execute(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// プロセスインスタンス連携IDが枝番に相当するので、既存の関連文書情報を抽出して連携元プロセスIDをキーにMap化
		final Map<Long, ApprovalRelationInfo> currents =
				getApprovalRelationList(req.contents.corporationCode, req.contents.processId)
				.stream().collect(Collectors.toMap(e -> e.processRelationId, e -> e));

		final List<ApprovalRelationInfo> inputs = req.approvalRelationList;
		for (ApprovalRelationInfo input : inputs) {
			// 現在も使用中の関連文書を除去していく。
			// 除去できたものは現在も使用中、除去できなかったものは新規、残余は未使用とみなす
			final Long processRelationId = input.processRelationId;
			ApprovalRelationInfo current = null;
			if (processRelationId != null) {
				current = currents.remove(processRelationId);
			}

			if (current == null)
				insert(req.contents, input);
			else
				;	// 現在も使用中の関連文書。書き換える個所が何もないので、update()はない
		}
		// 残余は不要になった関連文書なので削除
		for (ApprovalRelationInfo current : currents.values()) {
			delete(current);
		}
	}

	/** 関連文書を削除 */
	private void delete(ApprovalRelationInfo current) {
		final WftProcessRelation pr = new WftProcessRelationImpl();
		pr.setCorporationCode(current.corporationCode);
		pr.setProcessId(current.processId);
		pr.setProcessRelationId(current.processRelationId);

		final RemoveProcessRelationInParam in = new RemoveProcessRelationInParam();
		in.setWftProcessRelation(pr);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final RemoveProcessRelationOutParam out = wf.removeProcessRelation(in);
		if (!eq(ReturnCode.SUCCESS, out.getReturnCode())) {
			throw new WfException(out);
		}
	}

	/** 決裁関連文書情報を新規登録 */
	private void insert(Vd0310Contents contents, ApprovalRelationInfo input) {
		final AppendProcessRelationInParam in = new AppendProcessRelationInParam();
		in.setCorporationCode(contents.corporationCode);
		in.setProcessId(contents.processId);
		in.setCorporationCodeRelation(contents.corporationCode);
		in.setProcessIdRelation(input.processIdRelation);
		in.setProcessRelationType(ProcessRelationType.RELATION);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final AppendProcessRelationOutParam out = wf.appendProcessRelation(in);
		if (!eq(ReturnCode.SUCCESS, out.getReturnCode())) {
			throw new WfException(out);
		}
	}
}
