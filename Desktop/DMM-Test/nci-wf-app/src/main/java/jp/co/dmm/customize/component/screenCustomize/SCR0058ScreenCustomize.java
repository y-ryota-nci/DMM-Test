package jp.co.dmm.customize.component.screenCustomize;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.po.PurordInfService;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.SessionHolder;

/**
 * 変更発注申請画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0058ScreenCustomize extends SCR0038ScreenCustomize {

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** DMM発注情報用サービス */
	@Inject private PurordInfService purordInfService;
	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		super.afterInitLoad(req, res, ctx);

		if (in(req.trayType, TrayType.NEW, TrayType.WORKLIST)) {
			// パラメータ
			String companyCd = req.param1;
			String purordNo = req.param2;
			if (isNotEmpty(companyCd) && isNotEmpty(purordNo)) {
				// 発注情報をユーザデータとして読み込み
				final Map<String, List<UserDataEntity>> tables = purordInfService.getUserDataMap(companyCd, purordNo);
				// ユーザデータからランタイムMapを生成
				loader.fillUserData(ctx, tables, false);

				// コピーせず、変更＿発注申請で固有のフィールドを設定
				{
					setPartsValue(ctx, "TXT0158", "CHANGE");		// 新規／変更区分→変更＿発注申請なのでCHANGEに書き換え
					// 新規＿発注申請と変更＿発注申請で同一組織コードとは限らないが、組織改変で
					// 新規＿発注申請で設定されていた組織コードが無くなる可能性がある。
					// そして存在しない組織に対しては仕訳が起こせないので、新規＿発注申請から組織コードは引き継がず、
					// 変更＿発注申請を起票した人の組織コードを使用する
					LoginInfo login = sessionHolder.getLoginInfo();
					ctx.runtimeMap.get("TXT0091").setValue(login.getCorporationCode());
					ctx.runtimeMap.get("TXT0068").setValue(login.getUserCode());
					ctx.runtimeMap.get("TXT0072").setValue(login.getUserName());
					ctx.runtimeMap.get("TXT0069").setValue(login.getOrganizationCode());
					ctx.runtimeMap.get("TXT0073").setValue(login.getOrganizationName());
					ctx.runtimeMap.get("TXT0071").setValue(login.getExtendedInfo01()); // 所在地コード
					ctx.runtimeMap.get("TXT0087").setValue(login.getSbmtrAddr());
					ctx.runtimeMap.get("TXT0160").setValue(login.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
					// 明細の申請者情報
					PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0074");
					for (PartsContainerRow row : grid.rows) {
						String prefix = grid.htmlId + "-" + row.rowId + "_";
						ctx.runtimeMap.get(prefix + "TXT0091").setValue(login.getCorporationCode());
						ctx.runtimeMap.get(prefix + "TXT0048").setValue(login.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
					}
				}
			}
			// 新規＿発注申請の初期化を流用
			super.afterInitLoad(req, res, ctx);
		}
	}
}