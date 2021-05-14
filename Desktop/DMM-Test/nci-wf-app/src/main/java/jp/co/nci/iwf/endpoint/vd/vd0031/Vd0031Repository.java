package jp.co.nci.iwf.endpoint.vd.vd0031;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.util.SecurityUtils;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;

/**
 * 画面定義設定リポジトリ
 */
@ApplicationScoped
public class Vd0031Repository extends BaseRepository {
	@Inject
	private NumberingService numbering;

	/** 有効なコンテナ定義を抽出 */
	public List<MwmContainer> getMwmContainers(String corporationCode, String localeCode) {
		final Object[] params = {
				localeCode,
				corporationCode,
		};
		return select(MwmContainer.class, getSql("VD0031_01"), params);
	}

	/** 有効なコンテナ定義を抽出 */
	public List<MwmContainer> getMwmContainers(long screenId, String localeCode) {
		final Object[] params = {
				localeCode,
				screenId,
		};
		return select(MwmContainer.class, getSql("VD0031_00"), params);
	}

	/** インサート */
	public long insert(MwmScreen input, String corporationCode) {
		long screenId = numbering.newPK(MwmScreen.class);
		input.setScreenId(screenId);
		input.setScreenCode(String.format("SCR%04d", screenId));
		input.setCorporationCode(corporationCode);
		input.setDeleteFlag(DeleteFlag.OFF);
		em.persist(input);
		em.flush();
		return screenId;
	}

	/** 更新 */
	public long update(MwmScreen input, MwmScreen org) {
		org.setContainerId(input.getContainerId());
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setScratchFlag(input.getScratchFlag());
		org.setScreenCode(input.getScreenCode());
		org.setScreenName(input.getScreenName());
		org.setScreenCustomClass(input.getScreenCustomClass());
		org.setSubmitFuncName(input.getSubmitFuncName());
		org.setSubmitFuncParam(input.getSubmitFuncParam());
		org.setLoadFuncName(input.getLoadFuncName());
		org.setLoadFuncParam(input.getLoadFuncParam());
		org.setChangeStartUserFuncName(input.getChangeStartUserFuncName());
		org.setChangeStartUserFuncParam(input.getChangeStartUserFuncParam());
		org.setVersion(input.getVersion());
		em.merge(org);

		return org.getScreenId();
	}

	/** コンテナIDの存在チェック */
	public boolean existContainer(Long containerId, String corporationCode) {
		final Object[] params = { containerId, corporationCode };
		return count(getSql("VD0031_02"), params) > 0;
	}

	/** 指定ScreenID以外でスクリーンコードが使用されているか */
	public boolean isDuplicateScreenCode(String corporationCode, String screenCode, long screenId) {
		final Object[] params = { corporationCode, screenCode, screenId };
		return count(getSql("VD0031_03"), params) > 0;
	}

	/** 指定ScreenID以外でコンテナIDを使っているか */
	public boolean isDuplicateContainerId(Long containerId, long screenId) {
		final Object[] params = { containerId, screenId };
		return count(getSql("VD0031_04"), params) > 0;
	}

	/** 画面パーツ条件からMWM_SCREEN_PARTS_CONDへ変換しインサート */
	public Long insert(PartsCond inputed, Long screenId, Long partsId) {
		final MwmScreenPartsCond org = new MwmScreenPartsCond();
		org.setScreenPartsCondId(numbering.next("MWM_SCREEN_PARTS_COND", "SCREEN_PARTS_COND_ID"));
		org.setScreenId(screenId);
		org.setPartsId(partsId);
		org.setPartsConditionType(inputed.partsConditionType);
		org.setCallbackFunction(inputed.callbackFunction);
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.persist(org);
		return org.getScreenPartsCondId();
	}

	/** 画面パーツ条件からMWM_SCREEN_PARTS_CONDへ変更内容を設定しアップデート */
	public Long update(PartsCond inputed, MwmScreenPartsCond org) {
		org.setPartsConditionType(inputed.partsConditionType);
		org.setCallbackFunction(inputed.callbackFunction);
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
		return org.getScreenPartsCondId();
	}

