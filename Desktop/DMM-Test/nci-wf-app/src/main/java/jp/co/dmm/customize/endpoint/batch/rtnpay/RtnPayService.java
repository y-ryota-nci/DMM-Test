package jp.co.dmm.customize.endpoint.batch.rtnpay;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 経常支払バッチサービス
 */
@ApplicationScoped
public class RtnPayService extends BaseService {

	@Inject private Step1GetRtnpayInfoService step1Service;
	@Inject private Step2CreateOrderService step2Service;
	@Inject private Step3CreateAcceptanceService step3Service;
	@Inject private Step4CreatePaymentService step4Service;

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public RtnPayResponse process(RtnPayRequest req) {

		// 支払情報取得作成処理
		RtnPayResponse res = step1Service.process(req);

		// 完了以外は処理終了
		if (!"SUCCESS".equals(res.result)) {
			return res;
		}

		// 発注マスタ作成処理
		res = step2Service.process(req);

		// 完了以外は処理終了
		if (!"SUCCESS".equals(res.result)) {
			return res;
		}

		// 検収マスタ作成処理
		res = step3Service.process(req);

		// 完了以外は処理終了
		if (!"SUCCESS".equals(res.result)) {
			return res;
		}

		// 支払申請作成処理
		res = step4Service.process(req);

		return res;
	}
}
