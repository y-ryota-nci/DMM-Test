package jp.co.dmm.customize.endpoint.po.po0010;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.dmm.customize.component.DmmCodeBook.PurordTp;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 発注一覧サービス
 */
@ApplicationScoped
public class Po0010Service extends BasePagingService {
	@Inject private Po0010Repository repository;


	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		final BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public BaseResponse search(Po0010SearchRequest req) {
		final int allCount = repository.count(req);
		final Po0010SearchResponse res = createResponse(Po0010SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 変更申請バリデーション
	 * @param req
	 * @return
	 */
	public Po0010ValidResponse validate(Po0010ValidRequest req) {
		final Po0010ValidResponse res = createResponse(Po0010ValidResponse.class, req);
		int countRcvinsp = repository.countRcvinsp(req);
		if (countRcvinsp > 0) {
			res.success = false;
			throw new InvalidUserInputException("発注No[" + req.purordNo + "]は検収申請中のため、変更申請できません");
		}

		int countPurord = repository.countPurord(req);
		if (countPurord > 0) {
			res.success = false;
			throw new InvalidUserInputException("発注No[" + req.purordNo + "]はまだ申請中です。");
		}

		final String companyCd = req.companyCd;
		final String screenProcessCode = toScreenProcessCode(req);
		res.screenProcessId = repository.getScreenProcessId(companyCd, screenProcessCode);
		res.success = true;
		return res;
	}

	private String toScreenProcessCode(Po0010ValidRequest req) {
		final String purordTp = req.purordTp;	// 発注区分
		if (isEmpty(purordTp))
			throw new BadRequestException("発注区分が未指定です");

//		final String prdPurordTp = req.prdPurordTp;	// 定期発注区分
//		if (isEmpty(prdPurordTp))
//			throw new BadRequestException("定期発注区分が未指定です");

//		// 変更＿発注申請（定期）
//		if (in(prdPurordTp, PrdPurordTp.ROUTINE_ADS, PrdPurordTp.ROUTINE_OTHER))
//			return "";
		// 変更＿発注申請（集中購買）
		if (eq(PurordTp.FOCUS, purordTp))
			return "0000000151";
		// 変更＿発注申請
		return "0000000071";
	}

	/**
	 * 完了
	 * @param req
	 * @return
	 */
	public BaseResponse complete(Po0010SaveRequest req) {
		final String corporationCode = sessionHolder.getWfUserRole().getCorporationCode();
		final String userCode = sessionHolder.getWfUserRole().getUserCode();
		final String ipAddr = sessionHolder.getWfUserRole().getIpAddress();
		int count = 0;
		for (String purordNo : req.purordNoList) {
			count += repository.updateSts(purordNo, corporationCode, userCode, ipAddr);
		}
		final Po0010SaveResponse res = createResponse(Po0010SaveResponse.class, req);
		res.addSuccesses(i18n.getText(MessageCd.MSG0067, count + " " + i18n.getText(MessageCd.records)));
		res.success = true;
		return res;
	}

	/** 支払業務マスタを抽出 */
	public PayApplMst getPayApplMst(String companyCd, String payApplCd) {
		return repository.getPayApplMst(companyCd, payApplCd);
	}
}
