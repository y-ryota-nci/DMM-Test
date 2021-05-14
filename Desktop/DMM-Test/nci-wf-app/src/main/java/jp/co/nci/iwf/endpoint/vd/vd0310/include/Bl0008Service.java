package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmInformationSharerDefOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ブロック：デフォルト閲覧者（デフォルト参照者情報）のサービス
 */
@BizLogic
public class Bl0008Service extends BaseService implements CodeMaster {

	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;

	public List<WfmInformationSharerDef> getInformationSharerDefList(String corporationCode, String processDefCode, String processDefDetailCode, Long processId) {

		SearchWfmInformationSharerDefInParam in = new SearchWfmInformationSharerDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setProcessId(processId);

		in.setValidStartDate(now());
		in.setValidEndDate(now());
		in.setDisplayFlag(DisplayFlag.ON);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "S.ASSIGN_ROLE_CODE")});

		SearchWfmInformationSharerDefOutParam out = wf.searchWfmInformationSharerDef(in);
		return out.getInformationSharerDefs().stream().filter(isd -> isEmpty(isd.getExpressionDefCode())).collect(Collectors.toList());
	}

}
