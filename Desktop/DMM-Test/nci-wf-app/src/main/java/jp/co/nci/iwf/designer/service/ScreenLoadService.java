package jp.co.nci.iwf.designer.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.javascript.ChangeStartUserFunction;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.designer.service.javascript.LoadFunction;
import jp.co.nci.iwf.designer.service.javascript.SubmitFunction;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.designer.service.userData.UserDataRepository;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmScreenPartsCondEx;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;

/**
 * 画面定義読込サービス.
 */
@BizLogic
public class ScreenLoadService extends BaseService {

	/** パーツ定義の読み込みサービス */
	@Inject ContainerLoadService service;
	/** 画面定義関連の読込リポジトリ */
	@Inject ScreenLoadRepository repository;
	/** 表示条件サービス */
	@Inject private DisplayConditionService dc;
	/** 条件評価サービス */
	@Inject private EvaluateConditionService ec;
	/** 計算式サービス */
	@Inject private CalculateService calc;
	/** パーツのユーザデータのリポジトリ */
	@Inject private UserDataRepository udRepository;
	/** 外部Javascriptサービス */
	@Inject private JavascriptService jsService;

	/**
	 * コンテナIDをもとにルートコンテナとその配下パーツを一括をロードし、
	 * 画面IDから画面定義に紐づく有効条件定義等をパーツにマージしデザイナコンテキストへ格納。
	 * @param screenId 画面ID
	 * @param ctx 格納先のデザイナコンテキスト
	 */
	public void loadScreenParts(long screenId, DesignerContext ctx) {
		final MwvScreen screen = udRepository.getMwvScreen(screenId, sessionHolder.getLoginInfo().getLocaleCode());
		if (screen == null)
			throw new NotFoundException("画面ID=" + screenId + "の画面情報が見つかりません");

		ctx.screenId = screen.screenId;
		ctx.screenCustomClass = screen.screenCustomClass;
		ctx.screenCode = screen.screenCode;
		ctx.screenName = screen.screenName;
		ctx.screen = screen;
		if (isNotEmpty(screen.submitFuncName)) {
			final SubmitFunction sf = new SubmitFunction(screen);
			if (!ctx.submitFunctions.contains(sf))
				ctx.submitFunctions.add(sf);
		}
		if (isNotEmpty(screen.loadFuncName)) {
			final LoadFunction lf = new LoadFunction(screen);
			if (!ctx.loadFunctions.contains(lf))
				ctx.loadFunctions.add(lf);
		}
		if (isNotEmpty(screen.changeStartUserFuncName)) {
			final ChangeStartUserFunction f = new ChangeStartUserFunction(screen);
			if (!ctx.changeStartUserFunctions.contains(f))
				ctx.changeStartUserFunctions.add(new ChangeStartUserFunction(screen));
		}

		// コンテナに紐付くパーツ定義等を一括読込み
		service.loadRootDesign(screen.containerId, ctx);

		// 画面定義に紐付く有効条件や計算式定義を読込み
		final Map<Long, List<PartsCond>> condMap = this.getScreenCondMap(screenId);
		final Map<Long, List<PartsCalc>> calcMap = this.getScreenCalcMap(screenId);

		// 読み込んだパーツに対して有効条件や計算定義をマージ
		merge(ctx, condMap, calcMap);

		// パーツ表示条件
		ctx.dcMap  = dc.getMwmpartsDcMap(ctx.containerIds, ctx.dcId);

		// 条件判定先パーツIDからみた条件判定元パーツのパーツID一覧Map
		ctx.targetCondMap = ec.createTargetMap(ctx.designMap);

		// 計算元パーツIDからみた計算先パーツのパーツID一覧Map
		ctx.targetCalcMap = calc.createCalcTargetMap(ctx.designMap);

		// 計算条件の判定元パーツIDからみた計算先パーツのパーツID一覧Map
		ctx.targetCalcEcMap = calc.createCalcEcTargetMap(ctx.designMap);

		// Ajaxの起動元パーツのパーツID一覧Map
		ctx.triggerAjaxMap = PartsAjaxUtils.createTriggerAjaxMap(ctx.designMap);

		// 外部JavascriptのスクリプトIDリスト（画面とコンテナ、それぞれの外部Javascript）
		ctx.javascriptIds = jsService.getJavascriptIds(screen);
		jsService.toJavascriptIds(ctx).forEach(id -> {
			if (!ctx.javascriptIds.contains(id))
				ctx.javascriptIds.add(id);
		});

		// 必要に応じて、画面カスタムクラスにてデザイン定義を変更
		if (ctx.renderMode != RenderMode.DESIGN) {
			final Map<String, PartsDesign> designCodeMap = ctx.designMap.values().stream()
					.collect(Collectors.toMap(d -> d.designCode, d -> d, (d1, d2) -> d1));
			final IScreenCustom screenCustom = IScreenCustom.get(ctx.screenCustomClass);
			screenCustom.modifyDesignContext(ctx, designCodeMap);
		}
	}

