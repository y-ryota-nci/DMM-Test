package jp.co.dmm.customize.endpoint.co.co0020;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * 経常支払マスタ一覧サービス
 */
@ApplicationScoped
public class Co0020Service extends BasePagingService {

	@Inject private Co0020Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Co0020SearchResponse init(Co0020SearchRequest req) {
		final Co0020SearchResponse res = createResponse(Co0020SearchResponse.class, req);
		res.companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		// "RM0011"(支払予約編集ロール)を持っていたら編集権限ありとする
		res.editableFlg = sessionHolder.getLoginInfo().getMenuRoleCodes().contains("RM0011") ? CommonFlag.ON : CommonFlag.OFF;
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Co0020SearchResponse search(Co0020SearchRequest req) {

		req.companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		final int allCount = repository.count(req);
		final Co0020SearchResponse res = createResponse(Co0020SearchResponse.class, req, allCount);

		res.results = repository.select(req, res);
		res.companyCd = sessionHolder.getWfUserRole().getCorporationCode();
		res.success = true;
		return res;
	}
}
