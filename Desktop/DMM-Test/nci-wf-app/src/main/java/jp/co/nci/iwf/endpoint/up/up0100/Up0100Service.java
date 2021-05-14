package jp.co.nci.iwf.endpoint.up.up0100;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アップロード履歴画面Service
 */
@BizLogic
public class Up0100Service extends BasePagingService {
	@Inject private Up0100Repository repository;
	@Inject private MwmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Up0100Response init(BaseRequest req) {
		final Up0100Response res = createResponse(Up0100Response.class, req);
		res.accessibleScreenIds = sessionHolder.getLoginInfo().getAccessibleScreenIds();
		res.uploadKinds = lookup.getOptionItems(LookupGroupId.UPLOAD_KIND, true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Up0100Response search(Up0100Request req) {
		final int allCount = repository.count(req);
		final Up0100Response res = createResponse(Up0100Response.class, req, allCount);
		res.results = repository.select(req, allCount);
		res.success = true;
		return res;
	}

	/**
	 * アップロードファイル登録情報を取得
	 * @param req
	 * @return
	 */
	public Up0100Response getHistory(Up0100Request req) {
		final Up0100Response res = createResponse(Up0100Response.class, req);
		res.results = repository.getHistory(req.uploadFileId);
		res.success = true;
		return res;
	}
}
