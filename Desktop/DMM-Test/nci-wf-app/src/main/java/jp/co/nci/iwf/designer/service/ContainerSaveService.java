package jp.co.nci.iwf.designer.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainerJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsChildHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsColumn;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsEvent;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsRelation;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsTableInfo;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * コンテナ単位のパーツ定義の保存サービス
 */
@BizLogic
public class ContainerSaveService extends BaseService {
	/** パーツ定義読込リポジトリ */
	@Inject private ContainerLoadRepository loadRepository;
	/** パーツ定義保存リポジトリ */
	@Inject private ContainerSaveRepository saveRepository;
	/** 多言語対応サービス */
	@Inject private MultilingalService multi;
	/** 画面に紐付くJavascriptサービス */
	@Inject private JavascriptService jsService;

	/**
	 * 差分更新
	 * @param root
	 * @param designMap
	 */
	@Transactional
	public void save(PartsDesignRootContainer root, Map<Long, PartsDesign> designMap) {
		// 更新対象パーツをかき集める
		final List<PartsDesign> targets = root.childPartsIds.stream()
				.map(partsId -> designMap.get(partsId))
				.filter(d -> d != null)
				.peek(d -> d.beforeSave())	// パーツごとの更新前処理
				.collect(Collectors.toList());

		// MWM_PARTS
		// 戻り値は今回削除されたパーツのIDリスト。
		// 後続処理の各テーブルは必ずこのパーツIDを使用しているレコードを削除すること
		final Set<Long> deletePartsIds = saveMwmParts(root, targets);

		// MWM_PARTS_CHILD_HOLDER
		saveMwmPartsChildHolder(targets, deletePartsIds);

		// MWM_PARTS_COND
		saveMwmPartsCond(root.containerId, targets, deletePartsIds);

		// MWM_PARTS_CALC
		saveMwmPartsCalc(root.containerId, targets, deletePartsIds);

		// MWM_PARTS_COLUMN
		saveMwmPartsColumn(root.containerId, targets, deletePartsIds);

		// MWM_PARTS_RELATION
		saveMwmPartsRelation(root.containerId, targets, deletePartsIds);

		// MWM_PARTS_TABLE_INFO
		saveMwmPartsTableInfo(root.containerId, targets, deletePartsIds);

		// MWM_CONTAINER_JAVASCRIPT
		saveMwmContainerJavascript(root);

		// MWM_PARTS_EVENT
		saveMwmPartsEvent(root.containerId, targets, deletePartsIds);

		// MWM_CONTAINER
		// テーブル同期日時を正しく更新するため、saveMwmPartsColumn()よりあとに実行すること
		saveMwmContainer(root);

		// キャッシュをクリア
		clearCache(root);
	}

	/** MWM_PARTS_EVENTを差分更新 */
	private void saveMwmPartsEvent(Long containerId, List<PartsDesign> targets, Set<Long> deletePartsIds) {
		// コンテナに紐付くパーツイベントを抽出し、PartsIdをキーにMap化
		final Map<Long, List<MwmPartsEvent>> allMap =
				loadRepository.getMwmPartsEvent(containerId);

		// 差分更新
		for (PartsDesign d : targets) {
			List<MwmPartsEvent> currentList = allMap.remove(d.partsId);
			for (PartsEvent input : d.events) {
				if (isEmpty(input.eventName)) {
					continue;
				}
				if (currentList == null || currentList.isEmpty()) {
					saveRepository.insert(input, d.partsId);
				}
				else {
					Map<String, MwmPartsEvent> currents = currentList
							.stream()
							.collect(Collectors.toMap(MwmPartsEvent::getEventName, c -> c));

					MwmPartsEvent current = currents.remove(input.eventName);
					if (current == null)
						saveRepository.insert(input, d.partsId);
					else
						saveRepository.update(input, current);
				}
			}
		}

		// パーツは存在しているが、イベント定義として不要になった項目
		final Set<Long> deletePartsEventId = allMap.values().stream()
				.flatMap(x -> x.stream())
				.map(x -> x.getPartsEventId())
				.collect(Collectors.toSet());

		// 不要物を一括削除
		saveRepository.deleteMwmPartsEvent(deletePartsIds, deletePartsEventId);
	}

