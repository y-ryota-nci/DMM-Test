package jp.co.nci.iwf.endpoint.vd.vd0031;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderMode;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.ContainerLoadService;
import jp.co.nci.iwf.designer.service.ScreenLoadRepository;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.designer.service.screenCustom.ScreenCustomizable;
import jp.co.nci.iwf.endpoint.vd.vd0110.Vd0110Repository;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.ex.MwmScreenPartsCondEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;

/**
 * 画面設定サービス
 */
@BizLogic
public class Vd0031Service extends BaseService {
	@Inject private MwmLookupService lookup;
	@Inject private Vd0031Repository repository;
	@Inject private Vd0110Repository vd0110repo;
	@Inject private ScreenLoadRepository screenRepo;
	@Inject private ScreenLoadService screenLoadService;
	@Inject private ContainerLoadService partsLoadService;
	@Inject private MultilingalService multi;
	@Inject private JavascriptService jsService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0031InitResponse init(Vd0031InitRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Vd0031InitResponse res = createResponse(Vd0031InitResponse.class, req);
		res.trayTypes = lookup.getOptionItems(LookupGroupId.TRAY_TYPE, false);

		if (req.screenId == null) {
			// 新規はスクラッチ区分とコンテナ情報を入力可能
			res.screen = new MwmScreen();
			res.screen.setScratchFlag(CommonFlag.OFF);
			res.dcList = null;
			res.scratchFlags = lookup.getOptionItems(LookupGroupId.SCRATCH_FLAG, true);
			res.containers = createContainerOptionItems(corporationCode, localeCode, req.screenId);
		}
		else {
			// 既存はスクラッチ区分とコンテナ情報を入力不可
			// この仕様により、有効条件と計算式の入力でキーが不確定な状態になることを防いでいる
			res.screen = screenRepo.get(req.screenId, localeCode);

			if (res.screen == null)
				throw new NotFoundException("screenId=" + req.screenId);

			// 排他判定
			if (!eq(res.screen.getVersion(), req.version))
				throw new AlreadyUpdatedException();

			res.dcList = createDcList(res.screen.getContainerId(), localeCode);
			res.scratchFlags = lookup.getOptionItems(LookupGroupId.SCRATCH_FLAG, false, res.screen.getScratchFlag());
			res.containers = createContainerOptionItems(corporationCode, localeCode, req.screenId);

			// 画面パーツ有効条件、画面計算式定義を設定
			res.condMap = getPartsCondMap(req.screenId);
			res.calcMap = getPartsCalcMap(req.screenId);

			// 画面Javascript定義
			res.scripts = repository.getScreenJavascript(req.screenId);
		}
		res.success = true;
		return res;
	}

	/** コンテナの選択肢を生成 */
	private List<OptionItem> createContainerOptionItems(String corporationCode, String localeCode, Long screenId) {
		List<OptionItem> items = null;
//		if (screenId == null) {
			// 新規なら未使用のコンテナすべて
			items = repository.getMwmContainers(corporationCode, localeCode)
					.stream()
					.map(c -> new OptionItem(c.getContainerId(), c.getContainerCode() + " " + c.getContainerName()))
					.collect(Collectors.toList());
			items.add(0, OptionItem.EMPTY);
//		}
//		else {
//			// 既存なら自分のコンテナのみ
//			items = repository.getMwmContainers(screenId, localeCode)
//					.stream()
//					.map(c -> new OptionItem(c.getContainerId(), c.getContainerCode() + " " + c.getContainerName()))
//					.collect(Collectors.toList());
//		}

		return items;
	}

	private Map<Long, List<PartsCond>> getPartsCondMap(Long screenId) {
		// 画面IDに紐付く画面パーツ条件定義、画面パーツ条件項目定義を取得
		final Map<Long, List<MwmScreenPartsCondEx>> condMap = screenRepo.getMwmScreenPartsCondMap(screenId);
		final Map<Long, List<MwmScreenPartsCondItem>> condItemMap = screenRepo.getMwmScreenPartsCondItemMap(screenId);

		// 戻り値
		final Map<Long, List<PartsCond>> result =
				condMap.keySet().stream().collect(Collectors.toMap(k -> k, k -> condMap.get(k).stream().map(e -> new PartsCond(e)).collect(Collectors.toList())));

		// PartsCondに対して画面パーツ条件項目を設定
		for (Long partsId : result.keySet()) {
			result.get(partsId).stream().forEach(cal -> {
				if (condItemMap.containsKey(cal.screenPartsCondId)) {
					cal.items = condItemMap.get(cal.screenPartsCondId).stream().map(ci -> new PartsCondItem(ci)).collect(Collectors.toList());
				}
			});
		}

		return result;
	}

