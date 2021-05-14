package jp.co.nci.iwf.endpoint.mm.mm0001;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmCorporationInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmCorporationInParam;
import jp.co.nci.integrated_workflow.api.param.output.DeleteWfmCorporationOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmCorporationOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 企業編集画面サービス
 */
@BizLogic
public class Mm0001Service extends BaseService {
	@Inject
	private WfInstanceWrapper wf;
	@Inject private CorporationService corpService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0001InitResponse init(Mm0001InitRequest req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.timestampUpdated == null)
			throw new BadRequestException("最終更新日時が未指定です");

		final Mm0001InitResponse res = createResponse(Mm0001InitResponse.class, req);
		res.corp = getCorporation(req.corporationCode);
		res.corporationGroupCodes = corpService.getCorporationGroup(true);

		// 排他チェック
		if (!eq(req.timestampUpdated, res.corp.getTimestampUpdated().getTime()))
			throw new AlreadyUpdatedException();

		res.success = (res.corp != null);
		return res;
	}

	private WfmCorporation getCorporation(String corporationCode) {
		SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(corporationCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);
		List<WfmCorporation> list = wf.searchWfmCorporation(in).getCorporations();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 企業の更新
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0001UpdateResponse update(Mm0001UpdateRequest req) {
		final WfmCorporation inputed = req.corp;
		if (isEmpty(inputed.getCorporationCode()))
			throw new BadRequestException("企業コードが未指定です");

		final WfmCorporation corp = getCorporation(inputed.getCorporationCode());
		corp.setAddress(inputed.getAddress());
		corp.setCorporationAddedInfo(inputed.getCorporationAddedInfo());
		corp.setCorporationGroupCode(inputed.getCorporationGroupCode());
		corp.setCorporationName(inputed.getCorporationName());
		corp.setTelNum(inputed.getTelNum());
		corp.setPostNum(inputed.getPostNum());
		corp.setAddress(inputed.getAddress());
		corp.setDeleteFlag(DeleteFlag.OFF);
		corp.setExtendedInfo01(inputed.getExtendedInfo01());

		final UpdateWfmCorporationInParam in = new UpdateWfmCorporationInParam();
		in.setWfmCorporation(corp);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		final UpdateWfmCorporationOutParam out = wf.updateWfmCorporation(in);
		if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
			throw new WfException(out);

		final Mm0001UpdateResponse res = createResponse(Mm0001UpdateResponse.class, req);
		res.corp = getCorporation(req.corp.getCorporationCode());
		res.success = true;
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, i18n.getText(MessageCd.corporation)));

		return res;
	}

	/**
	 * 削除
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0001UpdateResponse delete(Mm0001UpdateRequest req) {
		final Mm0001UpdateResponse res = createResponse(Mm0001UpdateResponse.class, req);
		if (isEmpty(req.corp.getCorporationCode()))
			throw new BadRequestException("企業コードまたは組織コードが未指定です");
		if (CorporationCodes.ASP.equals(req.corp.getCorporationCode())) {
			// ASP企業を削除はできない
			res.addAlerts(i18n.getText(MessageCd.MSG0110, MessageCd.aspCorporation));
		}
		else {
			final DeleteWfmCorporationInParam in = new DeleteWfmCorporationInParam();
			in.setWfmCorporation(req.corp);
			in.setWfUserRole(sessionHolder.getWfUserRole());

			final DeleteWfmCorporationOutParam out = wf.deleteWfmCorporation(in);
			if (!ReturnCode.SUCCESS.equals(out.getReturnCode()))
				throw new WfException(out);

			res.corp = getCorporation(req.corp.getCorporationCode());
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, i18n.getText(MessageCd.corporation)));
		}
		res.success = true;
		return res;
	}

}