	/** MWM_PARTS_TABLE_INFOを差分更新 */
	private void saveMwmPartsTableInfo(Long containerId, List<PartsDesign> targets, Set<Long> deletePartsIds) {
		Map<Long, MwmPartsTableInfo> map = loadRepository.getMwmPartsTableInfo(containerId);

		// 差分更新
		for (PartsDesign target : targets) {
			if (target instanceof PartsDesignAjax) {
				final PartsDesignAjax input = (PartsDesignAjax)target;
				final MwmPartsTableInfo current = map.remove(input.partsId);
				if (current == null)
					saveRepository.insertMwmPartsTableInfo(input);
				else
					saveRepository.updateMwmPartsTableInfo(current, input);
			}
		}
		// 残余を削除
		for (MwmPartsTableInfo entity : map.values()) {
			saveRepository.delete(entity);
		}
	}

	/** MWM_CONTAINER_JAVASCRIPTの差分更新 */
	private void saveMwmContainerJavascript(PartsDesignRootContainer root) {
		// コンテナに紐付くコンテナJavascriptを抽出し、JavascriptIdをキーにMap化
		final Map<Long, MwmContainerJavascript> map = loadRepository.getMwmContainerJavascript(root.containerId)
				.stream()
				.collect(Collectors.toMap(MwmContainerJavascript::getJavascriptId, cj -> cj));

		// 差分更新
		for (PartsJavascript js : root.javascripts) {
			final MwmContainerJavascript org = map.remove(js.javascriptId);
			if (org == null)
				saveRepository.insert(root.containerId, js);
			else
				saveRepository.update(org, js);
		}
		// 残余は使わなくなったJavascript参照なので、削除
		for (MwmContainerJavascript cj : map.values()) {
			saveRepository.delete(cj);
		}
	}

	/** キャッシュをクリア */
	private void clearCache(PartsDesignRootContainer root) {
		// 画面Javascript
		// ・処理前に画面Javascriptを構成していたコンテナ群と現在のコンテナ群は
		// 　変わっているケースがあり、このため任意のコンテナがどの画面に影響があったかを
		// 　判断するのは困難だから、すなおにキャッシュ全クリアだ
		jsService.clear();
	}

	/** MWM_PARTS_RELATIONを差分更新  */
	private void saveMwmPartsRelation(long containerId, List<PartsDesign> targets, Set<Long> deletePartsIds) {
		// コンテナIDに紐付く全パーツ関連定義をMapで抽出
		final Map<Long, List<MwmPartsRelation>> currentsAll = loadRepository.getMwmPartsRelationMap(containerId);

		final Set<MwmPartsRelation> deletes = new HashSet<>();

		// 差分更新
		Map<String, MwmPartsRelation> currents = new HashMap<>();;
		for (PartsDesign design : targets) {
			// パーツのパーツ関連定義をMapから抜き出す（ユニークキーはパーツID＋対象パーツID＋カラム名）
			if (currentsAll.containsKey(design.partsId)) {
				currents = currentsAll.get(design.partsId).stream()
						.collect(Collectors.toMap(pr -> toPartsRelationKey(pr), pr -> pr));
			} else {
				currents.clear();
			}

			if (design.relations != null) {
				for (PartsRelation pr : design.relations) {
					// ユニークキーはパーツID＋対象パーツID＋カラム名
					String key = toPartsRelationKey(design, pr);
					MwmPartsRelation current = currents.remove(key);
					if (current == null)
						saveRepository.insertMwmPartsRelation(design.partsId, pr);
					else
						saveRepository.updateMwmPartsRelation(current, pr);
				}
			}
			// パーツは存在しているが、関連コードとして不要になった項目
			if (currents != null && !currents.isEmpty())
				deletes.addAll(currents.values());
		}

		// 不要物を一括削除
		saveRepository.delete(deletes, deletePartsIds);
	}

