package jp.co.nci.iwf.endpoint.au.au0010;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.mm.MmBaseService;
import jp.co.nci.iwf.jpa.entity.mw.WfvUserPassword;

/**
 * ルックアップグループ一覧のサービス
 */
@BizLogic
public class Au0010Service extends MmBaseService<WfvUserPassword> {
	@Inject
	private Au0010Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Au0010Response init(Au0010Request req) {
		final Au0010Response res = createResponse(Au0010Response.class, req);
		res.corporations = getAccessibleCorporations(false);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Au0010Response search(Au0010Request req) {
		final int allCount = repository.count(req);
		final Au0010Response res = createResponse(Au0010Response.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}
}
