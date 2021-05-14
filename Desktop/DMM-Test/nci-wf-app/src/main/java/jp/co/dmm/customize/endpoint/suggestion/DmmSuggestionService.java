package jp.co.dmm.customize.endpoint.suggestion;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.endpoint.suggestion.bumon.DmmSuggestionBumonRepository;
import jp.co.dmm.customize.endpoint.suggestion.bumon.DmmSuggestionBumonRequest;
import jp.co.dmm.customize.endpoint.suggestion.prdPurord.PrdPurordRequest;
import jp.co.dmm.customize.endpoint.suggestion.prdPurord.PrdPurordResponse;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.sandbox.SandboxSuggestionResponse;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * DMM_Suggestionサービス
 */
@BizLogic
public class DmmSuggestionService extends BasePagingService {
	@Inject private DmmSuggestionBumonRepository bmnRepository;
	@Inject private DmmSuggestionRepository repository;

	/**
	 * 部門検索
	 * @param req
	 * @return
	 */
	public SandboxSuggestionResponse suggestBumon(DmmSuggestionBumonRequest req) {
		final int allCount = bmnRepository.count(req);
		final SandboxSuggestionResponse res = createResponse(
				SandboxSuggestionResponse.class, req, allCount);
		res.results = bmnRepository.select(req, res);
		res.success = true;
		return res;
	}

	/** 定期発注予定マスタの抽出 */
	public BaseResponse getPrdPurord(PrdPurordRequest req) {
		if (isEmpty(req.companyCd))
			throw new BadRequestException("会社CDが未指定です");

		final PrdPurordResponse res = createResponse(PrdPurordResponse.class, req);
		if (req.prdPurordNo == null) {
			// 新規発注なら自動起票済み定期発注予定マスタはないのが自明である
			res.results = new ArrayList<>();
		}
		else {
			// 直近の自動起票済み定期支払日
			res.results = repository.getPrdPurordPlnMst(req.companyCd, req.prdPurordNo);
			if (!res.results.isEmpty()) {
				res.lastPrdPayDt = res.results.get(res.results.size() - 1).getPrdPayDt();
			}
		}
		res.success = true;
		return res;
	}
}