	/** パーツ連携定義をキー文字列化 */
	private String toPartsRelationKey(PartsDesign d, PartsRelation pr) {
		return join(d.partsId, "_", pr.targetPartsId, "_", pr.columnName);
	}

	/** パーツ連携定義をキー文字列化 */
	private String toPartsRelationKey(MwmPartsRelation pr) {
		return join(pr.getPartsId(), "_", pr.getTargetPartsId(), "_", pr.getColumnName());
	}

	/** MWM_PARTS_CALC・MWM_PARTS_CALC_ITEM・MWM_PARTS_CALC_ECを差分更新  */
	private void saveMwmPartsCalc(long containerId, List<PartsDesign> targets, Set<Long> deletePartsIds) {
		// コンテナIDに紐付く全パーツの計算式定義、計算項目定義、計算条件定義をMapで抽出
		final Map<Long, MwmPartsCalc> calcMap = loadRepository.getMwmPartsCalcMapByCalcId(containerId);
		final Map<Long, MwmPartsCalcItem> itemMap = loadRepository.getMwmPartsCalcItemMapByItemId(containerId);
		final Map<Long, MwmPartsCalcEc> ecMap = loadRepository.getMwmPartsCalcEcMapByEcId(containerId);
		// 削除済みのパーツの計算式定義の計算式ID一覧を取得
		final Set<Long> deletePartsCalcIds = new HashSet<>();
		if (!deletePartsIds.isEmpty()) {
			deletePartsCalcIds.addAll( loadRepository.getPartsCalcIdListByPartsId(deletePartsIds) );
		}

		// 差分更新
		for (PartsDesign design : targets) {
			// パーツ計算式定義の差分更新
			Long partsCalcId = null;
			for (PartsCalc calc : design.partsCalcs) {
				final MwmPartsCalc calcOrg = calcMap.remove(calc.partsCalcId);
				if (calcOrg == null) {
					partsCalcId = saveRepository.insert(calc, design.partsId);
				} else {
					saveRepository.update(calc, calcOrg);
					partsCalcId = calcOrg.getPartsCalcId();
				}
				// 多言語対応(計算式名)
				multi.save("MWM_PARTS_CALC", partsCalcId, "PARTS_CALC_NAME", calc.partsCalcName);

				// パーツ計算項目定義の差分更新
				for (PartsCalcItem item : calc.items) {
					final MwmPartsCalcItem org = itemMap.remove(item.partsCalcItemId);
					if (org == null)
						saveRepository.insert(item, partsCalcId);
					else
						saveRepository.update(item, org);
				}
				// パーツ計算式有効条件定義の差分更新
				for (PartsCalcEc ec : calc.ecs) {
					final MwmPartsCalcEc org = ecMap.remove(ec.partsCalcEcId);
					if (org == null)
						saveRepository.insert(ec, partsCalcId);
					else
						saveRepository.update(ec, org);
				}
			}
		}

		// 不要になった計算式定義の計算式IDをかき集める
		final Set<Long> allDeletePartsCalcIds = new HashSet<>();
		allDeletePartsCalcIds.addAll(deletePartsCalcIds);
		allDeletePartsCalcIds.addAll(calcMap.keySet());
		// 計算式定義を一括削除
		saveRepository.deleteMwmPartsCalc(allDeletePartsCalcIds);
		// 多言語対応している「計算式名」を一つずつ削除
		for (Long partsCalcId : allDeletePartsCalcIds) {
			multi.physicalDelete("MWM_PARTS_CALC", partsCalcId, "PARTS_CALC_NAME");
		}

		// 計算項目定義、計算式有効条件定義を一括削除
		saveRepository.deleteMwmPartsCalcItem(deletePartsCalcIds, deletePartsIds, itemMap.keySet());
		saveRepository.deleteMwmPartsCalcEc(deletePartsCalcIds, deletePartsIds, ecMap.keySet());
	}

