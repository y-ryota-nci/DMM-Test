package jp.co.nci.iwf.endpoint.vd.vd0330;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.ActionType;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.designer.PartsCondUtils;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.vd.vd0310.BaseContents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.bean.StampInfo;
import jp.co.nci.iwf.endpoint.vd.vd0310.include.Bl0002Service;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;

/**
 * 申請画面(参照)サービス
 * 各プロジェクトのカスタマイズ要件で使用され、別途、何らかの一覧画面から遷移する想定である。
 * 非画面デザイナー管理しているテーブルからデータを抽出して、画面デザイナーの描画エンジンに食わせて画面表示を行う。
 * 実際上は画面カスタマイズクラス経由でユーザデータを抽出し、それを画面デザイナーの描画エンジンに食わせてレンダリングを行う。
 */
@BizLogic
public class Vd0330Service extends BaseService {
	@Inject private PartsRenderFactory factory;
	@Inject private Vd0330Repository repository;
	@Inject private ScreenLoadService screenLoadService;
	@Inject private UserDataLoaderService userDataLoaderService;
	/** 文書内容サービス */
	@Inject private Bl0002Service bl0002Service;
	/** パーツレンダラーFactory */
	@Inject private PartsRenderFactory render;
	/** パーツ条件に関するユーティリティクラス. */
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0330Response init(Vd0330Request req) {
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(req.screenCode))
			throw new BadRequestException("画面コードが未指定です");
		if (isEmpty(req.keys))
			throw new BadRequestException("抽出キーが未指定です");

		// 画面定義の抽出
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final MwvScreen screen = repository.getScreen(req.corporationCode, req.screenCode, localeCode);
		if (screen == null)
			throw new NotFoundException(
					"画面情報が見つかりません。企業コード=" + req.corporationCode +
					", 画面コード=" + req.screenCode + ", 言語コード=" + localeCode);

		// 抽出キーをもとにユーザデータを読み込んでデザイナーコンテキストを生成
		final BaseContents contents = toBaseContents(req, screen);
		final RuntimeContext ctx = loadUserData(req, contents, screen);

		// HTML化
		final Vd0330Response res = createResponse(Vd0330Response.class, req);
		res.backUrl = req.backUrl;
		res.html = factory.renderAll(ctx);
		res.runtimeMap = ctx.runtimeMap;
		res.screenId = screen.screenId;
		res.screenName = defaults(req.screenName, screen.screenName);
		res.contents = contents;
		res.contents.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
		res.contents.submitFunctions = ctx.submitFunctions;
		res.contents.loadFunctions = ctx.loadFunctions;
		res.contents.changeStartUserFunctions = ctx.changeStartUserFunctions;
		res.contents.javascriptIds = ctx.javascriptIds;
		res.contents.screenCustomClass = ctx.screenCustomClass;

