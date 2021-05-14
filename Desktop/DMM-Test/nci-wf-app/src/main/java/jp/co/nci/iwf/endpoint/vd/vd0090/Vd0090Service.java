package jp.co.nci.iwf.endpoint.vd.vd0090;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 新規申請メニュー割当一覧サービス
 */
@BizLogic
public class Vd0090Service extends BasePagingService {
	@Inject private Vd0090Repository repository;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0090InitResponse init(BaseRequest req) {
		Vd0090InitResponse res = createResponse(Vd0090InitResponse.class, req);
		res.success = true;
		res.processDefs = getProcessDefOptions(sessionHolder.getLoginInfo().getCorporationCode());

		return res;
	}

	/** プロセス定義の選択肢 */
	protected List<OptionItem> getProcessDefOptions(String corporationCode) {
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[]{
				new OrderBy(true, "PD." + WfmProcessDef.CORPORATION_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_DETAIL_CODE),
		});
		in.setWfUserRole(sessionHolder.getWfUserRole());
		// 仮に有効期限が切れたとしても、過去案件は検索対象に含まれなければならない。
		// よってプロセス定義に対して有効期限で絞り込むのは誤りである。

		final List<OptionItem> items = new ArrayList<>();
		items.add(OptionItem.EMPTY);

		// 枝番があるのでプロセス定義コードは重複する可能性があるので、ユニーク化する
		final Set<String> uniques = new HashSet<>();
		wf.searchWfmProcessDef(in).getProcessDefs().forEach(e -> {
			if (!uniques.contains(e.getProcessDefCode())) {
				items.add(new OptionItem(e.getProcessDefCode(), e.getProcessDefName()));
				uniques.add(e.getProcessDefCode());
			}
		});
		return items;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Vd0090Response search(Vd0090Request req) {
		final int allCount = repository.count(req);
		final Vd0090Response res = createResponse(Vd0090Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse delete(Vd0090Request req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		if (req.screenProcessMenuIds == null || req.screenProcessMenuIds.isEmpty()) {
			res.addAlerts(i18n.getText(MessageCd.MSG0135, 1));
			res.success = false;
		} else {
			for (Long screenProcessMenuId : req.screenProcessMenuIds) {
				repository.delete(screenProcessMenuId);
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.screenProcessMenuInfo));
			res.success = true;
		}
		return res;
	}
}