	/** 画面パーツ条件項目からMWM_SCREEN_PARTS_COND_ITEMへ変換しインサート */
	public void insert(PartsCondItem inputed, Long screenPartsCondId) {
		final MwmScreenPartsCondItem org = new MwmScreenPartsCondItem();
		org.setScreenPartsCondItemId(numbering.next("MWM_SCREEN_PARTS_COND_ITEM", "SCREEN_PARTS_COND_ITEM_ID"));
		org.setScreenPartsCondId(screenPartsCondId);
		org.setItemClass(inputed.itemClass);
		org.setCondType(inputed.condType);
		org.setOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		em.persist(org);
	}

	/** 画面パーツ条件項目からMWM_SCREEN_CALC_ITEMへ変更内容を設定しアップデート */
	public void update(PartsCondItem inputed, MwmScreenPartsCondItem org) {
		org.setItemClass(inputed.itemClass);
		org.setCondType(inputed.condType);
		org.setOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 削除された画面パーツ条件定義を一括削除 */
	public void deleteMwmScreenPartsCond(Set<Long> deleteScreenPartsCondIds) {
		// 画面パーツ条件ID単位で削除
		if (!deleteScreenPartsCondIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenPartsCondIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_15"));
			sql.append(toInListSql("SCREEN_PARTS_COND_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 削除された画面パーツ条件定義の画面パーツ条件項目定義 or 削除された画面パーツ条件項目定義を一括削除 */
	public void deleteMwmScreenPartsCondItem(Set<Long> deleteScreenPartsCondIds, Set<Long> deleteScreenPartsCondItemIds) {
		// 削除された画面パーツ条件定義に紐付く画面パーツ条件項目定義を一括削除
		if (!deleteScreenPartsCondIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenPartsCondIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_16"));
			sql.append(toInListSql("SCREEN_PARTS_COND_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}

		// 削除された画面パーツ条件項目定義を一括削除
		if (!deleteScreenPartsCondItemIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenPartsCondItemIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_16"));
			sql.append(toInListSql("SCREEN_PARTS_COND_ITEM_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 計算式からMWM_SCREEN_CALCへ変換しインサート */
	public Long insert(PartsCalc inputed, Long screenId, Long partsId) {
		final MwmScreenCalc org = new MwmScreenCalc();
		org.setScreenCalcId(numbering.next("MWM_SCREEN_CALC", "SCREEN_CALC_ID"));
		org.setScreenId(screenId);
		org.setPartsId(partsId);
		org.setPartsCalcName(inputed.partsCalcName);
		org.setSortOrder(inputed.sortOrder);
		org.setDefaultFlag(inputed.defaultFlag);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		org.setCallbackFunction(inputed.callbackFunction);
		em.persist(org);
		return org.getScreenCalcId();
	}

	/** 計算式からMWM_SCREEN_CALCへ変更内容を設定しアップデート */
	public Long update(PartsCalc inputed, MwmScreenCalc org) {
		org.setPartsCalcName(inputed.partsCalcName);
		org.setSortOrder(inputed.sortOrder);
		org.setDefaultFlag(inputed.defaultFlag);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setCallbackFunction(inputed.callbackFunction);
		em.merge(org);
		return org.getScreenCalcId();
	}

	/** 計算項目からMWM_SCREEN_CALC_ITEMへ変換しインサート */
	public void insert(PartsCalcItem inputed, Long screenCalcId) {
		final MwmScreenCalcItem org = new MwmScreenCalcItem();
		org.setScreenCalcItemId(numbering.next("MWM_SCREEN_CALC_ITEM", "SCREEN_CALC_ITEM_ID"));
		org.setScreenCalcId(screenCalcId);
		org.setCalcItemType(inputed.calcItemType);
		org.setCalcItemValue(inputed.calcItemValue);
		org.setSortOrder(inputed.sortOrder);
		org.setForceCalcFlag(inputed.forceCalcFlag ? CommonFlag.ON : CommonFlag.OFF);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		em.persist(org);
	}

	/** 計算項目からMWM_SCREEN_CALC_ITEMへ変更内容を設定しアップデート */
	public void update(PartsCalcItem inputed, MwmScreenCalcItem org) {
		org.setCalcItemType(inputed.calcItemType);
		org.setCalcItemValue(inputed.calcItemValue);
		org.setSortOrder(inputed.sortOrder);
		org.setForceCalcFlag(inputed.forceCalcFlag ? CommonFlag.ON : CommonFlag.OFF);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 計算式有効条件からMWM_SCREEN_CALC_ECへ変換しインサート */
	public void insert(PartsCalcEc inputed, Long screenCalcId) {
		final MwmScreenCalcEc org = new MwmScreenCalcEc();
		org.setScreenCalcEcId(numbering.next("MWM_SCREEN_CALC_EC", "SCREEN_CALC_EC_ID"));
		org.setScreenCalcId(screenCalcId);
		org.setEcClass(inputed.itemClass);
		org.setEcType(inputed.condType);
		org.setEcOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.persist(org);
	}

	/** 計算式有効条件からMWM_SCREEN_CALC_ECへ変更内容を設定しアップデート */
	public void update(PartsCalcEc inputed, MwmScreenCalcEc org) {
		org.setEcClass(inputed.itemClass);
		org.setEcType(inputed.condType);
		org.setEcOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 削除されたMWM_SCREEN_CALCを一括削除 */
	public void deleteMwmScreenCalc(Set<Long> deleteScreenCalcIds) {
		// パーツ計算式ID単位で削除
		if (!deleteScreenCalcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenCalcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_09"));
			sql.append(toInListSql("SCREEN_CALC_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 削除された計算式定義の計算項目定義 or 削除された計算項目定義のMWM_SCREEN_CALC_ITEMを一括削除 */
	public void deleteMwmScreenCalcItem(Set<Long> deleteScreenCalcIds, Set<Long> deleteScreenCalcItemIds) {
		// 削除された計算式定義に紐付く計算式項目定義を一括削除
		if (!deleteScreenCalcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenCalcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_11"));
			sql.append(toInListSql("SCREEN_CALC_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}

		// 削除された計算式項目定義を一括削除
		if (!deleteScreenCalcItemIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenCalcItemIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_11"));
			sql.append(toInListSql("SCREEN_CALC_ITEM_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 削除された計算式定義の計算式有効条件 or 削除されたパーツの計算式有効条件のMWM_SCREEN_CALC_ECを一括削除 */
	public void deleteMwmScreenCalcEc(Set<Long> deleteScreenCalcIds, Set<Long> deleteScreenCalcEcIds) {
		// 削除された計算式定義に紐付く計算式有効条件定義を一括削除
		if (!deleteScreenCalcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenCalcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_13"));
			sql.append(toInListSql("SCREEN_CALC_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}

		// 削除された計算式有効条件定義を一括削除
		if (!deleteScreenCalcEcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deleteScreenCalcEcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0031_13"));
			sql.append(toInListSql("SCREEN_CALC_EC_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	public List<PartsJavascript> getScreenJavascript(Long screenId) {
		final Object[] params = { screenId };
		return select(MwmScreenJavascript.class, getSql("VD0031_14"), params)
			.stream()
			.map(e -> new PartsJavascript(e))
			.collect(Collectors.toList());
	}

	public Map<Long, MwmScreenJavascript> getMwmScreenJavascript(long screenId) {
		final Object[] params = { screenId };
		return select(MwmScreenJavascript.class, getSql("VD0031_14"), params)
				.stream()
				.collect(Collectors.toMap(e -> e.getJavascriptId(), e -> e));
	}

	public long insert(PartsJavascript pj, long screenId) {
		final long screenJavascriptId = numbering.newPK(MwmScreenJavascript.class);
		MwmScreenJavascript sj = new MwmScreenJavascript();
		sj.setDeleteFlag(DeleteFlag.OFF);
		sj.setJavascriptId(pj.javascriptId);
		sj.setScreenId(screenId);
		sj.setScreenJavascriptId(screenJavascriptId);
		sj.setSortOrder(pj.sortOrder);
		em.persist(sj);
		return screenJavascriptId;
	}

	public void update(PartsJavascript pj, MwmScreenJavascript sj) {
		sj.setDeleteFlag(DeleteFlag.OFF);
		sj.setSortOrder(pj.sortOrder);
	}

	public void delete(MwmScreenJavascript sj) {
		em.remove(sj);
	}
}
