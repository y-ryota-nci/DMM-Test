package jp.co.dmm.customize.component.screenCustomize;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.ri.RcvinspInfService;
import jp.co.dmm.customize.endpoint.vd.vd0310.DmmCustomService;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.jersey.SessionHolder;

/**
 * 変更＿検収申請画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0112ScreenCustomize extends SCR0049ScreenCustomize {
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** DMM検収情報用サービス */
	@Inject private RcvinspInfService rcvinspInfService;
	/** セッションホルダ */
	@Inject private SessionHolder sessionHolder;
	@Inject private DmmCustomService dmmCustomService;

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		if (in(req.trayType, TrayType.NEW, TrayType.WORKLIST)) {
			// パラメータ
			String companyCd = req.param1;
			String rcvinspNo = req.param2;
			if (isNotEmpty(companyCd) && isNotEmpty(rcvinspNo)) {
				// 検収情報をユーザデータとして読み込み
				final Map<String, List<UserDataEntity>> tables = rcvinspInfService.getUserDataMap(companyCd, rcvinspNo);
				// ユーザデータからランタイムMapを生成
				loader.fillUserData(ctx, tables, false);

				// コピーせず、変更＿検収申請で固有のフィールドを設定
				{
					setPartsValue(ctx, "TXT0134", "CHANGE");		// 新規／変更区分→変更＿発注申請なのでCHANGEに書き換え
					// 新規＿検収申請と変更＿検収申請で同一組織コードとは限らないが、組織改変で
					// 新規＿検収申請で設定されていた組織コードが無くなる可能性がある。
					// そして存在しない組織に対しては仕訳が起こせないので、新規＿検収申請から組織コードは引き継がず、
					// 変更＿検収申請を起票した人の組織コードを使用する
					LoginInfo login = sessionHolder.getLoginInfo();
					ctx.runtimeMap.get("TXT0068").setValue(login.getCorporationCode());
					ctx.runtimeMap.get("TXT0053").setValue(login.getUserCode());
					ctx.runtimeMap.get("TXT0054").setValue(login.getUserName());
					ctx.runtimeMap.get("TXT0055").setValue(login.getOrganizationCode());
					ctx.runtimeMap.get("TXT0058").setValue(login.getOrganizationName());
					ctx.runtimeMap.get("TXT0057").setValue(login.getExtendedInfo01()); // 所在地コード
					ctx.runtimeMap.get("TXT0066").setValue(login.getSbmtrAddr());
					ctx.runtimeMap.get("TXT0079").setValue(login.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
					// 明細の申請者情報
					PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0059");
					for (PartsContainerRow row : grid.rows) {
						String prefix = grid.htmlId + "-" + row.rowId + "_";
						ctx.runtimeMap.get(prefix + "TXT0070").setValue(login.getCorporationCode());
						ctx.runtimeMap.get(prefix + "TXT0062").setValue(login.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
					}

					//	前払い情報を含む検収の変更申請を可能に
					// 社内レート基準日：費用計上月の末日（費用計上月が未設定ならシステム日付の末日）
					final PartsBase<?> $cstAddYm = ctx.runtimeMap.get("TXT0131");	// 検収予定日
					final PartsBase<?> $inRtoBaseDt = ctx.runtimeMap.get("TXT0135");
					if (isAdvpaySetting(ctx.runtimeMap)) {
						final Date cstAddDt = isEmpty($cstAddYm.getValue()) ? today() : toDate($cstAddYm.getValue() + "/01", "yyyy/MM/dd");
						final Date endOfLastM = dmmCustomService.getRtoBaseDt(cstAddDt);
						$inRtoBaseDt.setValue(toStr(endOfLastM, "yyyy/MM/dd"));
					}
				}
			}
		}
		// 新規＿検収申請の初期化を流用
		super.afterInitLoad(req, res, ctx);
	}
}
