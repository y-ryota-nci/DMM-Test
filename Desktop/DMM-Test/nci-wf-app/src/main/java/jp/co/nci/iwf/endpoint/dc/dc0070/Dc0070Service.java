package jp.co.nci.iwf.endpoint.dc.dc0070;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 拡張項目一覧サービス.
 */
@BizLogic
public class Dc0070Service extends BasePagingService {

	/** ルックアップサービス */
	@Inject private WfmLookupService wfmLookup;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** 拡張項目一覧リポジトリ */
	@Inject private Dc0070Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0070Response init(Dc0070Request req) {
		final Dc0070Response res = createResponse(Dc0070Response.class, req);
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		// 削除区分の選択肢
		res.deleteFlags = wfmLookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Dc0070Response search(Dc0070Request req) {
		// ロケールコードを設定
		req.localeCode = sessionHolder.getLoginInfo().getLocaleCode();

		final int allCount = repository.count(req);
		final Dc0070Response res = createResponse(Dc0070Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
