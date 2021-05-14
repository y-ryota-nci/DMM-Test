package jp.co.nci.iwf.endpoint.mm.mm0071;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmActionInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmActionInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmActionInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmActionOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * アクション設定サービス
 */
@BizLogic
public class Mm0071Service extends BaseService {
	@Inject
	private WfmLookupService lookupService;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0071Response init(Mm0071Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");

		final Mm0071Response res = createResponse(Mm0071Response.class, req);

		if (CommonUtil.isEmpty(req.actionCode)) {
			res.action = new WfmAction();
			res.action.setCorporationCode(req.corporationCode);
			res.action.setDeleteFlag(DeleteFlag.OFF);
		} else {
			// アクション取得
			SearchWfmActionInParam saIn = new SearchWfmActionInParam();
			saIn.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
			saIn.setCorporationCode(req.corporationCode);
			saIn.setActionCode(req.actionCode);
			saIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmActionOutParam sardOut = wf.searchWfmAction(saIn);
			res.action = sardOut.getActions().get(0);
		}

		// アクション区分の選択肢
		res.actionTypeList = lookupService.getOptionItems(false, LookupTypeCode.ACTION_TYPE);
		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.action != null);
		return res;
	}

	/**
	 * アクション登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0071Response insert(Mm0071InsertRequest req) {
		final Mm0071Response res = createResponse(Mm0071Response.class, req);

		// アクション取得
		SearchWfmActionInParam saIn = new SearchWfmActionInParam();
		saIn.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
		saIn.setCorporationCode(req.action.getCorporationCode());
		saIn.setActionCode(req.action.getActionCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmActionOutParam saOut = wf.searchWfmAction(saIn);

		if (!CommonUtil.isEmpty(saOut.getActions())) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.action, req.action.getActionCode()));
			res.success = false;
			return res;
		}

		InsertWfmActionInParam insertIn = new InsertWfmActionInParam();
		insertIn.setWfmAction(req.action);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.insertWfmAction(insertIn);

		// 参加者ロール構成取得
		saIn.setCorporationCode(req.action.getCorporationCode());
		saIn.setActionCode(req.action.getActionCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		saOut = wf.searchWfmAction(saIn);
		res.action = saOut.getActions().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.action));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0071Response update(Mm0071InsertRequest req) {
		final Mm0071Response res = createResponse(Mm0071Response.class, req);

		UpdateWfmActionInParam updateIn = new UpdateWfmActionInParam();
		updateIn.setWfmAction(req.action);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.updateWfmAction(updateIn);

		// アクション取得
		SearchWfmActionInParam saIn = new SearchWfmActionInParam();
		saIn.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
		saIn.setCorporationCode(req.action.getCorporationCode());
		saIn.setActionCode(req.action.getActionCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmActionOutParam saOut = wf.searchWfmAction(saIn);

		res.action = saOut.getActions().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.action));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0071Response delete(Mm0071InsertRequest req) {
		final Mm0071Response res = createResponse(Mm0071Response.class, req);

		DeleteWfmActionInParam deleteIn = new DeleteWfmActionInParam();
		deleteIn.setWfmAction(req.action);
		deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmAction(deleteIn);

		// アクション取得
		SearchWfmActionInParam saIn = new SearchWfmActionInParam();
		saIn.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
		saIn.setCorporationCode(req.action.getCorporationCode());
		saIn.setActionCode(req.action.getActionCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmActionOutParam saOut = wf.searchWfmAction(saIn);

		res.action = saOut.getActions().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.action));
		res.success = true;
		return res;
	}

}
