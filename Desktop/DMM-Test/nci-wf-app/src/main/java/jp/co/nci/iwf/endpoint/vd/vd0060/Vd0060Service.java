package jp.co.nci.iwf.endpoint.vd.vd0060;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 選択肢一覧のサービス
 */
@BizLogic
public class Vd0060Service extends BasePagingService {

	@Inject private Vd0060Repository repository;
	/** 企業サービス */
	@Inject private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0060InitResponse init(BaseRequest req) {
		final Vd0060InitResponse res = createResponse(Vd0060InitResponse.class, req);
		// 企業の選択肢
		res.corporations = corp.getMyCorporations(false);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Vd0060SearchResponse search(Vd0060SearchRequest req) {
		final int allCount = repository.count(req);
		final Vd0060SearchResponse res = createResponse(Vd0060SearchResponse.class, req, allCount);
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
	public BaseResponse delete(Vd0060DeleteRequest req) {
		for (Long optionId : req.optionIds) {
			repository.delete(optionId);
		}
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0064, MessageCd.optionSetting));
		res.success = true;
		return res;
	}
}
