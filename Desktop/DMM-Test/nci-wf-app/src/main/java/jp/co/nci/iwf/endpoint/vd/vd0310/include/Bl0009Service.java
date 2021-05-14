package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.RemoveInformationSharerInParam;
import jp.co.nci.integrated_workflow.api.param.input.SetInformationSharerInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.impl.WftInformationSharerImpl;
import jp.co.nci.integrated_workflow.model.custom.WftInformationSharer;
import jp.co.nci.integrated_workflow.param.input.SearchWftInformationSharerInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWftInformationSharerOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ブロック：参照者情報のサービス
 */
@BizLogic
public class Bl0009Service extends BaseService implements CodeMaster {

	/** WF API */
	@Inject
	protected WfInstanceWrapper wf;

	public List<WftInformationSharer> getInformationSharerList(String corporationCode, Long processId) {

		SearchWftInformationSharerInParam in = new SearchWftInformationSharerInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessId(processId);
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "S.INFORMATION_SHARER_ID")});

		SearchWftInformationSharerOutParam out = wf.searchWftInformationSharer(in);
		return out.getInformationSharers();
	}

	public void execute(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		create(req);
		remove(req);
	}

	private void create(Vd0310ExecuteRequest req) {
		if (isEmpty(req.additionInformationSharerList)) {
			return;
		}
		SetInformationSharerInParam in = new SetInformationSharerInParam();
		in.setWftInformationSharerList(
				req.additionInformationSharerList
				.stream()
				.filter(is -> isEmpty(is.getInformationSharerId()))
				.map(is -> {
					jp.co.nci.integrated_workflow.model.base.WftInformationSharer e = new WftInformationSharerImpl();
					copyProperties(is, e);
					e.setCorporationCode(req.contents.corporationCode);
					e.setProcessId(req.contents.processId);
					e.setInformationSharerType(InformationSharerType.ALL);
					return e;
				})
				.collect(Collectors.toList())
		);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.setInformationSharer(in);
	}

	private void remove(Vd0310ExecuteRequest req) {
		if (isEmpty(req.removeInformationSharerList)) {
			return;
		}

		RemoveInformationSharerInParam in = new RemoveInformationSharerInParam();
		in.setWftInformationSharerList(
				req.removeInformationSharerList
				.stream()
				.filter(is -> isNotEmpty(is.getInformationSharerId()))
				.map(is -> {
					jp.co.nci.integrated_workflow.model.base.WftInformationSharer e = new WftInformationSharerImpl();
					copyProperties(is, e);
					return e;
				})
				.collect(Collectors.toList())
		);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.removeInformationSharer(in);
	}

}
