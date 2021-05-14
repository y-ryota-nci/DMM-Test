package jp.co.nci.iwf.endpoint.cm.cm0190;

import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tray.BaseTrayService;

/**
 * 遷移先アクティビティ選択画面のサービス
 */
@BizLogic
public class Cm0190Service extends BaseTrayService {

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Cm0190Response init(Cm0190Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.processId))
			throw new BadRequestException("プロセスIDが未指定です");
		if (isEmpty(req.trayType))
			throw new BadRequestException("トレイタイプが未指定です");

		final Cm0190Response res = createResponse(Cm0190Response.class, req);
		res.results = getAccessibleActivity(req.corporationCode, req.processId, req.trayType);
		res.success = true;
		return res;
	}

}
