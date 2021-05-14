package jp.co.nci.iwf.endpoint.vd.vd0180;

import java.util.Set;
import java.util.stream.Collectors;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * パーツコード変更画面サービス
 */
@BizLogic
public class Vd0180Service extends BaseService {

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(BaseRequest req) {
		BaseResponse res = createResponse(BaseResponse.class, req);
		res.success = true;
		return res;
	}

	/**
	 * パーツコード変更
	 * @param req
	 * @return
	 */
	public BaseResponse change(Vd0180Request req) {
		// ここで行うのは、変更可能かどうかの判断のみ。
		final BaseResponse res = createResponse(BaseResponse.class, req);
		final Set<String> partsCodes = req.ctx.designMap.values().stream()
				.map(d -> d.partsCode)
				.collect(Collectors.toSet());
		if (partsCodes.contains(req.newPartsCode)) {
			res.addAlerts(i18n.getText(MessageCd.MSG0108, MessageCd.partsCode, req.newPartsCode));
			res.success = false;
		}
		else {
			res.success = true;
		}
		return res;
	}

}
