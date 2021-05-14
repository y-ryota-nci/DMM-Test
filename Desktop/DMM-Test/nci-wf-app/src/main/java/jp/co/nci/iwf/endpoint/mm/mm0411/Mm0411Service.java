package jp.co.nci.iwf.endpoint.mm.mm0411;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmCorporationInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmCorporationInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmCorporationInParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.CommonUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmCorporationOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * アクション設定サービス
 */
@BizLogic
public class Mm0411Service extends BaseService {
	@Inject
	private WfmLookupService lookupService;

	@Inject
	protected WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0411Response init(Mm0411Request req) {
		final Mm0411Response res = createResponse(Mm0411Response.class, req);

		if (CommonUtil.isEmpty(req.corporationCode)) {
			res.corporation = new WfmCorporation();
			res.corporation.setCorporationCode(req.corporationCode);
			res.corporation.setDeleteFlag(DeleteFlag.OFF);
		} else {
			// アクション取得
			SearchWfmCorporationInParam saIn = new SearchWfmCorporationInParam();
			saIn.setCorporationCode(req.corporationCode);
			saIn.setCorporationCode(req.corporationCode);
			saIn.setWfUserRole(sessionHolder.getWfUserRole());
			SearchWfmCorporationOutParam sardOut = wf.searchWfmCorporation(saIn);
			res.corporation = sardOut.getCorporations().get(0);
		}

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.success = (res.corporation != null);
		return res;
	}

	/**
	 * アクション登録
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0411Response insert(Mm0411InsertRequest req) {
		final Mm0411Response res = createResponse(Mm0411Response.class, req);

		// アクション取得
		SearchWfmCorporationInParam saIn = new SearchWfmCorporationInParam();
		saIn.setCorporationCode(req.corporation.getCorporationCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmCorporationOutParam saOut = wf.searchWfmCorporation(saIn);

		if (!CommonUtil.isEmpty(saOut.getCorporations())) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.corporation, req.corporation.getCorporationCode()));
			res.success = false;
			return res;
		}

		InsertWfmCorporationInParam insertIn = new InsertWfmCorporationInParam();
		insertIn.setWfmCorporation(req.corporation);
		insertIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.insertWfmCorporation(insertIn);

		// 会社マスタ
		saIn.setCorporationCode(req.corporation.getCorporationCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		saOut = wf.searchWfmCorporation(saIn);
		res.corporation = saOut.getCorporations().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0066, MessageCd.corporation));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0411Response update(Mm0411InsertRequest req) {
		final Mm0411Response res = createResponse(Mm0411Response.class, req);

		UpdateWfmCorporationInParam updateIn = new UpdateWfmCorporationInParam();
		updateIn.setWfmCorporation(req.corporation);
		updateIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.updateWfmCorporation(updateIn);

		// アクション取得
		SearchWfmCorporationInParam saIn = new SearchWfmCorporationInParam();
		saIn.setCorporationCode(req.corporation.getCorporationCode());
		saIn.setCorporationCode(req.corporation.getCorporationCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmCorporationOutParam saOut = wf.searchWfmCorporation(saIn);

		res.corporation = saOut.getCorporations().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.corporation));
		res.success = true;
		return res;
	}

	@Transactional
	public Mm0411Response delete(Mm0411InsertRequest req) {
		final Mm0411Response res = createResponse(Mm0411Response.class, req);

		DeleteWfmCorporationInParam deleteIn = new DeleteWfmCorporationInParam();
		deleteIn.setWfmCorporation(req.corporation);
		deleteIn.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmCorporation(deleteIn);

		// アクション取得
		SearchWfmCorporationInParam saIn = new SearchWfmCorporationInParam();
		saIn.setCorporationCode(req.corporation.getCorporationCode());
		saIn.setCorporationCode(req.corporation.getCorporationCode());
		saIn.setWfUserRole(sessionHolder.getWfUserRole());
		SearchWfmCorporationOutParam saOut = wf.searchWfmCorporation(saIn);

		res.corporation = saOut.getCorporations().get(0);

		// 削除区分の選択肢
		res.deleteFlagList = lookupService.getOptionItems(false, LookupTypeCode.DELETE_FLAG);

		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.corporation));
		res.success = true;
		return res;
	}

}