	/** MWM_PARTS_COND・MWM_PARTS_COND_ITEMを差分更新  */
	private void saveMwmPartsCond(long containerId, List<PartsDesign> targets, Set<Long> deletePartsIds) {
		// コンテナIDに紐付く全パーツのパーツ条件定義を抽出し、ユニークキー(パーツID + パーツ条件区分)をキーにMap化
		final Map<String, MwmPartsCond> condMap = loadRepository.getMwmPartsCondList(containerId).stream()
				.collect(Collectors.toMap(pc -> toPartsCondUniqueKey(pc) ,pc -> pc));
		// コンテナIDに紐付く全パーツのパーツ条件項目定義をパーツ条件項目IDでMapで抽出
		final Map<Long, MwmPartsCondItem> itemMap = loadRepository.getMwmPartsCondItemMapByItemId(containerId);
		// 削除済みのパーツのパーツ条件定義のパーツ条件ID一覧を取得
		final Set<Long> deletePartsCondIds = new HashSet<>();
		if (!deletePartsIds.isEmpty()) {
			deletePartsCondIds.addAll( loadRepository.getPartsCondIdListByPartsId(deletePartsIds) );
		}

		// 差分更新
		for (PartsDesign design : targets) {
			// パーツ条件定義の差分更新
			Long partsCondId = null;
			for (PartsCond cond : design.partsConds) {
				// パーツ条件定義のユニークキー(パーツID + パーツ条件区分)を取得
				String key = toPartsCondUniqueKey(design, cond);
				final MwmPartsCond condOrg = condMap.remove(key);
				if (condOrg == null) {
					partsCondId = saveRepository.insert(cond, design.partsId);
				} else {
					saveRepository.update(cond, condOrg);
					partsCondId = condOrg.getPartsCondId();
				}

				// パーツ条件項目定義の差分更新
				for (PartsCondItem item : cond.items) {
					final MwmPartsCondItem org = itemMap.remove(item.partsCondItemId);
					if (org == null)
						saveRepository.insert(item, partsCondId);
					else
						saveRepository.update(item, org);
				}
			}
		}

		// 不要となったパーツ条件定義を一括削除
		// パーツは存在しているが、パーツ条件定義として不要になった項目
		deletePartsCondIds.addAll(condMap.values().stream().map(MwmPartsCond::getPartsCondId).collect(Collectors.toList()));
		saveRepository.deleteMwmPartsCond(deletePartsCondIds);
		// 不要となったパーツ条件項目定義を一括削除
		saveRepository.deleteMwmPartsCondItem(deletePartsCondIds, deletePartsIds, itemMap.keySet());
	}

	/** パーツ条件定義のユニークキーをキー文字列化 */
	private String toPartsCondUniqueKey(MwmPartsCond pc) {
		return join(pc.getPartsId(), "_", pc.getPartsConditionType());
	}

	/** パーツ条件定義のユニークキーをキー文字列化 */
	private String toPartsCondUniqueKey(PartsDesign d, PartsCond pc) {
		return join(d.partsId, "_", pc.partsConditionType);
	}

	/** MWM_PARTS_COLUMNを差分更新 */
	private void saveMwmPartsColumn(long containerId, List<PartsDesign> targets, Set<Long> deletePartsIds) {
		// コンテナIDに紐付く全パーツカラム定義を抽出
		final Map<String, MwmPartsColumn> mpcMap = loadRepository
				.getMwmPartsColumnList(containerId)
				.stream()
				.collect(Collectors.toMap(MwmPartsColumn::getColumnName, p -> p));

		// パーツXを削除して、「他パーツのカラム名」を「パーツXのカラム名」にリネームしようとすると
		// 一時的に同一カラムが同時に存在してしまい、一意制約違反となってしまう。
		// このため、先に削除されるパーツXのカラムを削除することで一制約違反を回避する
		final Map<String, MwmPartsColumn> colNameMap = new HashMap<>(mpcMap);
		for (PartsDesign design : targets) {
			for (PartsColumn inputed : design.columns) {
				colNameMap.remove(inputed.columnName);
			}
		}
		final Set<Long> deletePartsColumnIds = colNameMap.values().stream()
				.map(MwmPartsColumn::getPartsColumnId)
				.collect(Collectors.toSet());
		saveRepository.deleteMwmPartsColumn(deletePartsIds, deletePartsColumnIds);

		// 差分更新
		for (PartsDesign design : targets) {
			for (PartsColumn inputed : design.columns) {
				final MwmPartsColumn org = mpcMap.remove(inputed.columnName);
				if (org == null)
					saveRepository.insert(design.containerId, inputed);
				else
					saveRepository.update(inputed, org);
			}
		}
	}

