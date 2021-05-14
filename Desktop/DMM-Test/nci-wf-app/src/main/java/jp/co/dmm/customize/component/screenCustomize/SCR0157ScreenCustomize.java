package jp.co.dmm.customize.component.screenCustomize;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.co.CntrctInfService;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 管理_支払予約の画面カスタムクラス
 */
@ScreenCustomizable
@Named
public class SCR0157ScreenCustomize extends DmmScreenCustomize {
	/** 契約情報サービス */
	@Inject private CntrctInfService cntrctInfService;
	@Inject private UserDataService userDataService;
	@Inject private PartsCondUtils partsCondUtils;

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
		// 通貨に対応した小数点桁数
		final PartsBase<?> rdxpntGdt = ctx.runtimeMap.get("TXT1116");
		if (rdxpntGdt != null) {
			final Integer decimalPlaces = MiscUtils.toInt(rdxpntGdt.getValue());
			final String[] designCodes = {
					"TXT1132",	// [ヘッダ部]合計金額(税額)
					"TXT0133",	// [ヘッダ部]合計金額(税抜)
					"TXT0134",	// [ヘッダ部]合計金額(税込)
					"TXT0137",	// [ヘッダ部]源泉対象額
					"TXT0138",	// [ヘッダ部]源泉徴収額
					"TXT1119",	// [ヘッダ部]支払額
					"TXT1152",	// [ヘッダ部]税抜金額(10%対象)
					"TXT1154",	// [ヘッダ部]税額(10%対象)
					"TXT1156",	// [ヘッダ部]税込金額(10%対象)
					"TXT1158",	// [ヘッダ部]税抜金額(軽減8%対象)
					"TXT1160",	// [ヘッダ部]税額(軽減8%対象)
					"TXT1162",	// [ヘッダ部]税込金額(軽減8%対象)
					"TXT1164",	// [ヘッダ部]税抜金額(8%対象)
					"TXT1166",	// [ヘッダ部]税額(8%対象)
					"TXT1168",	// [ヘッダ部]税込金額(8%対象)
					"GRD1100_TXT1007",	// [明細部]発注金額(入力値)
					"GRD1100_TXT1008",	// [明細部]発注金額(税抜)
					"GRD1100_TXT1009",	// [明細部]発注金額(税込)
			};
			// 「通貨に対応した小数点桁数」を「金額欄の小数点桁数」として設定
			for (String designCode : designCodes) {
				final PartsDesignTextbox d = (PartsDesignTextbox)designCodeMap.get(designCode);
				d.decimalPlaces = MiscUtils.defaults(decimalPlaces, 0);
			}
		}
	}

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	@Override
	public Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys) {
		final String companyCd = keys.get("companyCd");
		final String cntrctNo = keys.get("cntrctNo");
		final String rtnPayNo = keys.get("rtnPayNo");
		return cntrctInfService.getUserDataMap(companyCd, cntrctNo, rtnPayNo, true);
	}

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void afterUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// パーツのデータを吸上げてユーザデータMapへ変換
		final Map<String, EvaluateCondition> ecResults = partsCondUtils.createEcResults(res.ctx);
		final Map<String, List<UserDataEntity>> userDataMap = userDataService.toInputedUserData(res.ctx, ecResults);
		cntrctInfService.updateRtnPayPlnMst(userDataMap);
	}
}