	/** 画面パーツ条件をパーツIDをキーとしたMapで抽出 */
	public Map<Long, List<PartsCond>> getScreenCondMap(Long screenId) {
		// 画面IDに紐付く画面パーツ条件定義、画面パーツ条件項目定義を取得
		final Map<Long, List<MwmScreenPartsCondEx>> condMap = repository.getMwmScreenPartsCondMap(screenId);
		final Map<Long, List<MwmScreenPartsCondItem>> condItemMap = repository.getMwmScreenPartsCondItemMap(screenId);

		// 戻り値
		final Map<Long, List<PartsCond>> result =
				condMap.keySet().stream().collect(Collectors.toMap(k -> k, k -> condMap.get(k).stream().map(e -> new PartsCond(e)).collect(Collectors.toList())));
		// PartsCondに対して条件項目を設定
		for (Long partsId : result.keySet()) {
			result.get(partsId).stream().forEach(cal -> {
				if (condItemMap.containsKey(cal.screenPartsCondId)) {
					cal.items = condItemMap.get(cal.screenPartsCondId).stream().map(ci -> new PartsCondItem(ci)).collect(Collectors.toList());
				}
			});
		}

		return result;
	}

	/** 画面パーツ計算式をパーツIDをキーとしたMapで抽出 */
	public Map<Long, List<PartsCalc>> getScreenCalcMap(Long screenId) {
		// 画面IDに紐付く画面計算式定義、画面計算式項目定義、画面計算式有効条件定義を取得
		final Map<Long, List<MwmScreenCalc>> calcMap = repository.getMwmScreenCalcMap(screenId);
		final Map<Long, List<MwmScreenCalcItem>> calcItemMap = repository.getMwmScreenCalcItemMap(screenId);
		final Map<Long, List<MwmScreenCalcEc>> calcEcMap = repository.getMwmScreenCalcEcMap(screenId);

		// 戻り値
		final Map<Long, List<PartsCalc>> result =
				calcMap.keySet().stream().collect(Collectors.toMap(k -> k, k -> calcMap.get(k).stream().map(e -> new PartsCalc(e)).collect(Collectors.toList())));

		// PartsCalcに対して計算式項目、計算式有効条件を設定
		for (Long partsId : result.keySet()) {
			result.get(partsId).stream().forEach(cal -> {
				if (calcItemMap.containsKey(cal.screenCalcId)) {
					cal.items = calcItemMap.get(cal.screenCalcId).stream().map(ci -> new PartsCalcItem(ci)).collect(Collectors.toList());
				}
				if (calcEcMap.containsKey(cal.screenCalcId)) {
					cal.ecs = calcEcMap.get(cal.screenCalcId).stream().map(ce -> new PartsCalcEc(ce)).collect(Collectors.toList());
				}
			});
		}

		return result;
	}

	private void merge(DesignerContext ctx, Map<Long, List<PartsCond>> condMap, Map<Long, List<PartsCalc>> calcMap) {
		condMap.keySet().stream().forEach(k -> {
			PartsDesign parts = ctx.designMap.get(k);
			if (parts != null && isEmpty(parts.partsConds)) {
				parts.partsConds = condMap.get(k);
			}
		});
		calcMap.keySet().stream().forEach(k -> {
			PartsDesign parts = ctx.designMap.get(k);
			if (parts != null && isEmpty(parts.partsCalcs)) {
				parts.partsCalcs = calcMap.get(k);
			}
		});
	}
}
