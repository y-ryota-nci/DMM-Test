package jp.co.nci.iwf.endpoint.al.al0010;

import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アクセスログ検索サービス
 */
@BizLogic
public class Al0010Service extends BasePagingService {
	@Inject
	private Al0010Repository repository;
	@Inject
	private MwmLookupService lookup;
	@Inject
	private CorporationService corp;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Al0010InitResponse init(BaseRequest req) {
		final Al0010InitResponse res = createResponse(Al0010InitResponse.class, req);

		// アクセスログ結果種別の選択肢
		res.accessLogResultTypes = lookup.getOptionItems(LookupGroupId.ACCESS_LOG_RESULT_TYPE, true);

		// 企業の選択肢
		res.corporations = corp.getMyCorporations(true);

		// 現時刻より10分前
		res.today = today();
		res.now = new SimpleDateFormat("HH:mm").format(addMinutes(now(), -10));

		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Al0010SearchResponse search(Al0010SearchRequest req) {
		if (isEmpty(req.accessDate))
			throw new BadRequestException("日付が未指定です");
		if (isEmpty(req.accessTmFrom))
			throw new BadRequestException("時刻が未指定です");

		final int allCount = repository.count(req);
		final Al0010SearchResponse res = createResponse(Al0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

}
