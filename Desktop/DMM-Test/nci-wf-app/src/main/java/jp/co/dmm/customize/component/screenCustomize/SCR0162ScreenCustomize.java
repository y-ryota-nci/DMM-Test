package jp.co.dmm.customize.component.screenCustomize;

import javax.inject.Named;

import jp.co.dmm.customize.component.DmmCodeBook.PurordTp;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;

/**
 * 変更_発注申請（集中購買）画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0162ScreenCustomize extends SCR0058ScreenCustomize {
	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		// 変更＿発注申請の初期化を流用
		super.afterInitLoad(req, res, ctx);

		if (in(req.trayType, TrayType.NEW, TrayType.WORKLIST)) {
			if (req.processId == null) {
				// 発注区分→集中購買
				ctx.runtimeMap.get("TXT0088").setValue(PurordTp.FOCUS);
			}
		}
	}
}
