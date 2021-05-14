package jp.co.nci.iwf.endpoint.vd.vd0110;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.LookupGroupId;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.MwmLookupService;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignLabel;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.service.ContainerLoadService;
import jp.co.nci.iwf.designer.service.ContainerSaveService;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;

/**
 * 画面コンテナ設定サービス
 */
@BizLogic
public class Vd0110Service extends BaseService {
	@Inject
	private PartsRenderFactory renderFactory;
	@Inject
	private ContainerLoadService partsLoadService;
	@Inject
	private ContainerSaveService partsSaveService;
	@Inject
	private Vd0110Repository vd0110Repository;
	@Inject
	private Vd0110ValidateService validator;
	@Inject
	private MwmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public BaseResponse init(Vd0110InitRequest req) {
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		final Vd0110InitResponse res = createResponse(Vd0110InitResponse.class, req);
		final DesignerContext ctx = DesignerContext.designInstance(req.viewWidth);
		res.ctx = ctx;

		List<OptionItem> dcList;

		if (req.containerId == null) {
			// 新規コンテナ
			final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
			partsLoadService.newRootContainer(corporationCode, ctx);
			dcList = new ArrayList<>();
		} else {
			// コンテナ定義をロード
			partsLoadService.loadRootDesign(req.containerId, ctx);
			// 表示条件の選択肢
			dcList = vd0110Repository.getDcList(req.containerId, localeCode);

			if (ctx.root == null)
				res.addAlerts(i18n.getText(MessageCd.noRecord));

			// 排他判定
			if (!eq(ctx.root.containerVersion, req.version))
				throw new AlreadyUpdatedException();
		}
		dcList.add(0, OptionItem.EMPTY);
		res.dcList = dcList;

		// トレイタイプの選択肢
		res.trayTypes = lookup.getOptionItems(LookupGroupId.TRAY_TYPE, false);

		// レンダリング
		res.html = renderFactory.renderAll(ctx);

		// ルートコンテナ配下の全カスタムCSSスタイルを集約
		res.customCssStyleTag = PartsUtils.toCustomStyles(ctx);

		res.success = (ctx.root != null);
		return res;
	}

	/**
	 * パーツの追加
	 * @param req
	 * @return
	 */
	public Vd0110PartsResponse addParts(Vd0110PartsRequest req) {
		if (req.partsType == 0) throw new BadRequestException("パーツ種別が未指定です");
		if (req.ctx == null) throw new BadRequestException("パーツコンテキストが未指定です");

		final DesignerContext ctx = req.ctx;
		final Map<Long, PartsDesign> designMap = ctx.designMap;
		final int partsType = req.partsType;
		final PartsDesignRootContainer root = ctx.root;

		// パーツ種別をもとにパーツ定義を生成
		final PartsDesign design = PartsUtils.newPartsDesign(partsType, ctx);
		design.bgHtmlCellNo = req.bgHtmlCellNo;

		// クリックされたパーツがあれば、その直後に新パーツを挿入
		final Long clickedPartsId = req.partsId;
		if (clickedPartsId != null) {
			boolean found = false;
			for (Long partsId : root.childPartsIds) {
				PartsDesign d = ctx.designMap.get(partsId);
				if (found) {
					// 新パーツ以降のパーツの並び順をずらす
					d.sortOrder = d.sortOrder + 1;
				}
				if (eq(clickedPartsId, partsId)) {
					// クリックされたパーツの直後に新パーツを入れるため、並び順を調整
					found = true;
					design.sortOrder = d.sortOrder + 1;
					design.bgHtmlCellNo = d.bgHtmlCellNo;
				}
			}
		}

		// パーツ追加
		designMap.put(design.partsId, design);
		if (root.childPartsIds.indexOf(design.partsId) < 0) {
			root.childPartsIds.add(design.partsId);
			root.childPartsIds.sort((id1, id2) -> {
				PartsDesign d1 = ctx.designMap.get(id1);
				PartsDesign d2 = ctx.designMap.get(id2);
				return compareTo(d1.sortOrder, d2.sortOrder);
			});
		}

		// ラベル＋本体を同時にレンダリング
		final Vd0110PartsResponse res = createResponse(Vd0110PartsResponse.class, req);
		res.html = renderFactory.renderAll(ctx);
		res.designMap = designMap;
		res.newPartsId = design.partsId;
		res.childPartsIds = root.childPartsIds;
		res.success = true;
		res.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
		res.partsCodeSeq = (ctx.root == null ? 1: ctx.root.partsCodeSeq);
		return res;
	}

