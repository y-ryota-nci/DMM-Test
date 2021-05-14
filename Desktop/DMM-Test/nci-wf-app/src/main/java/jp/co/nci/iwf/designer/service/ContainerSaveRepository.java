package jp.co.nci.iwf.designer.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.designer.parts.PartsEvent;
import jp.co.nci.iwf.designer.parts.PartsJavascript;
import jp.co.nci.iwf.designer.parts.PartsRelation;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.design.PartsDesignChildHolder;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignOption;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaDataService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
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

/**
 * コンテナ単位のパーツ定義の保存リポジトリ
 */
@ApplicationScoped
public class ContainerSaveRepository extends BaseRepository {
	@Inject
	private PartsExtService extService;
	@Inject
	private NumberingService numbering;
	@Inject
	private TableMetaDataService meta;

	/**
	 * ローカルバッファ内容をDBへ反映
	 */
	public void flush() {
		em.flush();
	}

	/** パーツ定義からMWM_PARTSへ変換してインサート */
	public void insert(PartsDesign inputed) {
		// パーツの拡張情報
		final String extInfo = extService.toJsonFromFields(inputed);

		final MwmPart parts = new MwmPart();
		parts.setBgColorInput(inputed.bgColorInput);
		parts.setBgColorRefer(inputed.bgColorRefer);
		parts.setBgHtmlCellNo(inputed.bgHtmlCellNo);
		parts.setBgTransparentFlag(inputed.bgTransparentFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setCssClass(inputed.cssClass);
		parts.setCssStyle(inputed.cssStyle);
		parts.setFontBold(inputed.fontBold ? CommonFlag.ON : CommonFlag.OFF);
		parts.setFontColor(inputed.fontColor);
		parts.setFontSize(inputed.fontSize);
		parts.setColLg(inputed.colLg);
		parts.setColMd(inputed.colMd);
		parts.setColMd(inputed.colMd);
		parts.setColSm(inputed.colSm);
		parts.setColXs(inputed.colXs);
		parts.setCopyTargetFlag(inputed.copyTargetFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setContainerId(inputed.containerId);
		parts.setDeleteFlag(DeleteFlag.OFF);
		parts.setDescription(inputed.description);
		parts.setExtInfo(extInfo);
		parts.setGrantTabIndexFlag(inputed.grantTabIndexFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setLabelText(inputed.labelText);
		parts.setMobileInvisibleFlag(inputed.mobileInvisibleFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setPartsCode(inputed.partsCode);
		parts.setPartsId(inputed.partsId);
		parts.setPartsType(inputed.partsType);
		parts.setRequiredFlag(inputed.requiredFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setRenderingMethod(inputed.renderingMethod);
		parts.setSortOrder(inputed.sortOrder);
		parts.setBusinessInfoCode(inputed.businessInfoCode);
		parts.setDocBusinessInfoCode(inputed.docBusinessInfoCode);
		em.persist(parts);

		// 選択肢使用パーツの場合、パーツ選択肢定義を登録
		if (inputed instanceof PartsDesignOption) {
			insert((PartsDesignOption)inputed);
		}
	}

	/** パーツ定義を元にMWM_PARTS_OPTIONへインサート */
	private void insert(PartsDesignOption inputed) {
		if (isNotEmpty(inputed.optionId)) {
			final MwmPartsOption option = new MwmPartsOption();
			final long partsOptionId = numbering.newPK(MwmPartsOption.class);
			option.setPartsOptionId(partsOptionId);
			option.setPartsId(inputed.partsId);
			option.setOptionId(inputed.optionId);
			option.setDeleteFlag(DeleteFlag.OFF);
			em.persist(option);
		}
	}

	/** パーツ定義からMWM_PARTSへ変換してアップデート */
	public void update(PartsDesign inputed, MwmPart parts, MwmPartsOption option) {
		// パーツの拡張情報
		final String extInfo = extService.toJsonFromFields(inputed);

		parts.setBgColorInput(inputed.bgColorInput);
		parts.setBgColorRefer(inputed.bgColorRefer);
		parts.setBgHtmlCellNo(inputed.bgHtmlCellNo);
		parts.setBgTransparentFlag(inputed.bgTransparentFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setCssClass(inputed.cssClass);
		parts.setCssStyle(inputed.cssStyle);
		parts.setFontBold(inputed.fontBold ? CommonFlag.ON : CommonFlag.OFF);
		parts.setFontColor(inputed.fontColor);
		parts.setFontSize(inputed.fontSize);
		parts.setColLg(inputed.colLg);
		parts.setColMd(inputed.colMd);
		parts.setColMd(inputed.colMd);
		parts.setColSm(inputed.colSm);
		parts.setColXs(inputed.colXs);
		parts.setCopyTargetFlag(inputed.copyTargetFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setContainerId(inputed.containerId);
		parts.setDeleteFlag(DeleteFlag.OFF);
		parts.setDescription(inputed.description);
		parts.setExtInfo(extInfo);
		parts.setGrantTabIndexFlag(inputed.grantTabIndexFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setLabelText(inputed.labelText);
		parts.setMobileInvisibleFlag(inputed.mobileInvisibleFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setPartsCode(inputed.partsCode);
		parts.setPartsType(inputed.partsType);
		parts.setRequiredFlag(inputed.requiredFlag ? CommonFlag.ON : CommonFlag.OFF);
		parts.setRenderingMethod(inputed.renderingMethod);
		parts.setSortOrder(inputed.sortOrder);
		parts.setBusinessInfoCode(inputed.businessInfoCode);
		parts.setDocBusinessInfoCode(inputed.docBusinessInfoCode);
		em.merge(parts);

		// 選択肢使用パーツの場合、パーツ選択肢定義もアップデート
		if (inputed instanceof PartsDesignOption) {
			update((PartsDesignOption)inputed, option);
		}
	}

	/** パーツ定義を元にMWM_PARTS_OPTIONへ変換してアップデート */
	private void update(PartsDesignOption inputed, MwmPartsOption option) {
		if (isEmpty(option)) {
			insert(inputed);
			return;
		}
		if (isNotEmpty(inputed.optionId)) {
			option.setOptionId(inputed.optionId);
			em.merge(option);
		} else {
			delete(option);
		}
	}

	/** コンテナ定義からMWM_CONTAINERへ変換してインサート */
	public void insert(PartsDesignContainer inputed, Timestamp tableModifiedTimestamp) {
		final MwmContainer container = new MwmContainer();
		container.setBgHtml(inputed.bgHtml);
		container.setContainerCode(inputed.containerCode);
		container.setContainerId(inputed.containerId);
		container.setContainerName(inputed.containerName);
		container.setCorporationCode(inputed.corporationCode);
		container.setCustomCssStyle(inputed.customCssStyle);
		container.setTableName(inputed.tableName);
		container.setTableModifiedTimestamp(tableModifiedTimestamp);
		container.setDeleteFlag(DeleteFlag.OFF);
		container.setPartsCodeSeq(inputed.partsCodeSeq);
		container.setFontSize(inputed.fontSize);
		container.setSubmitFuncName(inputed.submitFuncName);
		container.setSubmitFuncParam(inputed.submitFuncParam);
		container.setLoadFuncName(inputed.loadFuncName);
		container.setLoadFuncParam(inputed.loadFuncParam);
		container.setChangeStartUserFuncName(inputed.changeStartUserFuncName);
		container.setChangeStartUserFuncParam(inputed.changeStartUserFuncParam);
		container.setNotDropTableFlag(inputed.notDropTableFlag ? CommonFlag.ON : CommonFlag.OFF);
		em.persist(container);
	}

	/** コンテナ定義からMWM_CONTAINERへ変換してアップデート */
	public void update(PartsDesignContainer inputed, MwmContainer container, Timestamp tableModifiedTimestamp) {
		// テーブル名が変わったらテーブル同期日時をクリア
		if (!meta.isExistTable(inputed.tableName)) {
			container.setTableSyncTimestamp(null);
		}
		container.setBgHtml(inputed.bgHtml);
		container.setContainerCode(inputed.containerCode);
		container.setContainerName(inputed.containerName);
		container.setCorporationCode(inputed.corporationCode);
		container.setCustomCssStyle(inputed.customCssStyle);
		container.setTableName(inputed.tableName);
		container.setTableModifiedTimestamp(tableModifiedTimestamp);
		container.setDeleteFlag(DeleteFlag.OFF);
		container.setPartsCodeSeq(inputed.partsCodeSeq);
		container.setFontSize(inputed.fontSize);
		container.setSubmitFuncName(inputed.submitFuncName);
		container.setSubmitFuncParam(inputed.submitFuncParam);
		container.setLoadFuncName(inputed.loadFuncName);
		container.setLoadFuncParam(inputed.loadFuncParam);
		container.setChangeStartUserFuncName(inputed.changeStartUserFuncName);
		container.setChangeStartUserFuncParam(inputed.changeStartUserFuncParam);
		container.setNotDropTableFlag(inputed.notDropTableFlag ? CommonFlag.ON : CommonFlag.OFF);
	}

	/** MWM_PARTSを物理削除 */
	public void delete(MwmPart parts) {
		if (parts != null)
			em.remove(parts);
	}

	/** MWM_PARTS_OPTIONを物理削除 */
	public void delete(MwmPartsOption option) {
		if (option != null)
			em.remove(option);
	}

	/** パーツ子要素定義からMWM_PARTS_CHILD_HOLDERへ変換してインサート */
	public void insert(PartsDesignChildHolder inputed) {
		final MwmPartsChildHolder child = new MwmPartsChildHolder();
		final long partsChildHolderId = numbering.newPK(MwmPartsChildHolder.class);
		child.setChildContainerId(inputed.childContainerId);
		child.setDeleteFlag(DeleteFlag.OFF);
		child.setInitRowCount(inputed.initRowCount);
		child.setMinRowCount(inputed.minRowCount);
		child.setPageSize(inputed.pageSize);
		child.setPartsChildHolderId(partsChildHolderId);
		child.setPartsId(inputed.partsId);
		em.persist(child);
	}

	/** パーツ子要素定義からMWM_PARTS_CHILD_HOLDERへ変換してアップデート */
	public void update(PartsDesignChildHolder inputed, MwmPartsChildHolder child) {
		child.setChildContainerId(inputed.childContainerId);
		child.setInitRowCount(inputed.initRowCount);
		child.setMinRowCount(inputed.minRowCount);
		child.setPageSize(inputed.pageSize);
		em.merge(child);
	}

	/** パーツIDを指定してMWM_PARTS_CHILD_HOLDERを削除 */
	public void deleteMwmPartsChildHolder(Set<Long> deletePartsIds) {
		if (!deletePartsIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deletePartsIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_07"));
			sql.append(toInListSql("PARTS_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** パーツカラム定義からMWM_PARTS_COLUMNへ変換しインサート */
	public void insert(long containerId, PartsColumn inputed) {
		final MwmPartsColumn org = new MwmPartsColumn();
		org.setColumnName(inputed.columnName);
		org.setColumnSize(inputed.columnSize);
		org.setColumnType(inputed.columnType);
		org.setComments(inputed.comments);
		org.setContainerId(containerId);
		org.setDecimalPoint(inputed.decimalPoint);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setPartsColumnId(numbering.newPK(MwmPartsColumn.class));
		org.setPartsId(inputed.partsId);
		org.setRoleCode(inputed.roleCode);
		org.setSortOrder(inputed.sortOrder);
		em.persist(org);
	}

	/** パーツカラム定義からMWM_PARTS_COLUMNへ変換しアップデート */
	public void update(PartsColumn inputed, MwmPartsColumn org) {
		org.setColumnName(inputed.columnName);
		org.setColumnSize(inputed.columnSize);
		org.setColumnType(inputed.columnType);
		org.setComments(inputed.comments);
		org.setDecimalPoint(inputed.decimalPoint);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setPartsId(inputed.partsId);
		org.setRoleCode(inputed.roleCode);
		org.setSortOrder(inputed.sortOrder);
		em.merge(org);
	}

	/** パーツID or パーツカラムIDに紐付くMWM_PARTS_COLUMNを一括削除 */
	public void deleteMwmPartsColumn(Set<Long> deletePartsIds, Set<Long> deletePartsColumnIds) {
		// パーツID単位
		if (!deletePartsIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deletePartsIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_08"));
			sql.append(toInListSql("PARTS_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
		// パーツカラムID単位
		if (!deletePartsColumnIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deletePartsColumnIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_08"));
			sql.append(toInListSql("PARTS_COLUMN_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** パーツ条件定義からMWM_PARTS_CONDへ変換しインサート */
	public Long insert(PartsCond inputed, Long partsId) {
		final MwmPartsCond org = new MwmPartsCond();
		org.setPartsCondId(numbering.newPK(MwmPartsCond.class));
		org.setPartsId(partsId);
		org.setPartsConditionType(inputed.partsConditionType);
		org.setCallbackFunction(inputed.callbackFunction);
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.persist(org);
		return org.getPartsCondId();
	}

	/** パーツ条件定義からMWM_PARTS_CONDへ変換しアップデート */
	public void update(PartsCond inputed, MwmPartsCond org) {
		org.setCallbackFunction(inputed.callbackFunction);
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** パーツ条件項目定義からMWM_PARTS_COND_ITEMへ変換しインサート */
	public void insert(PartsCondItem inputed, Long partsCalcId) {
		final MwmPartsCondItem org = new MwmPartsCondItem();
		org.setPartsCondItemId(numbering.newPK(MwmPartsCondItem.class));
		org.setPartsCondId(partsCalcId);
		org.setItemClass(inputed.itemClass);
		org.setCondType(inputed.condType);
		org.setOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		em.persist(org);
	}

	/** パーツ条件項目定義からMWM_PARTS_COND_ITEMへ変換しアップデート */
	public void update(PartsCondItem inputed, MwmPartsCondItem org) {
		org.setItemClass(inputed.itemClass);
		org.setCondType(inputed.condType);
		org.setOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 削除となったパーツ or 削除されたMWM_PARTS_CONDを一括削除 */
	public void deleteMwmPartsCond(Set<Long> deletePartsCondIds) {
		// パーツ条件ID単位で削除
		if (!deletePartsCondIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deletePartsCondIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_30"));
			sql.append(toInListSql("PARTS_COND_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 削除されたパーツ条件定義 or 削除されたパーツのパーツ条件項目定義 or 削除となったパーツでパーツ条件項目の対象パーツになっているMWM_PARTS_COND_ITEMを一括削除 */
	public void deleteMwmPartsCondItem(Set<Long> deletePartsCondIds, Set<Long> deletePartsIds, Set<Long> deletePartsCondItemIds) {
		// 削除されたパーツ条件定義のパーツ条件項目定義を削除
		if (!deletePartsCondIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deletePartsCondIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_32"));
			sql.append(toInListSql("PARTS_COND_ID", deletePartsCondIds.size()));
			execSql(sql.toString(), params.toArray());
		}

		// 削除されたパーツ自身が比較対象パーツとなっているパーツ条件項目定義を削除
		if (!deletePartsIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			deletePartsIds.stream().forEach(d -> params.add(String.valueOf(d)));
			params.addAll(deletePartsIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_32"));
			sql.append(" ITEM_CLASS = '3' and ");
			sql.append("(");
			sql.append(toInListSql("COND_TYPE", deletePartsIds.size()));
			sql.append(" OR ");
			sql.append(toInListSql("TARGET_PARTS_ID", deletePartsIds.size()));
			sql.append(")");
			execSql(sql.toString(), params.toArray());
		}

		// パーツ条件項目ID単位で削除
		if (!deletePartsCondItemIds.isEmpty()) {
			final List<Object> params = new ArrayList<>(deletePartsCondItemIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_32"));
			sql.append(toInListSql("PARTS_COND_ITEM_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/**
	 * コンテナIDをキーに、コンテナ配下の全テーブルから該当レコードを物理削除
	 * @param containerId コンテナID
	 * @return
	 */
	public int delete(Long containerId) {
		final Object[] params = { containerId };

		// MWM_PARTS_CHILD_HOLDER
		execSql(getSql("VD0010_04"), params);
		// MWM_PARTS_CALC_ITEM
		execSql(getSql("VD0010_05"), params);
		// MWM_PARTS_CALC_EC
		execSql(getSql("VD0010_06"), params);
		// MWM_PARTS_CALC
		execSql(getSql("VD0010_07"), params);
		// MWM_PARTS_DC
		execSql(getSql("VD0010_09"), params);
		// MWM_PARTS_OPTION
		execSql(getSql("VD0010_13"), params);
		// MWM_PARTS_COND_ITEM
		execSql(getSql("VD0010_17"), params);
		// MWM_PARTS_COND
		execSql(getSql("VD0010_18"), params);
		// MWM_PARTS
		execSql(getSql("VD0010_11"), params);
		// MWM_CONTAINER_JAVASCRIPT
		execSql(getSql("VD0010_12"), params);
		// MWM_MULTILINGUAL
		execSql(getSql("VD0010_14"), params);
		// MWM_PARTS_EVENT
		execSql(getSql("VD0010_16"), params);
		// MWM_CONTAINER
		return execSql(getSql("VD0010_15"), params);
	}

	/** MWM_PARTS_RELATIONをインサート */
	public void insertMwmPartsRelation(long partsId, PartsRelation pr) {
		MwmPartsRelation r = new MwmPartsRelation();
		r.setDeleteFlag(DeleteFlag.OFF);
		r.setPartsId(partsId);
		r.setPartsRelationId(numbering.newPK(MwmPartsRelation.class));
		r.setPartsIoType(pr.partsIoType);
		r.setColumnName(pr.columnName);
		r.setSortOrder(pr.sortOrder);
		r.setTargetPartsId(pr.targetPartsId);
		r.setWidth(pr.width);
		r.setNoChangeEventFlag(pr.noChangeEventFlag ? CommonFlag.ON : CommonFlag.OFF);
		em.persist(r);
	}

	/** MWM_PARTS_RELATIONを更新 */
	public void updateMwmPartsRelation(MwmPartsRelation org, PartsRelation pr) {
		org.setColumnName(pr.columnName);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setPartsIoType(pr.partsIoType);
		org.setSortOrder(pr.sortOrder);
		org.setTargetPartsId(pr.targetPartsId);
		org.setWidth(pr.width);
		org.setNoChangeEventFlag(pr.noChangeEventFlag ? CommonFlag.ON : CommonFlag.OFF);
		em.merge(org);
	}

	/** MWM_PARTS_RELATIONを削除 */
	public void delete(Set<MwmPartsRelation> deletes, Set<Long> deletePartsIds) {
		// パーツ単位
		for (MwmPartsRelation r : deletes) {
			em.remove(r);
		}
		em.flush();

		// 対象パーツID単位
		if (!deletePartsIds.isEmpty()) {
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_14"));
			// MiscUtils.toInListSql()を使えば簡単なのだけれど、万が一、空振りした時に全行削除は怖いので..
			sql.append(" (");
			sql.append(repeat("?", ", ", deletePartsIds.size()));
			sql.append(")");

			execSql(sql.toString(), deletePartsIds.toArray());
		}
	}

	/** 計算式定義からMWM_PARTS_CALCへ変換しインサート */
	public Long insert(PartsCalc inputed, Long partsId) {
		final MwmPartsCalc org = new MwmPartsCalc();
		org.setPartsCalcId(numbering.newPK(MwmPartsCalc.class));
		org.setPartsId(partsId);
		org.setPartsCalcName(inputed.partsCalcName);
		org.setSortOrder(inputed.sortOrder);
		org.setDefaultFlag(inputed.defaultFlag);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		org.setCallbackFunction(inputed.callbackFunction);
		em.persist(org);
		return org.getPartsCalcId();
	}

	/** 計算式定義からMWM_PARTS_CALCへ変換しアップデート */
	public void update(PartsCalc inputed, MwmPartsCalc org) {
		org.setPartsCalcName(inputed.partsCalcName);
		org.setSortOrder(inputed.sortOrder);
		org.setDefaultFlag(inputed.defaultFlag);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setCallbackFunction(inputed.callbackFunction);
		em.merge(org);
	}

	/** 計算項目定義からMWM_PARTS_CALC_ITEMへ変換しインサート */
	public void insert(PartsCalcItem inputed, Long partsCalcId) {
		final MwmPartsCalcItem org = new MwmPartsCalcItem();
		org.setPartsCalcItemId(numbering.newPK(MwmPartsCalcItem.class));
		org.setPartsCalcId(partsCalcId);
		org.setCalcItemType(inputed.calcItemType);
		org.setCalcItemValue(inputed.calcItemValue);
		org.setSortOrder(inputed.sortOrder);
		org.setForceCalcFlag(inputed.forceCalcFlag ? CommonFlag.ON : CommonFlag.OFF);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		em.persist(org);
	}

	/** 計算項目定義からMWM_PARTS_CALC_ITEMへ変換しアップデート */
	public void update(PartsCalcItem inputed, MwmPartsCalcItem org) {
		org.setCalcItemType(inputed.calcItemType);
		org.setCalcItemValue(inputed.calcItemValue);
		org.setSortOrder(inputed.sortOrder);
		org.setForceCalcFlag(inputed.forceCalcFlag ? CommonFlag.ON : CommonFlag.OFF);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** 計算式有効条件定義からMWM_PARTS_CALC_ECへ変換しインサート */
	public void insert(PartsCalcEc inputed, Long partsCalcId) {
		final MwmPartsCalcEc org = new MwmPartsCalcEc();
		org.setPartsCalcEcId(numbering.newPK(MwmPartsCalcEc.class));
		org.setPartsCalcId(partsCalcId);
		org.setEcClass(inputed.itemClass);
		org.setEcType(inputed.condType);
		org.setEcOperator(inputed.operator);
		org.setTargetLiteralVal(inputed.targetLiteralVal);
		org.setTargetPartsId(inputed.targetPartsId);
		org.setNumericFlag(StringUtils.defaultIfEmpty(inputed.numericFlag, CommonFlag.OFF));
		org.setSortOrder(inputed.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		org.setIdentifyKey(SecurityUtils.randomToken(32));
		em.persist(org);
	}

	/** 計算式有効条件定義からMWM_PARTS_CALC_ECへ変換しアップデート */
	public void update(PartsCalcEc inputed, MwmPartsCalcEc org) {
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

	/** 削除となったパーツ or 削除されたMWM_PARTS_CALCを一括削除 */
	public void deleteMwmPartsCalc(Set<Long> deletePartsCalcIds) {
		// パーツ計算式ID単位で削除
		if (!deletePartsCalcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsCalcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_16"));
			sql.append(toInListSql("PARTS_CALC_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 削除された計算項目定義 or 削除されたパーツの計算項目定義 or 削除となったパーツで計算項目の対象パーツになっているMWM_PARTS_CALCを一括削除 */
	public void deleteMwmPartsCalcItem(Set<Long> deletePartsCalcIds, Set<Long> deletePartsIds, Set<Long> deletePartsCalcItemIds) {
		// 削除されたパーツの計算項目定義を削除
		if (!deletePartsCalcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsCalcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_18"));
			sql.append(toInListSql("PARTS_CALC_ID", deletePartsCalcIds.size()));
			execSql(sql.toString(), params.toArray());
		}

		// 削除となったパーツで計算項目の対象パーツとなっている計算項目定義を削除
		if (!deletePartsIds.isEmpty()) {
			final List<Object> params = deletePartsIds.stream().map(d -> String.valueOf(d)).collect(Collectors.toList());
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_18"));
			sql.append(" CALC_ITEM_TYPE = '4'");
			sql.append(" and ");
			sql.append(toInListSql("CALC_ITEM_VALUE", deletePartsIds.size()));
			execSql(sql.toString(), params.toArray());
		}

		// パーツ計算項目ID単位で削除
		if (!deletePartsCalcItemIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsCalcItemIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_18"));
			sql.append(toInListSql("PARTS_CALC_ITEM_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** 削除された計算式有効条件 or 削除されたパーツの計算式有効条件 or 削除となったパーツで比較対象パーツIDになっているMWM_PARTS_CALC_ECを一括削除 */
	public void deleteMwmPartsCalcEc(Set<Long> deletePartsCalcIds, Set<Long> deletePartsIds, Set<Long> deletePartsCalcEcIds) {
		// 削除されたパーツの計算式有効条件定義を削除
		if (!deletePartsCalcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsCalcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_20"));
			sql.append(toInListSql("PARTS_CALC_ID", deletePartsCalcIds.size()));
			execSql(sql.toString(), params.toArray());
		}

		// 削除されたパーツ自身が比較対象パーツとなっている計算式有効条件定義を削除
		if (!deletePartsIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			deletePartsIds.stream().forEach(d -> params.add(String.valueOf(d)));
			params.addAll(deletePartsIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_20"));
			sql.append(" EC_CLASS = '3' and ");
			sql.append("(");
			sql.append(toInListSql("EC_TYPE", deletePartsIds.size()));
			sql.append(" OR ");
			sql.append(toInListSql("TARGET_PARTS_ID", deletePartsIds.size()));
			sql.append(")");
			execSql(sql.toString(), params.toArray());
		}

		// パーツ計算式有効条件ID単位で削除
		if (!deletePartsCalcEcIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsCalcEcIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_20"));
			sql.append(toInListSql("PARTS_CALC_EC_ID", params.size()));
			execSql(sql.toString(), params.toArray());
		}
	}

	/** パーツJavascriptからMWM_CONTAINER_JAVASCRIPTへ変換してインサート */
	public void insert(Long containerId, PartsJavascript js) {
		long containerJavascriptId = numbering.newPK(MwmContainerJavascript.class);
		final MwmContainerJavascript cj = new MwmContainerJavascript();
		cj.setContainerId(containerId);
		cj.setContainerJavascriptId(containerJavascriptId);
		cj.setDeleteFlag(DeleteFlag.OFF);
		cj.setJavascriptId(js.javascriptId);
		cj.setSortOrder(js.sortOrder);
		em.persist(cj);
	}

	/** パーツJavascriptからMWM_CONTAINER_JAVASCRIPTへ変換して更新 */
	public void update(MwmContainerJavascript org, PartsJavascript js) {
		org.setSortOrder(js.sortOrder);
		org.setDeleteFlag(DeleteFlag.OFF);
		em.merge(org);
	}

	/** MWM_CONTAINER_JAVASCRIPTを削除 */
	public void delete(MwmContainerJavascript cj) {
		em.remove(cj);
	}

	/** MWM_PARTS_TABLE_INFOをインサート */
	public void insertMwmPartsTableInfo(PartsDesignAjax input) {
		final long partsTableInfoId = numbering.newPK(MwmPartsTableInfo.class);
		final MwmPartsTableInfo entity = new MwmPartsTableInfo();
		entity.setDeleteFlag(DeleteFlag.OFF);
		entity.setPartsId(input.partsId);
		entity.setPartsTableInfoId(partsTableInfoId);
		entity.setTableId(input.tableId);
		entity.setTableSearchId(input.tableSearchId);
		em.persist(entity);

	}

	/** MWM_PARTS_TABLE_INFOを更新 */
	public void updateMwmPartsTableInfo(MwmPartsTableInfo current, PartsDesignAjax input) {
		current.setTableId(input.tableId);
		current.setTableSearchId(input.tableSearchId);
		current.setDeleteFlag(DeleteFlag.OFF);
		em.merge(current);
	}

	/** MWM_PARTS_TABLE_INFOを削除 */
	public void delete(MwmPartsTableInfo entity) {
		em.remove(entity);
	}

	/** MWM_PARTS_EVENTのインサート */
	public void insert(PartsEvent input, long partsId) {
		long partsEventId = numbering.newPK(MwmPartsEvent.class);
		MwmPartsEvent entity = new MwmPartsEvent();
		entity.setDeleteFlag(DeleteFlag.OFF);
		entity.setEventName(input.eventName);
		entity.setFunctionName(input.functionName);
		entity.setFunctionParameter(input.functionParameter);
		entity.setPartsId(partsId);
		entity.setPartsEventId(partsEventId);
		entity.setSortOrder(input.sortOrder);
		em.persist(entity);
	}

	/** MWM_PARTS_EVENTの更新 */
	public void update(PartsEvent input, MwmPartsEvent current) {
		current.setEventName(input.eventName);
		current.setFunctionName(input.functionName);
		current.setFunctionParameter(input.functionParameter);
		current.setSortOrder(input.sortOrder);
		current.setDeleteFlag(DeleteFlag.OFF);
	}

	/** MWM_PARTS_EVENTの削除 */
	public void deleteMwmPartsEvent(Set<Long> deletePartsIds, Set<Long> deletePartsEventId) {
		// 削除されたパーツのパーツイベント定義を削除
		if (!deletePartsIds.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsIds);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_28"));
			sql.append(toInListSql("PARTS_ID", deletePartsIds.size()));
			execSql(sql.toString(), params.toArray());
		}
		// 不要になったパーツイベント定義を削除
		if (!deletePartsEventId.isEmpty()) {
			final List<Object> params = new ArrayList<>();
			params.addAll(deletePartsEventId);
			final StringBuilder sql = new StringBuilder();
			sql.append(getSql("VD0110_28"));
			sql.append(toInListSql("PARTS_EVENT_ID", deletePartsEventId.size()));
			execSql(sql.toString(), params.toArray());
		}
	}
}