	/** MWM_PARTS_CHILD_HOLDERを差分更新 */
	private void saveMwmPartsChildHolder(List<PartsDesign> targets, Set<Long> deletePartsIds) {
		// 差分更新
		for (PartsDesign inputed : targets) {
			if (inputed instanceof PartsDesignChildHolder) {
				final MwmPartsChildHolder child = loadRepository.getMwmPartsChild(inputed.partsId);
				if (child == null)
					saveRepository.insert((PartsDesignChildHolder)inputed);
				else
					saveRepository.update((PartsDesignChildHolder)inputed, child);
			}
		}
		// 不要物を削除
		saveRepository.deleteMwmPartsChildHolder(deletePartsIds);
	}

	/** MWM_PARTSの差分更新。削除されたパーツIDのリストを返す */
	private Set<Long> saveMwmParts(PartsDesignRootContainer root, List<PartsDesign> targets) {

		// 当コンテナが直接管理するパーツの保存（＝コンテナの子パーツのみ、孫パーツは含まない）
		final Map<Long, MwmPart> orgPartsMap = loadRepository.getMwmPartsList(root.containerId)
				.stream()
				.collect(Collectors.toMap(MwmPart::getPartsId, p -> p));

		final Map<Long, MwmPartsOption> orgPartsOptionMap = loadRepository.getMwmPartsOptionMap(root.containerId);

		// 差分更新
		for (PartsDesign inputed : targets) {
			final MwmPart org = orgPartsMap.remove(inputed.partsId);
			if (org == null)
				saveRepository.insert(inputed);
			else
				saveRepository.update(inputed, org, orgPartsOptionMap.remove(inputed.partsId));

			// 多言語対応
			multi.save("MWM_PARTS", inputed.partsId, "LABEL_TEXT", inputed.labelText);
			multi.save("MWM_PARTS", inputed.partsId, "DESCRIPTION", inputed.description);
		}

		// 残余は使わなくなったパーツなので、削除
		final Set<Long> deletePartsIds = new HashSet<>(orgPartsMap.size());
		for (MwmPart parts : orgPartsMap.values()) {
			saveRepository.delete(parts);

			// 多言語対応
			multi.physicalDelete("MWM_PARTS", parts.getPartsId(), "LABEL_TEXT");
			multi.physicalDelete("MWM_PARTS", parts.getPartsId(), "DESCRIPTION");

			deletePartsIds.add(parts.getPartsId());
		}
		for (MwmPartsOption option : orgPartsOptionMap.values()) {
			saveRepository.delete(option);
		}
		return deletePartsIds;
	}

	/** MWM_CONTAINERを差分更新 */
	private void saveMwmContainer(PartsDesignRootContainer root) {
		// パーツカラム定義の最終更新日時の最大値を求め、これをテーブル定義更新日時とする
		Timestamp tableModified = loadRepository.getMwmPartsColumnList(root.containerId).stream()
				.map(MwmPartsColumn::getTimestampUpdated)
				.filter(tm -> tm != null)
				.max((t1, t2) -> MiscUtils.compareTo(t1, t2))
				.orElse(timestamp());

		// ルートコンテナの保存
		final MwmContainer org = loadRepository.getMwmContainer(root.containerId);
		if (org == null)
			saveRepository.insert(root, tableModified);
		else  if (!eq(org.getVersion(), root.containerVersion))
			throw new AlreadyUpdatedException();
		else
			saveRepository.update(root, org, tableModified);

		// 多言語対応
		multi.save("MWM_PARTS", root.containerId, "CONTAINER_NAME", root.containerName);
	}
}
