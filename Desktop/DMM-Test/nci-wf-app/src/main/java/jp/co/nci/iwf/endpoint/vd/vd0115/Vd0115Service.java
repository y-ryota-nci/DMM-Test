package jp.co.nci.iwf.endpoint.vd.vd0115;

import java.util.Map;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionCode;
import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.service.CalculateService;
import jp.co.nci.iwf.designer.service.DisplayConditionService;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.EvaluateConditionService;
import jp.co.nci.iwf.designer.service.PartsAjaxUtils;
import jp.co.nci.iwf.designer.service.PartsValidationService;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.ActionInfo;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * プレビュー画面サービス
 */
@BizLogic
public class Vd0115Service extends BaseService {
	/** レンダラーFactory */
	@Inject private PartsRenderFactory renderFactory;
	/** 表示条件サービス */
	@Inject private DisplayConditionService dc;
//	/** 有効条件サービス */
//	@Inject private EnabledConditionService ec;
	/** 条件判定サービス */
	@Inject private EvaluateConditionService ec;
	/** 計算式サービス */
	@Inject private CalculateService calc;
	/** パーツのユーザデータのサービス */
	@Inject private UserDataService userDataService;
	/** パーツのバリデーションサービス */
	@Inject private PartsValidationService partsValidationService;
	/** 外部Javascriptサービス */
	@Inject private JavascriptService jsService;
	/** パーツ条件に関するユーティリティクラス. */
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0115Response init(Vd0115Request req) {
		// レンダリングモードをプレビューに切り替え、出力するHTMLを変える
		DesignerContext ctx = req.ctx;
		ctx.renderMode = RenderMode.PREVIEW;
		ctx.dcId = req.dcId;
		ctx.trayType = req.trayType;
		ctx.viewWidth = req.viewWidth;

		// パーツ表示条件
		ctx.dcMap  = dc.getMwmpartsDcMap(ctx.containerIds, req.dcId);
		// パーツ条件の判定元パーツIDからみた判定先パーツのパーツID一覧Map
		ctx.targetCondMap = ec.createTargetMap(ctx.designMap);
		// 計算元パーツIDからみた計算先パーツのパーツID一覧Map
		ctx.targetCalcMap = calc.createCalcTargetMap(ctx.designMap);
		// 計算条件の判定元パーツIDからみた計算先パーツのパーツID一覧Map
		ctx.targetCalcEcMap = calc.createCalcEcTargetMap(ctx.designMap);
		// Ajaxの起動元パーツのパーツID一覧Map
		ctx.triggerAjaxMap = PartsAjaxUtils.createTriggerAjaxMap(ctx.designMap);
		// 外部JavascriptのスクリプトIDリスト
		ctx.javascriptIds = jsService.toJavascriptIds(ctx);

		// コンテキストに従って画面コンテンツであるHTMLを生成
		final Vd0115Response res = createResponse(Vd0115Response.class, req);
		res.ctx = req.ctx;
		res.html = renderFactory.renderAll(req.ctx);
		res.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
		// 再描画時に使用するcontentsクラスを生成
		res.contents = new Vd0310Contents();
		res.contents.trayType = req.trayType;
		res.contents.dcId = req.dcId;
		res.success = true;
		return res;
	}

	/**
	 * 入力内容から業務管理項目を作成
	 * @param req
	 * @return
	 */
	public Vd0115Response createBusinessInfoMap(Vd0115Request req) {
		DesignerContext ctx = req.ctx;
		final Vd0115Response res = createResponse(Vd0115Response.class, req);

		// 入力値から業務管理項目Mapを作成
		res.businessInfoMap = userDataService.createBusinessInfoMap(ctx);
		res.success = true;
		return res;
	}

	/**
	 * サーバ側のバリデーションを実行
	 * @param req
	 * @return
	 */
	public Vd0115Response validateServer(Vd0115Request req) {
		final Vd0115Response res = createResponse(Vd0115Response.class, req);

		// 現在までに入力されたパーツ値による有効条件の判定結果Mapを生成
		final Map<String, EvaluateCondition> ecResults = partsCondUtils.createEcResults(req.ctx);

		IScreenCustom custom = IScreenCustom.get(req.ctx.screenCustomClass);
		Vd0310ExecuteRequest reqDummy = new Vd0310ExecuteRequest();
		reqDummy.viewWidth = req.viewWidth;
		reqDummy.startUserInfo = sessionHolder.getLoginInfo();
		reqDummy.runtimeMap = req.ctx.runtimeMap;
		reqDummy.actionInfo = new ActionInfo();
		reqDummy.actionInfo.actionCode = ActionCode.DEFAULT;
		reqDummy.actionInfo.actionType = ActionType.NORMAL;
		Vd0310ExecuteResponse resDummy = new Vd0310ExecuteResponse();
		custom.beforeValidate(reqDummy, resDummy);

		// バリデーションとバリデーション結果による親コンテナのページ番号の変更処理
		res.errors = partsValidationService.validate(req.ctx, ecResults, true);

		custom.afterValidate(reqDummy, resDummy);

		// エラー内容が表示するためのHTMLをレンダリング
		res.ctx = req.ctx;
		res.html = renderFactory.renderAll(req.ctx);

		res.success = true;
		return res;
	}
}