		res.success = true;
		return res;
	}

	/** パラメータをデザイナー読み込み用コンテンツへ変換 */
	private BaseContents toBaseContents(Vd0330Request req, MwvScreen screen) {
		final BaseContents contents = new Vd0310Contents();
		contents.screenId = screen.screenId;
		contents.corporationCode = screen.corporationCode;
		contents.processId = null;						// プロセスIDは定められない
		contents.trayType = defaults(req.trayType, TrayType.ALL);
		contents.dcId = defaults(req.dcId, 1L);			// 指定があればその表示条件、なければ申請
		contents.startUserInfo = null;					// 申請者ブロックがないから不要
		contents.processUserInfo = null;				// 申請者ブロックがないから不要
		contents.stampMap = toStampMap(req, screen);
		contents.activityDef = null;					// 採番しないから不要
		contents.screenCustomClass = screen.screenCustomClass;
		contents.docId = null;
		contents.screenDocId = null;
		contents.oldTaxRateDisplayReferenceDate = corpProp.getString(CorporationProperty.OLD_TAX_RATE_DISPLAY_REFERENCE_DATE);	 // 旧消費税率(8%)を表示する基準日
		return contents;
	}

	/** スタンプMapを返す */
	private Map<String, StampInfo> toStampMap(Vd0330Request req, MwvScreen screen) {
		return new HashMap<>();	// プロセスIDが定められないと求めようがない
	}

	/** 抽出キーをもとにユーザデータを抽出し、デザイナーコンテキストを生成 */
	private RuntimeContext loadUserData(Vd0330Request req, BaseContents contents, MwvScreen screen) {
		// 画面のデザイン定義を読み込む
		final RuntimeContext ctx = RuntimeContext.newInstance(contents, req.viewWidth, null);
		screenLoadService.loadScreenParts(screen.screenId, ctx);

		// 画面カスタムクラスに抽出キーを渡して、ユーザデータを抽出
		final IScreenCustom screenCustom = IScreenCustom.get(screen.screenCustomClass);
		final Map<String, List<UserDataEntity>> tables = screenCustom.createUserDataMap(req.keys);

		// ユーザデータを変換して、runtimeMapを生成
		userDataLoaderService.fillUserData(ctx, tables, false);

		// 画面カスタムクラスのイベント用パラメータをでっち上げ
		final Vd0310InitRequest reqDummy = new Vd0310InitRequest();
		reqDummy.corporationCode = req.corporationCode;
		reqDummy.trayType = ctx.trayType;
		copyFields(ctx, reqDummy);
		final Vd0310InitResponse resDummy = new Vd0310InitResponse();
		resDummy.contents = (Vd0310Contents)contents;

		// 画面カスタムクラスのイベント実行をエミュレート
		screenCustom.afterInitLoad(reqDummy, resDummy, ctx);
		screenCustom.beforeInitRender(reqDummy, resDummy, ctx);
		screenCustom.afterInitRender(reqDummy, resDummy, ctx);
		screenCustom.beforeInitResponse(reqDummy, resDummy, ctx);

		return ctx;
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse update(Vd0310ExecuteRequest req) {
		// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
		final Vd0310ExecuteResponse res = createResponse(Vd0310ExecuteResponse.class, req);
		// 画面カスタムクラス：画面カスタムクラスFQCNが未設定ならNULLインスタンスが使用される
		final IScreenCustom screenCustom = IScreenCustom.get(req.contents.screenCustomClass);

		// パーツのバリデーション
		screenCustom.beforeValidate(req, res);
		Map<String, EvaluateCondition> ecResults = null;
		if (req.runtimeMap != null && !req.runtimeMap.isEmpty()) {
			// パーツ定義の読み込み
			res.ctx = bl0002Service.load(req);

			// 現在のパーツ値による条件判定結果Map
			ecResults = partsCondUtils.createEcResults(res.ctx);

			// バリデーションとその結果の反映（バリデーション結果による親コンテナのページ番号の変更処理）
			res.errors = bl0002Service.validate(res.ctx, ecResults);
			if (!res.errors.isEmpty()) {
				res.html = render.renderAll(res.ctx, ecResults);
				res.runtimeMap = res.ctx.runtimeMap;
			}
		}
		screenCustom.afterValidate(req, res);

		// バリデーション結果で問題が無ければ、更新処理
		if (res.errors == null || res.errors.isEmpty()) {
			res.ctx.processId = req.contents.processId;
			res.processId = req.contents.processId;

			// 画面カスタムクラスの更新処理をお呼び出す
			if (!eq(ActionType.DOACTION, req.actionInfo.actionType)) {
				screenCustom.beforeUpdateUserData(req, res);
				// screenCustom.afterUpdateUserDataで使えるよう、パーツに紐付く比較条件変数(業務管理項目)をMap形式で抜き出し
				if (req.runtimeMap != null && res.ctx != null) {
					res.bizInfos = bl0002Service.createBusinessInfoMap(res);
				}
				screenCustom.afterUpdateUserData(req, res);
			}
		}
		res.success = true;
		return res;
	}
}
