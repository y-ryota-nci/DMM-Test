 package jp.co.dmm.customize.component.screenCustomize;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.endpoint.co.CntrctInfService;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignEventButton;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;

/**
 * 変更契約申請画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0059ScreenCustomize extends SCR0056ScreenCustomize {

	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;
	/** 契約情報サービス */
	@Inject private CntrctInfService cntrctInfService;

	/**
	 * 初期表示前処理
	 */
	@Override
	public void beforeInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		// パラメータ
		String companyCd = req.param1;
		String cntrctNo = req.param2;
		String rtnPayNo = req.param3;

		if (StringUtils.isNotEmpty(companyCd) && StringUtils.isNotEmpty(cntrctNo)) {

			// 契約情報をユーザデータとして読み込み
			final Map<String, List<UserDataEntity>> tables = cntrctInfService.getUserDataMap(companyCd, cntrctNo, rtnPayNo, false);

			// ユーザデータからランタイムMapを生成
			loader.fillUserData(ctx, tables, false);

			// フィールド値を設定
			{
				// 起票者の状態で上書き
				// 変更申請を起票した起案担当者の組織コードを使用する
				final UserInfo startUserInfo = res.contents.startUserInfo;
				ctx.runtimeMap.get("TXT0305").setValue(startUserInfo.getCorporationCode());
				ctx.runtimeMap.get("TXT0301").setValue(startUserInfo.getUserCode());
				ctx.runtimeMap.get("TXT0020").setValue(startUserInfo.getUserName());
				ctx.runtimeMap.get("TXT0302").setValue(startUserInfo.getOrganizationCode());
				ctx.runtimeMap.get("TXT0021").setValue(startUserInfo.getOrganizationName());
				ctx.runtimeMap.get("TXT0303").setValue(startUserInfo.getExtendedInfo01()); // 所在地コード
				ctx.runtimeMap.get("TXT0023").setValue(startUserInfo.getSbmtrAddr());
				ctx.runtimeMap.get("TXT1054").setValue(startUserInfo.getOrganizationCodeUp3());	// 起案担当者の第三階層組織コード(部・室)

				// 経常支払情報の申請者情報
				PartsStandAlone standAlone = (PartsStandAlone)ctx.runtimeMap.get("SAS0401");

				for (PartsContainerRow row : standAlone.rows) {
					String prefix = standAlone.htmlId + "-" + row.rowId + "_";
					ctx.runtimeMap.get(prefix + "TXT0301").setValue(startUserInfo.getCorporationCode());
					ctx.runtimeMap.get(prefix + "TXT0128").setValue(startUserInfo.getUserName());
					ctx.runtimeMap.get(prefix + "TXT0130").setValue(startUserInfo.getOrganizationName5());
					ctx.runtimeMap.get(prefix + "TXT1104").setValue(startUserInfo.getOrganizationCodeUp3());

					// 支払予約の申請日
					if (res.contents.applicationDate == null) {
						ctx.runtimeMap.get(prefix + "TXT1106").setValue(toStr(today())); // 申請日
					} else {
						ctx.runtimeMap.get(prefix + "TXT1106").setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
					}
				}
			}
		}
	}

	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、RuntimeMapやDesignMapを書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	@Override
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {
		super.modifyDesignContext(ctx, designCodeMap);
		// 支払予約における日付更新ボタンは非表示
		// 非表示にするためStyleクラスを付与
		PartsDesignEventButton btn = (PartsDesignEventButton)designCodeMap.get("SAS0401_EVT1143");
		btn.cssStyle += " display:none;";
	}
}