	private Map<Long, List<PartsCalc>> getPartsCalcMap(Long screenId) {
		// 画面IDに紐付く画面計算式定義、画面計算式項目定義、画面計算式有効条件定義を取得
		final Map<Long, List<MwmScreenCalc>> calcMap = screenRepo.getMwmScreenCalcMap(screenId);
		final Map<Long, List<MwmScreenCalcItem>> calcItemMap = screenRepo.getMwmScreenCalcItemMap(screenId);
		final Map<Long, List<MwmScreenCalcEc>> calcEcMap = screenRepo.getMwmScreenCalcEcMap(screenId);

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

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Vd0031SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final BaseResponse res = createResponse(BaseResponse.class, req);

		// エラー
		List<String> errors = validate(req);
		if (!errors.isEmpty()) {
			res.addAlerts(errors.toArray(new String[errors.size()]));
			res.success = false;
		}
		else {
			// 差分更新
			MwmScreen input = req.screen;
			MwmScreen org = screenRepo.get(input.getScreenId(), localeCode);
			long screenId;
			if (org == null)
				screenId = repository.insert(input, corporationCode);
			else
				screenId = repository.update(input, org);

			multi.save("MWM_SCREEN", screenId, "SCREEN_NAME", input.getScreenName());

			// MWM_SCREEN_PARTS_COND・MWM_SCREEN_PARTS_COND_ITEMの差分更新
			saveMwmScreenPartsCond(screenId, req.condMap);

			// MWM_SCREEN_CALC・MWM_SCREEN_CALC_ITEM・MWM_SCREEN_CALC_ECの差分更新
			saveMwmScreenCalc(screenId, req.calcMap);

			// MWM_SCREEN_JAVASCRIPT
			saveMwmScreenJavascript(screenId, req.scripts);

			res.success = true;
		}
		return res;
	}

	/** 画面Javascript定義の差分更新 */
	private void saveMwmScreenJavascript(long screenId, List<PartsJavascript> scripts) {
		Map<Long, MwmScreenJavascript> currents =
				repository.getMwmScreenJavascript(screenId);

		for (PartsJavascript pj : scripts) {
			MwmScreenJavascript current = currents.remove(pj.javascriptId);
			if (current == null)
				repository.insert(pj, screenId);
			else
				repository.update(pj, current);
		}
		// 残余は不要になったものなので削除
		for (MwmScreenJavascript sj : currents.values())
			repository.delete(sj);

		// Javascriptキャッシュをクリア
		jsService.clear();
	}

	/** バリデーション */
	private List<String> validate(Vd0031SaveRequest req) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final List<String> errors = new ArrayList<>();

		// 画面コードの重複チェック
		final long screenId = req.screen.getScreenId();
		final String screenCode = req.screen.getScreenCode();
		if (isNotEmpty(screenCode)) {
			if (repository.isDuplicateScreenCode(corporationCode, screenCode, screenId)) {
				errors.add(i18n.getText(MessageCd.MSG0130, MessageCd.screenCode));
			}
		}

