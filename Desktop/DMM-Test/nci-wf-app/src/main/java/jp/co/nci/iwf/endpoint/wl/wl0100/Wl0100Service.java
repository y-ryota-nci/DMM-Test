package jp.co.nci.iwf.endpoint.wl.wl0100;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 起案担当者選択画面サービス
 */
@BizLogic
public class Wl0100Service extends BasePagingService {
	@Inject private Wl0100Repository repository;
	@Inject private CorporationService corp;
	@Inject private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Wl0100Response init(Wl0100Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.userCodeTransfer))
			throw new BadRequestException("入力担当者ユーザコードが未指定です");
		if (isEmpty(req.corporationCodeP))
			throw new BadRequestException("プロセス定義企業コードが未指定です");
		if (isEmpty(req.processDefCode))
			throw new BadRequestException("プロセス定義コードが未指定です");
		if (isEmpty(req.processDefDetailCode))
			throw new BadRequestException("プロセス定義明細コードが未指定です");

		final Wl0100Response res = createResponse(Wl0100Response.class, req);
		copyFields(req, res);
		res.success = true;
		res.corporations = corp.getThisCorporation(false);
		res.jobTypes = lookup.getOptionItems(true, LookupTypeCode.JOB_TYPE);
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BasePagingResponse search(Wl0100Request req) {
		final int allCount = repository.count(req);
		final BasePagingResponse res = createResponse(BasePagingResponse.class, req, allCount);
		res.results = repository.select(req);
		res.success = true;
		return res;
	}
}
