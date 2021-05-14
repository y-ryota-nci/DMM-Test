package jp.co.dmm.customize.endpoint.batch.purord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 定期発注バッチサービス
 */
@ApplicationScoped
public class PurOrdService extends BaseService {

	@Inject private Step1GetOrderInfoService step1Service;
	@Inject private Step2CreateOrderRequestService step2Service;

	/**
	 * 処理開始
	 * @param req
	 * @return
	 */
	public PurOrdResponse process(PurOrdRequest req) {

		// 発注情報取得作成処理
		PurOrdResponse res = step1Service.process(req);

		// 完了以外は処理終了
		if (!"SUCCESS".equals(res.result)) {
			return res;
		}

		// 発注申請作成処理
		res = step2Service.process(req);

		return res;
	}
}