	/**
	 * パーツ編集
	 * @param req
	 * @return
	 */
	public Vd0110PartsResponse editParts(Vd0110PartsRequest req) {
		if (req.design == null) throw new BadRequestException("編集パーツ定義が未指定です");
		if (req.ctx == null) throw new BadRequestException("デザイナーコンテキストが未指定です");

		final DesignerContext ctx = req.ctx;
		final PartsDesign design = req.design;
		if (design == null) throw new BadRequestException("コピー元パーツ定義が存在しません");

		ctx.designMap.put(design.partsId, design);

		// 編集されたのがコンテナであれば、パーツコードがき変わっているかもしれないので配下のデザインコードを書き換える
		if (design instanceof PartsDesignContainer) {
			PartsUtils.refreshDesignCode((PartsDesignContainer)design, ctx);
		}

		// パーツ定義＝ラベルかつ関連付けられたパーツがあれば、それらの間でラベル文言を同期
		if (design instanceof PartsDesignLabel)
			PartsUtils.syncLabelText((PartsDesignLabel)design, ctx.designMap);

		// 再生成したカラム定義に既存定義を転写してマージ
		PartsUtils.mergeColumns(design);

		// 編集したパーツがコンテナで、かつ子コンテナを変更すると以前の子パーツがデザインMapにゴミとして残ってしまう。
		// このゴミを取り除くべく、デザインMapを再構築する
		ctx.designMap = PartsUtils.rebuildDesignMap(ctx, ctx.root.childPartsIds, null);

		// ルートコンテナの子要素IDリストを並び順に従って再ソート
		Collections.sort(ctx.root.childPartsIds, (partsId1, partsId2) -> {
			final PartsDesign p1 = ctx.designMap.get(partsId1);
			final PartsDesign p2 = ctx.designMap.get(partsId2);
			return compareTo(p1, p2);
		});

		// バリデーション
		final Vd0110PartsResponse res = createResponse(Vd0110PartsResponse.class, req);
		final String error = validator.validate(design);
		if (error != null) {
			res.addAlerts(error);
			res.success = false;
		}
		else {
			// 子要素があるなら再ロード
			if (design instanceof PartsDesignChildHolder) {
				final PartsDesignChildHolder ch = (PartsDesignChildHolder)design;
				if (ch.childContainerId != null) {
					boolean stopFunction = (ch.partsType == PartsType.STAND_ALONE);
					partsLoadService.loadChildDesign(ch, ctx, stopFunction);
				}
			}
			// ラベル＋本体を同時にレンダリング
			res.html = renderFactory.renderAll(ctx);
			res.designMap = ctx.designMap;
			res.childPartsIds = ctx.root.childPartsIds;
			res.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
			res.success = true;
		}

		return res;
	}

	/**
	 * パーツコピー
	 * @param req
	 * @return
	 */
	public Vd0110PartsResponse copyParts(Vd0110PartsRequest req) {
		if (req.partsId == null) throw new BadRequestException("コピー元パーツIDが未指定です");
		if (req.ctx == null) throw new BadRequestException("デザイナーコンテキストが未指定です");

		final DesignerContext ctx = req.ctx;
		final Map<Long, PartsDesign> designMap = ctx.designMap;
		final PartsDesignRootContainer root = ctx.root;
		final PartsDesign src = ctx.designMap.get(req.partsId);
		final PartsDesign design = PartsUtils.copyPartsDesign(root, src, ctx);

		// 子要素があるならロード
		if (design instanceof PartsDesignChildHolder) {
			final PartsDesignChildHolder ch = (PartsDesignChildHolder)design;
			if (ch.childContainerId != null) {
				boolean stopFunction = (ch.partsType == PartsType.STAND_ALONE);
				partsLoadService.loadChildDesign(ch, ctx, stopFunction);
			}
		}

		// パーツ追加
		designMap.put(design.partsId, design);
		if (root.childPartsIds.indexOf(design.partsId) < 0) {
			root.childPartsIds.add(design.partsId);
		}

		final Vd0110PartsResponse res = createResponse(Vd0110PartsResponse.class, req);
		res.html = renderFactory.renderAll(ctx);
		res.designMap = designMap;
		res.childPartsIds = root.childPartsIds;
		res.newPartsId = design.partsId;
		res.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
		res.success = true;

		return res;
	}

