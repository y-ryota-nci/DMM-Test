package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.designer.DesignerCodeBook.FontSize;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RenderingMethod;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsAttachFileEntity;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOption;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.service.javascript.ChangeStartUserFunction;
import jp.co.nci.iwf.designer.service.javascript.LoadFunction;
import jp.co.nci.iwf.designer.service.javascript.SubmitFunction;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsCondEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsChildHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsColumn;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsEvent;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsRelation;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsTableInfo;

/**
 * コンテナ単位のパーツ定義の読み込みサービス
 */
@BizLogic
public class ContainerLoadService extends BaseService {
	@Inject
	private ContainerLoadRepository loadRepository;
	@Inject
	private NumberingService numbering;

	/**
	 * コンテナIDをもとにルートコンテナとその配下パーツを一括をロードし、デザイナコンテキストへ格納。
	 * @param containerId コンテナID
	 * @param ctx 格納先のデザイナコンテキスト
	 */
	public void loadRootDesign(long containerId, DesignerContext ctx) {
		final MwmContainer srcCntr = loadRepository.getMwmContainer(containerId);
		if (srcCntr == null)
			throw new NotFoundException("コンテナID＝" + containerId + "が見つかりません");

		// 使用しているコンテナID
		ctx.containerIds = new HashSet<>();
		ctx.containerIds.add(containerId);
		// Submit時の呼び出し関数
		if (isNotEmpty(srcCntr.getSubmitFuncName())) {
			final SubmitFunction sf = new SubmitFunction(srcCntr);
			if (!ctx.submitFunctions.contains(sf))
				ctx.submitFunctions.add(sf);
		}
		// Load時の呼び出し関数
		if (isNotEmpty(srcCntr.getLoadFuncName())) {
			final LoadFunction lf = new LoadFunction(srcCntr);
			if (!ctx.loadFunctions.contains(lf))
				ctx.loadFunctions.add(lf);
		}
		// 起案担当者変更時の呼び出し関数
		if (isNotEmpty(srcCntr.getChangeStartUserFuncName())) {
			final ChangeStartUserFunction f = new ChangeStartUserFunction(srcCntr);
			if (!ctx.changeStartUserFunctions.contains(f))
				ctx.changeStartUserFunctions.add(f);
		}

		final PartsDesignRootContainer root = new PartsDesignRootContainer();
		root.setInitValue();
		root.containerId = srcCntr.getContainerId();
		root.containerCode = srcCntr.getContainerCode();
		root.containerName = srcCntr.getContainerName();
		root.customCssStyle = srcCntr.getCustomCssStyle();
		root.tableName = srcCntr.getTableName();
		root.bgHtml = srcCntr.getBgHtml();
		root.corporationCode = srcCntr.getCorporationCode();
		root.partsCodeSeq = srcCntr.getPartsCodeSeq();
		root.fontSize = srcCntr.getFontSize();
		root.submitFuncName = srcCntr.getSubmitFuncName();
		root.submitFuncParam = srcCntr.getSubmitFuncParam();
		root.loadFuncName = srcCntr.getLoadFuncName();
		root.loadFuncParam = srcCntr.getLoadFuncParam();
		root.changeStartUserFuncName = srcCntr.getChangeStartUserFuncName();
		root.changeStartUserFuncParam = srcCntr.getChangeStartUserFuncParam();
		root.containerVersion = srcCntr.getVersion();
		root.notDropTableFlag = eq(CommonFlag.ON, srcCntr.getNotDropTableFlag());

		ctx.root = root;

		// コンテナの子パーツ（孫パーツは含まず）
		final List<MwmPart> list = loadRepository.getMwmPartsList(containerId);
		root.childPartsIds.clear();
		for (MwmPart p : list) {
			if (root.childPartsIds.indexOf(p.getPartsId()) < 0)
				root.childPartsIds.add(p.getPartsId());
		}

		// コンテナに紐付くJavascript定義
		// コンテナに紐付くJavascript定義
		root.javascripts = loadRepository.getMwmContainerJavascript(containerId)
				.stream().map(cj -> new PartsJavascript(cj))
				.collect(Collectors.toList());

		// パーツカラム定義をパーツIDをキーにMapとして抽出
		final Map<Long, List<MwmPartsColumn>> colMap = loadRepository.getMwmPartsColumnMap(containerId);

		// パーツ子要素をパーツIDをキーにMapとして抽出
		final Map<Long, MwmPartsChildHolder> childMap = loadRepository.getMwmPartsChildMap(containerId);

		// パーツ条件定義をパーツIDをキーにMapとして抽出
		final Map<Long, List<MwmPartsCondEx>> condMap = loadRepository.getMwmPartsCondMap(containerId);
		// パーツ条件項目定義をパーツ条件IDをキーにMapとして抽出
		final Map<Long, List<MwmPartsCondItem>> condItemMap = loadRepository.getMwmPartsCondItemMap(containerId);

		// パーツ関連定義をパーツIDをキーにMapとして抽出
		final Map<Long, List<MwmPartsRelation>> assMap = loadRepository.getMwmPartsRelationMap(containerId);

		// パーツ計算式定義をパーツIDをキーにMapとして抽出
		final Map<Long, List<MwmPartsCalc>> calcMap = loadRepository.getMwmPartsCalcMap(containerId);
		// パーツ計算式項目定義、パーツ計算式有効条件定義をそれぞれパーツ計算式IDをキーにMapとして抽出
		final Map<Long, List<MwmPartsCalcItem>> calcItemMap = loadRepository.getMwmPartsCalcItemMap(containerId);
		final Map<Long, List<MwmPartsCalcEc>> calcEcMap = loadRepository.getMwmPartsCalcEcMap(containerId);

		// パーツ選択肢定義、選択肢項目を抽出
		final Map<Long, MwmPartsOption> optionMap = loadRepository.getMwmPartsOptionMap(containerId);
		final Map<Long, List<MwmOptionItem>> optionItemMap = loadRepository.getMwmOptionItemMap(containerId);

		// パーツ汎用テーブル情報を抽出
		final Map<Long, MwmPartsTableInfo> tableInfoMap = loadRepository.getMwmPartsTableInfo(containerId);

		// パーツ添付ファイル定義を抽出
		final Map<Long, List<MwmPartsAttachFile>> partsAttachFileMap = loadRepository.getMwmPartsAttachFile(containerId);

		// パーツイベント定義
		final Map<Long, List<MwmPartsEvent>> partsEventMap = loadRepository.getMwmPartsEvent(containerId);

		// コンテナの子孫パーツ（子、孫、ひ孫などすべて）
		list.forEach(p -> {
			final List<MwmPartsColumn> cols = colMap.get(p.getPartsId());
			final MwmPartsChildHolder child = childMap.get(p.getPartsId());
			final List<MwmPartsCondEx> conds = condMap.get(p.getPartsId());
			// パーツ条件IDが同じパーツ条件項目定義を抽出
			final Map<Long, List<MwmPartsCondItem>> condItems = new HashMap<Long, List<MwmPartsCondItem>>();
			final List<MwmPartsRelation> asss = assMap.get(p.getPartsId());
			final List<MwmPartsCalc> calcs = calcMap.get(p.getPartsId());
			// パーツ計算式IDが同じパーツ計算式項目定義、パーツ計算式有効条件定義を抽出
			final Map<Long, List<MwmPartsCalcItem>> calcItems = new HashMap<Long, List<MwmPartsCalcItem>>();
			final Map<Long, List<MwmPartsCalcEc>> calcEcs = new HashMap<Long, List<MwmPartsCalcEc>>();
			// パーツ選択肢定義、選択肢項目
			final MwmPartsOption option = optionMap.get(p.getPartsId());
			final List<MwmOptionItem> optionItems = isEmpty(option) || isEmpty(option.getOptionId()) ? null : optionItemMap.get(option.getOptionId());
			final MwmPartsTableInfo ti = tableInfoMap.get(p.getPartsId());
			final List<MwmPartsAttachFile> files = partsAttachFileMap.get(p.getPartsId());
			final List<MwmPartsEvent> events = partsEventMap.get(p.getPartsId());

			if (conds != null) {
				conds.stream().map(MwmPartsCondEx::getPartsCondId).forEach(k -> {
					if (condItemMap.containsKey(k))
						condItems.put(k, condItemMap.get(k));
				});
			}
			if (calcs != null) {
				calcs.stream().map(MwmPartsCalc::getPartsCalcId).forEach(k -> {
					if (calcItemMap.containsKey(k))
						calcItems.put(k, calcItemMap.get(k));
					if (calcEcMap.containsKey(k))
						calcEcs.put(k, calcEcMap.get(k));
				});
			}

			final PartsDesign d = toPartsDesign(p, root, cols, child, conds, condItems, asss, calcs, calcItems, calcEcs, option, optionItems, ti, files, events, ctx.designMap);
			ctx.designMap.put(d.partsId, d);

			if (d instanceof PartsDesignChildHolder) {
				loadChildDesign((PartsDesignChildHolder)d, ctx, false);
			}
		});
	}

