package jp.co.dmm.customize.endpoint.py.py0030;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 支払一覧サービス
 */
@ApplicationScoped
public class Py0030Service extends BasePagingService {
	@Inject private Py0030Repository repository;

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
	public BaseResponse search(Py0030SearchRequest req) {
		final int allCount = repository.count(req);
		final Py0030SearchResponse res = createResponse(Py0030SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 変更申請バリデーション
	 * @param req
	 * @return
	 */
	public Py0030ValidResponse validate(Py0030ValidRequest req) {
		final Py0030ValidResponse res = createResponse(Py0030ValidResponse.class, req);
		int countPay = repository.countPay(req);
		if (countPay > 0) {
			res.success = false;
			throw new InvalidUserInputException("申請中の支払が存在する支払No[" + req.payNo + "]は変更申請できません");
		}
		res.screenProcessId = repository.getScreenProcessId(req.companyCd, req.payNo);
		res.success = true;
		return res;
	}

}