	/**
	 * パーツコピー
	 * @param req
	 * @return
	 */
	public Vd0110PartsResponse deleteParts(Vd0110PartsRequest req) {
		if (req.partsId == null) throw new BadRequestException("削除対象パーツIDが未指定です");
		if (req.ctx == null) throw new BadRequestException("デザイナーコンテキストが未指定です");

		final DesignerContext ctx = req.ctx;

		// ルートコンテナから削除
		for (Iterator<Long> it = ctx.root.childPartsIds.iterator(); it.hasNext(); ) {
			final Long id = it.next();
			if (req.partsId.equals(id))
				// パーツIDが同一なら自分自身が削除対象
				it.remove();
			else
				// 自分が削除対象でなくてもパーツ内のプロパティとしてこのパーツIDを使っているかもしれない
				ctx.designMap.get(id).removePartsId(req.partsId);
		}

		// 削除パーツが子や孫パーツをもつと、デザインMapにゴミが残ってしまう。
		// 削除パーツの子パーツを他コンテナが使っている場合があるので、単純削除はできないためデザイン時のパーツMapを再構築。
		ctx.designMap = PartsUtils.rebuildDesignMap(ctx, ctx.root.childPartsIds, req.partsId);

		// ラベル＋本体を同時にレンダリング
		final Vd0110PartsResponse res = createResponse(Vd0110PartsResponse.class, req);
		res.html = renderFactory.renderAll(ctx);
		res.designMap = ctx.designMap;
		res.childPartsIds = ctx.root.childPartsIds;
		res.customCssStyleTag = PartsUtils.toCustomStyles(ctx);
		res.success = true;

		return res;
	}

	/**
	 * 背景HTML変更
	 * @param req
	 * @return
	 */
	public Vd0110PartsResponse editBgHtml(Vd0110PartsRequest req) {
		final Vd0110PartsResponse res = createResponse(Vd0110PartsResponse.class, req);
		res.html = renderFactory.renderAll(req.ctx);
		res.customCssStyleTag = PartsUtils.toCustomStyles(req.ctx);
		res.success = true;
		return res;
	}

	/**
	 * 再描画
	 * @param req
	 * @return
	 */
	public Vd0110PartsResponse refresh(Vd0110PartsRequest req) {
		final Vd0110PartsResponse res = createResponse(Vd0110PartsResponse.class, req);
		res.html = renderFactory.renderAll(req.ctx);
		res.customCssStyleTag = PartsUtils.toCustomStyles(req.ctx);
		res.success = true;
		return res;
	}

	/**
	 * 更新
	 * @param req
	 * @return
	 */
	@Transactional
	public BaseResponse save(Vd0110SaveRequest req) {
		// 整合性チェック
		final List<String> errors = validator.validate(req.ctx);

		final Vd0110SaveResponse res = createResponse(Vd0110SaveResponse.class, req);
		if (errors == null || errors.isEmpty()) {
			// 差分更新
			partsSaveService.save(req.ctx.root, req.ctx.designMap);

			partsLoadService.loadRootDesign(req.ctx.root.containerId, req.ctx);
			res.ctx = req.ctx;
			res.success = true;
			res.addSuccesses(i18n.getText(MessageCd.MSG0067, MessageCd.containerInfo));
		} else {
			res.success = false;
			res.addAlerts(errors.toArray(new String[errors.size()]));
		}
		return res;
	}
}
