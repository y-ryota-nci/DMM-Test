package jp.co.nci.iwf.component.document;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.GetActionHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.param.output.GetProcessHistoryListOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.model.view.WfvActionHistory;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.dc.dc0100.bean.DocWfRelationInfo;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwtDocWfRelation;

/**
 * 文書情報-WF連携サービス.
 */
@BizLogic
public class DocWfRelationService extends BaseService {

	/** WF API */
	@Inject private WfInstanceWrapper wf;
	/** 文書情報-WF連携リポジトリ */
	@Inject private DocWfRelationRepository repository;

	/**
	 * 文書WF連携情報の差分更新.
	 * ※差分更新と言っても実際は新規登録しかない
	 * @param corporationCode 会社コード
	 * @param processId プロセスID
	 * @param docId 文書ID
	 * @param relationType 連携区分 1:WF→文書管理 2:文書管理→WF
	 */
	public void saveMwtDocWfRelation(String corporationCode, Long processId, Long docId, String relationType) {
		repository.insertMwtDocWfRelation(corporationCode, processId, docId, relationType);
	}

	/**
	 * 文書WF連携一覧取得.
	 * @param docId 文書ID
	 * @return 文書WF連携一覧
	 */
	public List<MwtDocWfRelation> getMwtDocWfRelationList(Long docId) {
		if (docId == null) {
			return null;
		}
		return repository.getMwtDocWfRelationList(docId);
	}

	/**
	 * 文書WF連携情報一覧取得.
	 * @param list
	 * @return
	 */
	public List<DocWfRelationInfo> getDocWfInfoList(Long docId) {
		// 文書WF連携一覧を取得
		final List<MwtDocWfRelation> list = this.getMwtDocWfRelationList(docId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		// 戻り値
		final List<DocWfRelationInfo> docWfInfoList = new ArrayList<>();
		list.stream().forEach(e -> {
			final WfvActionHistory actionHistory = this.getProcessInfo(e.getCorporationCode(), e.getProcessId());
			if (actionHistory != null) {
				docWfInfoList.add( new DocWfRelationInfo(actionHistory) );
			}
		});
		return docWfInfoList;
	}

	/**
	 * WF連携で使用する画面プロセス定義取得.
	 * @param screenId 画面ID
	 * @return 画面プロセス定義
	 */
	public MwmScreenProcessDef getMwmScreenProcessDef(String corporationCode, String screenProcessCode) {
		if (isEmpty(corporationCode) || isEmpty(screenProcessCode)) {
			return null;
		}
		return repository.getMwmScreenProcessDef(corporationCode, screenProcessCode);
	}

	/**
	 * WF情報(プロセス)取得.
	 * @param corporationCode 会社コード
	 * @param processId プロセスID
	 * @return
	 */
	private WfvActionHistory getProcessInfo(final String corporationCode, final Long processId) {
		GetProcessHistoryListInParam inParam = new GetProcessHistoryListInParam();
		inParam.setProcessId(processId);
		inParam.setExecuting(true);
		inParam.setMode(GetActionHistoryListInParam.Mode.PROCESS_A_HISTORY);
		inParam.setSelectMode(GetActionHistoryListInParam.SelectMode.DATA);	// 実データのみ
		inParam.setWfUserRole(sessionHolder.getWfUserRole());
		inParam.setSortType(CommonFlag.OFF);

		// 実行
		GetProcessHistoryListOutParam outParam = wf.getProcessHistoryList(inParam);
		List<WfvActionHistory> list = outParam.getTrayList();
		return list.stream().findFirst().orElse(null);
	}
}
