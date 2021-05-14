
package jp.co.dmm.customize.endpoint.ri.ri0030;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.po.po0010.Po0010Repository;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 検収一覧サービス
 */
@ApplicationScoped
public class Ri0030Service extends BasePagingService {
	@Inject private Ri0030Repository repository;
	@Inject private Po0010Repository po0010;


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
	public BaseResponse search(Ri0030SearchRequest req) {
		final int allCount = repository.count(req);
		final Ri0030SearchResponse res = createResponse(Ri0030SearchResponse.class, req, allCount);
		res.results = repository.select(req, res);
		res.success = true;
		return res;
	}

	/**
	 * 検収＿変更申請用バリデーション
	 * @param req
	 * @return
	 */
	public Ri0030ValidateResponse validate(Ri0030ValidateRequest req) {
		final Ri0030ValidateResponse res = createResponse(Ri0030ValidateResponse.class, req);

		// 支払に回っていたら不可
		int countPay = repository.countPay(req);
		if (countPay > 0) {
			throw new InvalidUserInputException("検収No[" + req.rcvinspNo + "]は支払申請中のため、変更申請できません");
		}
		// 申請中の検収＿変更申請ありは不可
		int countPurord = repository.countChangeRcvinsp(req);
		if (countPurord > 0) {
			throw new InvalidUserInputException("検収No[" + req.rcvinspNo + "]はまだ申請中です。");
		}
		// 前払Noが含まれていれば不可
//		int countPrePay = repository.countPrePay(req);
//		if (countPrePay > 0) {
//			throw new InvalidUserInputException("検収No[" + req.rcvinspNo + "]には前払Noが含まれているため、変更申請できません");
//		}
		// 変更＿検収申請の画面プロセスIDを求める
		res.screenProcessId = po0010.getScreenProcessId(req.companyCd, "0000000112");
		res.success = true;
		return res;
	}

}
