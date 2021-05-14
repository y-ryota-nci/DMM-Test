package jp.co.dmm.customize.component.screenCustomize;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.po.PrdPurordInfService;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;

/**
 * 管理_定期発注の画面カスタマイズ。
 * 通常と異なり、管理_定期発注は新規申請は行わず、UserDataLoaderServiceによる
 * VD0330での参照画面として表示をするためだけに存在している。
 * このため、その画面カスタムクラスである当クラスもそれだけに特化している。
 */
@ScreenCustomizable
@Named
public class SCR0086ScreenCustomize extends DmmScreenCustomize {
	@Inject private PrdPurordInfService prdPurordInfService;
	@Inject private UserDataService userDataService;
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	@Override
	public Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys) {
		final String companyCd = keys.get("companyCd");
		final String purordNo = keys.get("purordNo");
		return prdPurordInfService.getUserDataMap(companyCd, purordNo);
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
		prdPurordInfService.updatePrdPurordInf(userDataMap);
	}
}