	/**
	 * 子要素コンテナとその配下パーツを再帰的に読み込み
	 * @param ch コンテナ
	 * @param ctx デザイナーコンテキスト
	 * @param stopFunction それ以上Javascript関連のデータを読み込まないようにするならtrue。
	 * 						独立画面パーツはポップアップ画面として単独で使用されるため、通常のスクリプトと同じように読み込むとエレメントが存在しなかったりするので、それを抑制する。
	 */
	public void loadChildDesign(PartsDesignChildHolder ch, DesignerContext ctx, boolean stopFunction) {
		final Map<Long, PartsDesign> designMap = ctx.designMap;
		final MwmContainer srcCntr = loadRepository.getMwmContainer(ch.childContainerId);
		if (srcCntr != null) {
			// 使用しているコンテナID
			if (!ctx.containerIds.contains(ch.childContainerId))
				ctx.containerIds.add(ch.childContainerId);
			// Submit時の呼び出し関数
			if (isNotEmpty(srcCntr.getSubmitFuncName())) {
				if (ch.partsType == PartsType.STAND_ALONE) {
					stopFunction = true;
				} else {
					final SubmitFunction sf = new SubmitFunction(srcCntr);
					if (!ctx.submitFunctions.contains(sf)) {
						ctx.submitFunctions.add(sf);
					}
				}
			}
			// Load時の呼び出し関数
			if (isNotEmpty(srcCntr.getLoadFuncName())) {
				if (ch.partsType == PartsType.STAND_ALONE) {
					stopFunction = true;
				} else {
					final LoadFunction lf = new LoadFunction(srcCntr);
					if (!ctx.loadFunctions.contains(lf)) {
						ctx.loadFunctions.add(lf);
					}
				}
			}
			// 起案担当者変更時の呼び出し関数
			if (isNotEmpty(srcCntr.getChangeStartUserFuncName())) {
				final ChangeStartUserFunction f = new ChangeStartUserFunction(srcCntr);
				if (!ctx.changeStartUserFunctions.contains(f))
					ctx.changeStartUserFunctions.add(f);
			}
			ch.containerCode = srcCntr.getContainerCode();
			ch.containerName = srcCntr.getContainerName();
			ch.customCssStyle = srcCntr.getCustomCssStyle();
			ch.tableName = srcCntr.getTableName();
			ch.bgHtml = srcCntr.getBgHtml();
			ch.corporationCode = srcCntr.getCorporationCode();
			ch.partsCodeSeq = srcCntr.getPartsCodeSeq();
			ch.submitFuncName = srcCntr.getSubmitFuncName();
			ch.submitFuncParam = srcCntr.getSubmitFuncParam();
			ch.loadFuncName = srcCntr.getLoadFuncName();
			ch.loadFuncParam = srcCntr.getLoadFuncParam();
			ch.changeStartUserFuncName = srcCntr.getChangeStartUserFuncName();
			ch.changeStartUserFuncParam = srcCntr.getChangeStartUserFuncParam();
			ch.notDropTableFlag = eq(CommonFlag.ON, srcCntr.getNotDropTableFlag());

			// コンテナの子パーツ（孫パーツは含まず）
			final List<MwmPart> list = loadRepository.getMwmPartsList(ch.childContainerId);
			ch.childPartsIds.clear();
			for (MwmPart p : list) {
				if (ch.childPartsIds.indexOf(p.getPartsId()) < 0)
					ch.childPartsIds.add(p.getPartsId());
			}

			// コンテナに紐付くJavascript定義
			ch.javascripts = loadRepository.getMwmContainerJavascript(ch.childContainerId)
					.stream().map(cj -> new PartsJavascript(cj))
					.collect(Collectors.toList());

			// パーツカラム定義をパーツIDをキーにMapとして抽出
			final Map<Long, List<MwmPartsColumn>> colMap = loadRepository.getMwmPartsColumnMap(ch.childContainerId);

			// パーツ子要素をパーツIDをキーにMapとして抽出
			final Map<Long, MwmPartsChildHolder> childMap = loadRepository.getMwmPartsChildMap(ch.childContainerId);

			// パーツ条件定義をパーツIDをキーにMapとして抽出
			final Map<Long, List<MwmPartsCondEx>> condMap = loadRepository.getMwmPartsCondMap(ch.childContainerId);
			// パーツ条件項目定義をパーツ条件IDをキーにMapとして抽出
			final Map<Long, List<MwmPartsCondItem>> condItemMap = loadRepository.getMwmPartsCondItemMap(ch.childContainerId);

			// パーツ関連定義をパーツIDをキーにMapとして抽出
			final Map<Long, List<MwmPartsRelation>> assMap = loadRepository.getMwmPartsRelationMap(ch.childContainerId);

			// パーツ計算式定義をパーツIDをキーにMapとして抽出
			final Map<Long, List<MwmPartsCalc>> calcMap = loadRepository.getMwmPartsCalcMap(ch.childContainerId);
			// パーツ計算式項目定義、パーツ計算式有効条件定義をそれぞれパーツ計算式IDをキーにMapとして抽出
			final Map<Long, List<MwmPartsCalcItem>> calcItemMap = loadRepository.getMwmPartsCalcItemMap(ch.childContainerId);
			final Map<Long, List<MwmPartsCalcEc>> calcEcMap = loadRepository.getMwmPartsCalcEcMap(ch.childContainerId);
			// パーツ選択肢定義、選択肢項目を抽出
			final Map<Long, MwmPartsOption> optionMap = loadRepository.getMwmPartsOptionMap(ch.childContainerId);
			final Map<Long, List<MwmOptionItem>> optionItemMap = loadRepository.getMwmOptionItemMap(ch.childContainerId);
			// パーツ汎用テーブル情報を抽出
			final Map<Long, MwmPartsTableInfo> tableInfoMap = loadRepository.getMwmPartsTableInfo(ch.childContainerId);
			// パーツ添付ファイル定義を抽出
			final Map<Long, List<MwmPartsAttachFile>> partsAttachFileMap = loadRepository.getMwmPartsAttachFile(ch.childContainerId);
			// パーツイベント定義
			final Map<Long, List<MwmPartsEvent>> partsEventMap = loadRepository.getMwmPartsEvent(ch.childContainerId);

			// コンテナの子孫パーツ（子、孫、ひ孫などすべて）
			for (MwmPart p : list) {
				final List<MwmPartsColumn> cols = colMap.get(p.getPartsId());
				final MwmPartsChildHolder child = childMap.get(p.getPartsId());
				final List<MwmPartsCondEx> conds = condMap.get(p.getPartsId());
				// パーツ条件IDが同じパーツ条件項目定義を抽出
				final Map<Long, List<MwmPartsCondItem>> condItems = new HashMap<Long, List<MwmPartsCondItem>>();
				final List<MwmPartsRelation> asss = assMap.get(p.getPartsId());
				final List<MwmPartsCalc> calcs = calcMap.get(p.getPartsId());
				// パーツ計算式IDが同じパーツ計算式項目定義、パーツ計算式有効条件定義を抽出
				final Map<Long, List<MwmPartsCalcItem>> calcItems = new HashMap<Long, List<MwmPartsCalcItem>>();
				final Map<Long, List<MwmPartsCalcEc>> calcEcs = new HashMap<Long, List<MwmPartsCalcEc>>();
				// パーツ選択肢定義、選択肢項目
				final MwmPartsOption option = optionMap.get(p.getPartsId());
				final List<MwmOptionItem> optionItems = isEmpty(option) || isEmpty(option.getOptionId()) ? null : optionItemMap.get(option.getOptionId());

				if (conds != null) {
					conds.stream().map(MwmPartsCondEx::getPartsCondId).forEach(k -> {
						if (condItemMap.containsKey(k))
							condItems.put(k, condItemMap.get(k));
					});
				}
				if (calcs != null) {
					calcs.stream().map(MwmPartsCalc::getPartsCalcId).forEach(k -> {
						if (calcItemMap.containsKey(k))
							calcItems.put(k, calcItemMap.get(k));
						if (calcEcMap.containsKey(k))
							calcEcs.put(k, calcEcMap.get(k));
					});
				}
				// パーツ汎用テーブル情報
				final MwmPartsTableInfo ti = tableInfoMap.get(p.getPartsId());
				// パーツ添付ファイル
				List<MwmPartsAttachFile> files = partsAttachFileMap.get(p.getPartsId());
				// パーツイベント
				List<MwmPartsEvent> events = partsEventMap.get(p.getPartsId());

				final PartsDesign d = toPartsDesign(p, ch, cols, child, conds, condItems, asss, calcs, calcItems, calcEcs, option, optionItems, ti, files, events, designMap);
				designMap.put(d.partsId, d);

				if (d instanceof PartsDesignChildHolder) {
					loadChildDesign((PartsDesignChildHolder)d, ctx, stopFunction);
				}
			};
		}
	}