		// スクラッチ区分＝画面デザイナならコンテナID必須
		final String scratchFlag = req.screen.getScratchFlag();
		if (CommonFlag.OFF.equals(scratchFlag)) {
			// コンテナID必須
			final Long containerId = req.screen.getContainerId();
			if (containerId == null) {
				errors.add(i18n.getText(MessageCd.MSG0001, MessageCd.containerInfo));
			}
			else {
				// コンテナIDの存在チェック
				if (!repository.existContainer(containerId, corporationCode))
					errors.add(i18n.getText(MessageCd.MSG0129, MessageCd.containerInfo));

//				// コンテナIDの重複チェック
//				// 任意のコンテナIDを利用できるのは、一画面のみ。
//				if (repository.isDuplicateContainerId(containerId, screenId))
//					errors.add(i18n.getText(MessageCd.MSG0130, MessageCd.containerInfo));
			}
		}
		// 画面カスタムクラス（FQCN）
		final String screenCustomClass = req.screen.getScreenCustomClass();
		if (isNotEmpty(screenCustomClass)) {
			String fieldName = i18n.getText(MessageCd.screenCustomClass);
			try {
				// インターフェースIScreenCustomを実装しているか
				final Class<?> clazz = Class.forName(screenCustomClass);
				if (!IScreenCustom.class.isAssignableFrom(clazz))
					errors.add(i18n.getText(MessageCd.MSG0143, fieldName, screenCustomClass, IScreenCustom.class.getName()));
				// @ScreenCustomizableでアノテーションされているか
				else if (!clazz.isAnnotationPresent(ScreenCustomizable.class))
					errors.add(i18n.getText(MessageCd.MSG0144, fieldName, screenCustomClass, ScreenCustomizable.class.getSimpleName()));
				// @Namedでアノテーションされているか
				else if (!clazz.isAnnotationPresent(Named.class))
					errors.add(i18n.getText(MessageCd.MSG0144, fieldName, screenCustomClass, Named.class.getSimpleName()));
			}
			catch (ClassNotFoundException e) {
				// クラスが存在しない
				errors.add(i18n.getText(MessageCd.MSG0142, fieldName, screenCustomClass));
			}
		}
		return errors;
	}

	/**  MWM_SCREEN_PARTS_COND・MWM_SCREEN_PARTS_COND_ITEMの差分更新 */
	private void saveMwmScreenPartsCond(long screenId, Map<Long, List<PartsCond>> targets) {
		// 画面IDに紐付く画面パーツ条件定義を抽出し、ユニークキー(画面ID + パーツID + パーツ条件区分)をキーにMap化
		final Map<String, MwmScreenPartsCond> condMap = screenRepo.getMwmScreenPartsCondList(screenId).stream()
				.collect(Collectors.toMap(pc -> toScreenPartsCondUniqueKey(pc) ,pc -> pc));
		// 画面IDに紐付く画面パーツ条件項目定義を画面パーツ条件項目IDをキーにMapで抽出
		final Map<Long, MwmScreenPartsCondItem> itemMap = screenRepo.getMwmScreenPartsCondItemMapByItemId(screenId);

		// 差分更新
		if (isNotEmpty(targets)) {
			for (Long partsId : targets.keySet()) {
				// 画面パーツ条件定義の差分更新
				Long screenPartsCondId = null;
				for (PartsCond cond : targets.get(partsId)) {
					// パーツ条件定義のユニークキー(パーツID + パーツ条件区分)を取得
					String key = toScreenPartsCondUniqueKey(screenId, partsId, cond);
					final MwmScreenPartsCond condOrg = condMap.remove(key);
					if (condOrg == null) {
						screenPartsCondId = repository.insert(cond, screenId, partsId);
					} else {
						screenPartsCondId = repository.update(cond, condOrg);
					}

					// 画面パーツ条件項目定義の差分更新
					if (isNotEmpty(cond.items)) {
						for (PartsCondItem item : cond.items) {
							final MwmScreenPartsCondItem org = itemMap.remove(item.screenPartsCondItemId);
							if (org == null)
								repository.insert(item, screenPartsCondId);
							else
								repository.update(item, org);
						}
					}
				}
			}
		}

		// 不要になった画面パーツ条件定義の画面パーツ条件IDをかき集める
		final Set<Long> deleteScreenPartsCondIds = condMap.values().stream().map(MwmScreenPartsCond::getScreenPartsCondId).collect(Collectors.toSet());
		// 不要になった計算式定義を一括削除
		repository.deleteMwmScreenPartsCond(deleteScreenPartsCondIds);
		// 画面パーツ条件項目定義を一括削除
		repository.deleteMwmScreenPartsCondItem(deleteScreenPartsCondIds, itemMap.keySet());
	}

	/** パーツ条件定義のユニークキーをキー文字列化 */
	private String toScreenPartsCondUniqueKey(MwmScreenPartsCond spc) {
		return join(spc.getScreenId(), "_", spc.getPartsId(), "_", spc.getPartsConditionType());
	}

	/** パーツ条件定義のユニークキーをキー文字列化 */
	private String toScreenPartsCondUniqueKey(Long screenId, Long partsId, PartsCond pc) {
		return join(screenId, "_", partsId, "_", pc.partsConditionType);
	}

	/** MWM_SCREEN_CALC・MWM_SCREEN_CALC_ITEM・MWM_SCREEN_CALC_ECを差分更新  */
	private void saveMwmScreenCalc(long screenId, Map<Long, List<PartsCalc>> targets) {
		// 画面IDに紐付く計算式定義、計算項目定義、計算条件定義をMapで抽出
		final Map<Long, MwmScreenCalc> calcMap = screenRepo.getMwmScreenCalcMapByCalcId(screenId);
		final Map<Long, MwmScreenCalcItem> itemMap = screenRepo.getMwmScreenCalcItemMapByItemId(screenId);
		final Map<Long, MwmScreenCalcEc> ecMap = screenRepo.getMwmScreenCalcEcMapByEcId(screenId);

		// 差分更新
		if (isNotEmpty(targets)) {
			for (Long partsId : targets.keySet()) {
				// パーツ計算式定義の差分更新
				Long screenCalcId = null;
				for (PartsCalc calc : targets.get(partsId)) {
					final MwmScreenCalc calcOrg = calcMap.remove(calc.screenCalcId);
					if (calcOrg == null) {
						screenCalcId = repository.insert(calc, screenId, partsId);
					} else {
						screenCalcId = repository.update(calc, calcOrg);
					}
					// 多言語対応(計算式名)
					multi.save("MWM_SCREEN_CALC", screenCalcId, "PARTS_CALC_NAME", calc.partsCalcName);

					// パーツ計算項目定義の差分更新
					if (isNotEmpty(calc.items)) {
						for (PartsCalcItem item : calc.items) {
							final MwmScreenCalcItem org = itemMap.remove(item.screenCalcItemId);
							if (org == null)
								repository.insert(item, screenCalcId);
							else
								repository.update(item, org);
						}
					}

					// パーツ計算式有効条件定義の差分更新
					if (isNotEmpty(calc.ecs)) {
						for (PartsCalcEc ec : calc.ecs) {
							final MwmScreenCalcEc org = ecMap.remove(ec.screenCalcEcId);
							if (org == null)
								repository.insert(ec, screenCalcId);
							else
								repository.update(ec, org);
						}
					}
				}
			}
		}

		// 不要になった計算式定義の計算式IDをかき集める
		final Set<Long> deleteScreenCalcIds = calcMap.keySet();
		// 不要になった計算式定義を一括削除
		repository.deleteMwmScreenCalc(deleteScreenCalcIds);
		// 多言語対応している「計算式名」を一つずつ削除
		for (Long screenCalcId : deleteScreenCalcIds) {
			multi.physicalDelete("MWM_SCREEN_CALC", screenCalcId, "PARTS_CALC_NAME");
		}

		// 計算項目定義、計算式有効条件定義を一括削除
		repository.deleteMwmScreenCalcItem(deleteScreenCalcIds, itemMap.keySet());
		repository.deleteMwmScreenCalcEc(deleteScreenCalcIds, ecMap.keySet());
	}

	/**
	 * 表示条件リストの取得
	 * @param req
	 * @return
	 */
	public Vd0031DcResponse getDcList(Vd0031ContainerRequest req) {
		final Vd0031DcResponse res = createResponse(Vd0031DcResponse.class, req);
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Long containerId = req.containerId;

		res.dcList = createDcList(containerId, localeCode);
		res.success = true;

		return res;
	}

	private List<OptionItem> createDcList(Long containerId, String localeCode) {
		if (containerId == null)
			return new ArrayList<>();

		List<OptionItem> dcList = vd0110repo.getDcList(containerId, localeCode);
		dcList.add(0, OptionItem.EMPTY);
		return dcList;
	}

	/**
	 * プレビュー前処理
	 * @param req
	 * @return
	 */
	public Vd0031PreviewResponse preparePreview(Vd0031ContainerRequest req) {
		final DesignerContext ctx = DesignerContext.previewInstance(req.dcId, req.trayType, req.viewWidth);
		if (req.screenId == null) {
			// 画面の新規登録前は画面IDが未採番なので、しかたなくコンテナIDを使ってロードする
			partsLoadService.loadRootDesign(req.containerId, ctx);

			// 必要に応じて、画面カスタムクラスにてデザイン定義を変更
			if (ctx.renderMode != RenderMode.DESIGN) {
				final Map<String, PartsDesign> designCodeMap = ctx.designMap.values().stream()
						.collect(Collectors.toMap(d -> d.designCode, d -> d, (d1, d2) -> d1));
				final IScreenCustom screenCustom = IScreenCustom.get(req.screenCustomClass);
				screenCustom.modifyDesignContext(ctx, designCodeMap);
			}
		}
		else {
			screenLoadService.loadScreenParts(req.screenId, ctx);
		}
		// 作成されたデザイナーコンテキストに対して画面パーツ有効条件定義や画面計算式定義をマージ
		merge(ctx, req.condMap, req.calcMap, req.scripts);

		final Vd0031PreviewResponse res = createResponse(Vd0031PreviewResponse.class, req);
		res.ctx = ctx;
		res.success = true;

		return res;
	}

	/* 作成されたデザイナーコンテキストに対して画面パーツ条件定義や画面計算式定義をマージ */
	private void merge(DesignerContext ctx, Map<Long, List<PartsCond>> condMap, Map<Long, List<PartsCalc>> calcMap, List<PartsJavascript> scripts) {
		if (condMap != null) {
			condMap.keySet().stream().forEach(k -> {
				PartsDesign parts = ctx.designMap.get(k);
				if (parts != null && isEmpty(parts.partsConds)) {
					parts.partsConds = condMap.get(k);
				}
			});
		}
		if (calcMap != null) {
			calcMap.keySet().stream().forEach(k -> {
				PartsDesign parts = ctx.designMap.get(k);
				if (parts != null && isEmpty(parts.partsCalcs)) {
					parts.partsCalcs = calcMap.get(k);
				}
			});
		}
		if (scripts != null) {
			scripts.forEach(js -> {
				if (!ctx.javascriptIds.contains(js.javascriptId)) {
					ctx.javascriptIds.add(js.javascriptId);
				}
			});
		}
	}

}
