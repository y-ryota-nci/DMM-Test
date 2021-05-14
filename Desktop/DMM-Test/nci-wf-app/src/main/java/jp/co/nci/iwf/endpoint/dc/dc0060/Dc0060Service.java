package jp.co.nci.iwf.endpoint.dc.dc0060;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 拡張項目一覧サービス.
 */
@BizLogic
public class Dc0060Service extends BasePagingService {

	/** ルックアップサービス */
	@Inject private MwmLookupService lookup;
	/** ルックアップサービス */
	@Inject private WfmLookupService wfmLookup;
	/** 企業サービス */
	@Inject private CorporationService corp;
	/** 拡張項目一覧リポジトリ */
	@Inject private Dc0060Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Dc0060Response init(Dc0060Request req) {
		final Dc0060Response res = createResponse(Dc0060Response.class, req);
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		// メタ入力タイプの選択肢
		res.inputTypes = lookup.getOptionItems(LookupGroupId.META_INPUT_TYPE, true);
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
	public Dc0060Response search(Dc0060Request req) {
		final int allCount = repository.count(req);
		final Dc0060Response res = createResponse(Dc0060Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
