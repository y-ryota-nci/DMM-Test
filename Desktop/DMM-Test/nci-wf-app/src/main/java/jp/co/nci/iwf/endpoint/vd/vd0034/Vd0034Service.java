package jp.co.nci.iwf.endpoint.vd.vd0034;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 画面属性コピー設定画面サービス
 */
@BizLogic
public class Vd0034Service extends BaseService {
	@Inject private Vd0034Repository repository;
	@Inject private ScreenLoadService loader;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Vd0034Request req) {
		if (req.destScreenId == null)
			throw new BadRequestException("コピー先の画面IDが未指定です");
		if (req.destContainerId == null)
			throw new BadRequestException("コピー先のコンテナIDが未指定です");

		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Vd0034Response res = createResponse(Vd0034Response.class, req);
		res.screenIds = repository.getScreens(req.destContainerId, localeCode)
				.stream()
				.filter(scr -> req.destScreenId != scr.screenId)
				.map(scr -> new OptionItem(scr.screenId, scr.screenName))
				.collect(Collectors.toList());
		res.screenIds.add(0, OptionItem.EMPTY);
		res.results = new ArrayList<>();	// 初期表示は空リスト
		res.success = true;
		return res;
	}

	/**
	 * コピー元画面を変更
	 * @param req
	 * @return
	 */
	public BaseResponse changeScreen(Vd0034Request req) {
		if (req.srcScreenId == null)
			throw new BadRequestException("コピー元の画面IDが未指定です");

		// コピー元画面の定義を読み込み
		final DesignerContext ctx = DesignerContext.previewInstance(null, TrayType.NEW, ViewWidth.LG);
		loader.loadScreenParts(req.srcScreenId, ctx);

		List<Vd0034Entity> results = new ArrayList<>();
		ctx.designMap.values().stream().forEach(d -> {
			boolean canCopyConds = d.partsConds.stream().filter(pc -> pc.screenId != null).count() > 0L;
			boolean canCopyCalcs = d.partsCalcs.stream().filter(cl -> cl.screenId != null).count() > 0L;

			if (canCopyConds || canCopyCalcs) {
				Vd0034Entity r = new Vd0034Entity();
				r.containerId = req.destContainerId;
				r.screenId = req.destScreenId;
				r.partsId = d.partsId;
				r.partsType = d.partsType;
				r.partsTypeName = PartsUtils.getPartsTypeName(d.partsType);
				r.labelText = d.labelText;
				r.designCode = d.designCode;
				r.canCopyConds = canCopyConds;
				r.canCopyCalcs = canCopyCalcs;
				results.add(r);
			}
		});

		final Vd0034Response res = createResponse(Vd0034Response.class, req);
		res.results = results;
		res.success = true;
		return res;
	}

	/**
	 * コピー
	 * @param req
	 * @return
	 */
	public BaseResponse copy(Vd0034Request req) {
		if (req.destContainerId == null)
			throw new BadRequestException("コピー先のコンテナIDが未指定です");
		if (req.destScreenId == null)
			throw new BadRequestException("コピー先の画面IDが未指定です");

		if (req.srcScreenId == null)
			throw new BadRequestException("コピー元の画面IDが未指定です");
		if (req.srcParts == null || req.srcParts.isEmpty())
			throw new BadRequestException("コピー元のパーツが未指定です");
		if (req.destCondMap == null)
			throw new BadRequestException("コピー先のパーツ条件が未指定です");
		if (req.destCalcMap == null)
			throw new BadRequestException("コピー先のパーツ計算式が未指定です");

		// コピー元画面の定義を読み込み
		final DesignerContext ctx = DesignerContext.previewInstance(null, TrayType.NEW, ViewWidth.LG);
		loader.loadScreenParts(req.srcScreenId, ctx);

		final Vd0034Response res = createResponse(Vd0034Response.class, req);
		// 「コピー先／コピー元」で計算式と条件をマージ
		res.destCondMap = mergeCond(ctx, req);
		res.destCalcMap = mergeCalc(ctx, req);
		res.success = true;
		return res;
	}

	/**  「コピー先／コピー元」で条件をマージ */
	private Map<Long, List<PartsCond>> mergeCond(DesignerContext ctx, Vd0034Request req) {
		final Map<Long, List<PartsCond>> map = new HashMap<>(req.destCondMap);
		for (Vd0034Entity e : req.srcParts) {
			final PartsDesign d = ctx.designMap.get(e.partsId);
			if (e.condFlag) {
				// パーツ条件
				List<PartsCond> oldPartsConds = req.destCondMap.get(d.partsId);
				if (oldPartsConds == null) {
					oldPartsConds = new ArrayList<>();
				}
				Map<String, PartsCond> olds = oldPartsConds.stream()
					.collect(Collectors.toMap(old -> old.partsConditionType, old -> old));

				for (PartsCond pc : d.partsConds) {
					pc.partsId = e.partsId;
					pc.screenId = req.destScreenId;

					// 既存があればIDを引き継ぐ
					final PartsCond old = olds.get(pc.partsConditionType);
					pc.screenPartsCondId = (old == null ? null : old.screenPartsCondId);

					for (PartsCondItem itm : pc.items) {
						itm.screenPartsCondId = pc.screenPartsCondId;
						itm.screenPartsCondItemId = null;
						itm.identifyKey = null;
					};
				};
				map.put(d.partsId, d.partsConds);
			}
		}
		return map;
	}

	/**  「コピー先／コピー元」で計算式をマージ */
	private Map<Long, List<PartsCalc>> mergeCalc(DesignerContext ctx, Vd0034Request req) {
		final Map<Long, List<PartsCalc>> map = new HashMap<>(req.destCalcMap);
		for (Vd0034Entity e : req.srcParts) {
			final PartsDesign d = ctx.designMap.get(e.partsId);
			if (e.calcFlag) {
				for (PartsCalc cl : d.partsCalcs) {
					cl.partsId = e.partsId;
					cl.screenId = req.destScreenId;
					cl.screenCalcId = null;
					for (PartsCalcItem itm : cl.items) {
						itm.identifyKey = null;
						itm.screenCalcId = null;
						itm.screenCalcItemId = null;
					}
				};
				map.put(e.partsId, d.partsCalcs);
			}
		}
		return map;
	}
}
