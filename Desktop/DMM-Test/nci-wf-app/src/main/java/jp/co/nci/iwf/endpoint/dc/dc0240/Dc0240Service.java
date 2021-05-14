package jp.co.nci.iwf.endpoint.dc.dc0240;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書トレイ一覧(管理者用)サービス
 */
@BizLogic
public class Dc0240Service extends BasePagingService {
	@Inject private MwmLookupService mwmLookup;
	@Inject private Dc0240Repository repository;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final Dc0240Response res = createResponse(Dc0240Response.class, req);
		res.systemFlags = mwmLookup.getOptionItems(LookupGroupId.SYSTEM_FLAG, true);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Dc0240Response search(Dc0240Request req) {
		final int allCount = repository.count(req);
		final Dc0240Response res = createResponse(Dc0240Response.class, req, allCount);
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
	public BaseResponse delete(Dc0240Request req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		if (req.trayConfigIds == null || req.trayConfigIds.isEmpty()) {
			res.addAlerts(i18n.getText(MessageCd.MSG0135));
			res.success = false;
		}
		else {
			for (Long trayConfigId : req.trayConfigIds) {
				repository.delete(trayConfigId);
			}
			res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.trayConfig));
			res.success = true;
		}
		return res;
	}

}