	/**
	 * エンティティからパーツ定義へ変換し、パーツ定義Mapへ格納。
	 * @param p パーツ情報（MWM_PARTS）
	 * @param c コンテナ情報（MWM_CONTAINER）
	 * @param child パーツ子要素(MWM_PARTS_CHILD_HOLDER)
	 * @param conds パーツ条件(MWM_PARTS_COND)
	 * @param condItems パーツ条件項目(MWM_PARTS_COND_ITEM)
	 * @param asss パーツ関連定義(MWM_PARTS_ASSOCIATION)
	 * @param calcs パーツ計算式定義(MWM_PARTS_CALC)
	 * @param calcItems パーツ計算式項目定義(MWM_PARTS_CALC_ITEM)
	 * @param calcEcs パーツ計算式有効条件定義(MWM_PARTS_CALC_EC)
	 * @param option パーツ選択肢定義
	 * @param optionItems 選択肢項目リスト
	 * @param ti パーツ汎用テーブル情報
	 * @param designMap パーツ定義Map
	 * @return
	 */
	private PartsDesign toPartsDesign(
			MwmPart p,
			PartsDesignContainer c,
			List<MwmPartsColumn> cols,
			MwmPartsChildHolder child,
			List<MwmPartsCondEx> conds,
			Map<Long, List<MwmPartsCondItem>> condItems,
			List<MwmPartsRelation> asss,
			List<MwmPartsCalc> calcs,
			Map<Long, List<MwmPartsCalcItem>> calcItems,
			Map<Long, List<MwmPartsCalcEc>> calcEcs,
			MwmPartsOption option,
			List<MwmOptionItem> optionItems,
			MwmPartsTableInfo ti,
			List<MwmPartsAttachFile> files,
			List<MwmPartsEvent> events,
			Map<Long, PartsDesign> designMap
	) {
		final Class<? extends PartsDesign> clazz = PartsUtils.toPartsDesignClass(p.getPartsType());
		final PartsDesign d = newInstance(clazz);
		d.bgColorInput = p.getBgColorInput();
		d.bgColorRefer = p.getBgColorRefer();
		d.bgHtmlCellNo = p.getBgHtmlCellNo();
		d.bgTransparentFlag = CommonFlag.ON.equals(p.getBgTransparentFlag());
		d.colLg = p.getColLg();
		d.colMd = p.getColMd();
		d.colSm = p.getColSm();
		d.colXs = p.getColXs();
		d.copyTargetFlag = CommonFlag.ON.equals(p.getCopyTargetFlag());
		d.containerId = p.getContainerId();
		d.cssClass = p.getCssClass();
		d.cssStyle = p.getCssStyle();
		d.fontBold = CommonFlag.ON.equals(p.getFontBold());
		d.fontColor = p.getFontColor();
		d.fontSize = p.getFontSize();
		d.description = p.getDescription();
		d.grantTabIndexFlag = CommonFlag.ON.equals(p.getGrantTabIndexFlag());
		d.labelText = p.getLabelText();
		d.mobileInvisibleFlag = CommonFlag.ON.equals(p.getMobileInvisibleFlag());
		d.partsCode = p.getPartsCode();
		d.partsId = p.getPartsId();
		d.partsType = p.getPartsType();
		d.requiredFlag = CommonFlag.ON.equals(p.getRequiredFlag());
		d.renderingMethod = p.getRenderingMethod() == null ? RenderingMethod.BOOTSTRAP_GRID : p.getRenderingMethod();
		d.sortOrder = p.getSortOrder();
		d.businessInfoCode = p.getBusinessInfoCode();
		d.docBusinessInfoCode = p.getDocBusinessInfoCode();
		d.version = p.getVersion();

		// 親コンテナ情報
		d.parentPartsId = c.partsId;
		d.containerId = c.containerId;
		d.designCode = PartsUtils.toDesignCode(d, c);

		// パーツ子要素
		if (d instanceof PartsDesignChildHolder && child != null) {
			PartsDesignChildHolder dc = (PartsDesignChildHolder)d;
			dc.childContainerId = child.getChildContainerId();
			dc.initRowCount = child.getInitRowCount();
			dc.minRowCount = child.getMinRowCount();
			dc.pageSize = child.getPageSize();
			dc.partsChildHolderId = child.getPartsChildHolderId();
		} else if (d instanceof PartsDesignOption) {
			PartsDesignOption dd = (PartsDesignOption)d;
			dd.optionId = isEmpty(option) ? null : option.getOptionId();
			dd.optionItems = isEmpty(optionItems) ? new ArrayList<>() : optionItems.stream().map(oi -> new PartsOptionItem(oi.getCode(), oi.getLabel())).collect(Collectors.toList()) ;
		}


		// カラム定義
		if (cols == null || cols.isEmpty())
			d.columns = d.newColumns();
		else
			d.columns = cols.stream().map(pc -> new PartsColumn(pc)).collect(Collectors.toList());

		// 条件定義
		if (conds != null) {
			d.partsConds = conds.stream().map(ec -> new PartsCond(ec)).collect(Collectors.toList());
			d.partsConds.stream().forEach(cond -> {
				// パーツ条件項目定義
				if (condItems.containsKey(cond.partsCondId))
					cond.items = condItems.get(cond.partsCondId).stream().map(ci -> new PartsCondItem(ci)).collect(Collectors.toList());
			});
		}

		// パーツ関連（関連コードをキー、対象パーツIDを値としたMap）
		if (asss != null) {
			d.relations = asss.stream().map(pa -> new PartsRelation(pa)).collect(Collectors.toList());
		}

		// パーツ計算式定義
		if (calcs != null) {
			d.partsCalcs = calcs.stream().map(e -> new PartsCalc(e)).collect(Collectors.toList());
			d.partsCalcs.stream().forEach(cal -> {
				// パーツ計算式項目定義
				if (calcItems.containsKey(cal.partsCalcId))
					cal.items = calcItems.get(cal.partsCalcId).stream().map(ci -> new PartsCalcItem(ci)).collect(Collectors.toList());
				// パーツ計算式有効条件定義
				if (calcEcs.containsKey(cal.partsCalcId))
					cal.ecs = calcEcs.get(cal.partsCalcId).stream().map(ce -> new PartsCalcEc(ce)).collect(Collectors.toList());
			});
		}

		// パーツ汎用テーブル情報
		if (ti != null) {
			if (d instanceof PartsDesignAjax) {
				((PartsDesignAjax) d).tableId = ti.getTableId();
				((PartsDesignAjax) d).tableSearchId = ti.getTableSearchId();
			}
		}

		// パーツ添付ファイル
		if (files == null)
			d.attachFiles = new ArrayList<>();
		else
			d.attachFiles = files.stream().map(f -> new PartsAttachFileEntity(f)).collect(Collectors.toList());

		// パーツイベント定義
		if (events != null) {
			d.events = events.stream().map(ev -> new PartsEvent(ev)).collect(Collectors.toList());
		}

		// 拡張情報を反映
		@SuppressWarnings("unchecked")
		final Map<String, Object> ext = toObjFromJson(p.getExtInfo(), Map.class);
		d.fillExtInfo(ext);

		// パーツ読込の後処理（パーツごと）
		// ※すべてのテーブルから読み込まれたあとの、最終調整処理用。これより後に処理がないこと！
		d.afterLoad();

		designMap.put(p.getPartsId(), d);

		return d;
	}

	/**
	 * ルートコンテナを新規生成
	 * @param corporationCode
	 * @return
	 */
	public void newRootContainer(String corporationCode, DesignerContext ctx) {
		ctx.root = new PartsDesignRootContainer();
		ctx.root.setInitValue();
		ctx.root.containerId = numbering.newPK(MwmContainer.class);
		ctx.root.containerCode = String.format("CNTR%06d", ctx.root.containerId);
		ctx.root.containerName = ctx.root.containerCode;
		ctx.root.tableName = "MWT_" + ctx.root.containerCode;
		ctx.root.bgHtml = null;
		ctx.root.corporationCode = corporationCode;
		ctx.root.partsCodeSeq = 0;
		ctx.root.fontSize = FontSize.Inherit;
		ctx.root.submitFuncName = null;
		ctx.root.submitFuncParam = null;
		ctx.root.notDropTableFlag = false;

		// ルートコンテナに親はいない
		ctx.root.parentPartsId = null;
	}
}
