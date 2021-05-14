package jp.co.dmm.customize.component.screenCustomize;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.dmm.customize.endpoint.pr.PurrqstInfService;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.integrated_workflow.common.CodeMaster.VariableDefCode;
import jp.co.nci.iwf.component.CodeBook.TrayType;
import jp.co.nci.iwf.component.profile.UserInfo;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;

/**
 * WF購入依頼の画面カスタマイズ
 */
@ScreenCustomizable
@Named
public class SCR0054ScreenCustomize extends DmmScreenCustomize {

	@Inject private PurrqstInfService service;

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		if (in(req.trayType, TrayType.NEW, TrayType.WORKLIST)) {
			if (req.processId == null) {
				// 申請者情報
				setApplicationInfo(ctx, res.contents.startUserInfo);
			}
			// 申請日：申請するまではシステム日付、申請後は申請日
			setApplicationDate(res, ctx);

		}
	}

	/**
	 * 申請情報設定処理
	 * @param ctx
	 */
	protected void setApplicationInfo(RuntimeContext ctx, UserInfo startUserInfo) {
		ctx.runtimeMap.get("TXT0036").setValue(startUserInfo.getCorporationCode());
		ctx.runtimeMap.get("TXT0019").setValue(startUserInfo.getUserCode());
		ctx.runtimeMap.get("TXT0032").setValue(startUserInfo.getUserName());
		ctx.runtimeMap.get("TXT0021").setValue(startUserInfo.getOrganizationCode());	// 第五階層（チーム）の組織コード
		ctx.runtimeMap.get("TXT0033").setValue(startUserInfo.getOrganizationName());
		ctx.runtimeMap.get("TXT0025").setValue(startUserInfo.getExtendedInfo01()); // 所在地コード
		ctx.runtimeMap.get("TXT0034").setValue(startUserInfo.getSbmtrAddr());
		ctx.runtimeMap.get("TXT0038").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード

		// 明細の申請者情報
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD0030");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			ctx.runtimeMap.get(prefix + "TXT0023").setValue(startUserInfo.getCorporationCode());
			ctx.runtimeMap.get(prefix + "TXT0024").setValue(startUserInfo.getOrganizationCodeUp3()); // 第三階層（部・室）の組織コード
		}
	}

	/**
	 * 申請日設定処理
	 * @param ctx
	 */
	protected void setApplicationDate(Vd0310InitResponse res, RuntimeContext ctx) {
		PartsBase<?> applyDate = ctx.runtimeMap.get("TXT0023");
		if (res.contents.applicationDate == null) {
			applyDate.setValue(toStr(today())); // 申請日
		} else {
			applyDate.setValue(toStr(res.contents.applicationDate, "yyyy/MM/dd"));
		}
	}

	/**
	 * バリデーション前処理
	 */
	@Override
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {

		// 所在地のバリデーション
		validSbmtAddr(req, "TXT0025");
	}
	/**
	 * バリデーション後処理
	 */
	@Override
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {

		if (res.errors == null || res.errors.isEmpty()) {
			// 申請日を設定
			setRequestDate(req, "TXT0023");
		}
	}

	/**
	 * ワークフロー更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		// 購入依頼回答確認時に調達・納品のユーザに購入依頼回答の処理ユーザを設定する
		if ("0000000002".equals(req.contents.activityDefCode) && ActionType.NORMAL.equals(req.actionInfo.actionType)) {
			if (isNotEmpty(res.bizInfos)) {
				res.bizInfos.put(VariableDefCode.USER_SELECTED, res.loginInfo.getUserCode());
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
		final String purrqstNo = keys.get("purrqstNo");
		return service.getUserDataMap(companyCd, purrqstNo);
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
		// 明細の申請者情報
		final String prefix = "GRD0030_";
		if (ctx.runtimeMap != null && !ctx.runtimeMap.isEmpty()) {
			designCodeMap.get(prefix + "TXT0023").defaultValue = ctx.runtimeMap.get("TXT0036").getValue();	// 企業コード
			designCodeMap.get(prefix + "TXT0024").defaultValue = ctx.runtimeMap.get("TXT0038").getValue();	// 第三階層（部・室）の組織コード
		}

	}

}
