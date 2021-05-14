package jp.co.nci.iwf.designer.service.download;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.CodeBook;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwvScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmAccessibleScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryAuthority;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainerJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmDefaultBlockDisplay;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmJavascriptHistory;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsAttachFile;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsChildHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsColumn;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsDc;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsEvent;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsRelation;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsSequenceSpec;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsTableInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreen;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcEc;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenCalcItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenJavascript;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCond;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenPartsCondItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessMenu;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableAuthority;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearch;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearchColumn;

/**
 * 画面定義ダウンロードのリポジトリ
 */
@ApplicationScoped
public class ScreenDownloadRepository extends BaseRepository implements CodeBook {
	private static final String TABLE_NAME = quotePattern("{TABLE_NAME}");
	private static final String FILTER_BY_PK = quotePattern("{FILTER_BY_PK}");
	private static final String PK_COL_NAME = quotePattern("{PK_COL_NAME}");
	@Inject private SessionHolder sessionHolder;

	/** 画面IDに紐付くコンテナIDと、その配下コンテナIDを抽出 */
	public Long[] getContainerIdsByScreenId(Long screenId) {
		// 画面IDに紐付くコンテナと、その配下コンテナを抽出
		final Object[] params = { screenId };
		List<ScreenContainerEntity> list = select(ScreenContainerEntity.class, getSql("UP0001_02"), params);

		// コンテナIDだけを抜き出し
		final Set<Long> containerIds = new HashSet<>();
		list.forEach(e -> {
			if (!containerIds.contains(e.containerId))
				containerIds.add(e.containerId);
			if (e.childContainerId != null && !containerIds.contains(e.childContainerId))
				containerIds.add(e.childContainerId);
		});
		return containerIds.toArray(new Long[containerIds.size()]);
	}

	/** 画面IDに紐付く画面プロセス定義IDを抽出 */
	public Long[] getScreenProcessIdsByScreenId(Long screenId) {
		// 画面IDに紐付く画面プロセス定義を抽出
		final Object[] params = { screenId, sessionHolder.getLoginInfo().getLocaleCode() };
		final Set<Long> screenProcessIds = select(MwvScreenProcessDef.class, getSql("UP0001_03"), params)
				.stream()
				.map(e -> e.screenProcessId)
				.collect(Collectors.toSet());
		return screenProcessIds.toArray(new Long[screenProcessIds.size()]);
	}


	public List<MwmPart> getMwmParts(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_PARTS")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPart.class, sql, containerIds);
	}

	public List<MwmPartsCalc> getMwmPartsCalc(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_CALC")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsCalc.class, sql, containerIds);
	}

	public List<MwmPartsChildHolder> getMwmPartsChildHolder(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_CHILD_HOLDER")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsChildHolder.class, sql, containerIds);
	}

	public List<MwmPartsColumn> getMwmPartsColumn(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_COLUMN")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsColumn.class, sql, containerIds);
	}

	public List<MwmPartsDc> getMwmPartsDc(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_DC")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsDc.class, sql, containerIds);
	}

	public List<MwmPartsCond> getMwmPartsCond(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_COND")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsCond.class, sql, containerIds);
	}

	public List<MwmPartsCondItem> getMwmPartsCondItem(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_19")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_COND_ITEM")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsCondItem.class, sql, containerIds);
	}

	public List<MwmPartsOption> getMwmPartsOption(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_OPTION")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsOption.class, sql, containerIds);
	}

	public List<MwmPartsRelation> getMwmPartsRelation(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_RELATION")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsRelation.class, sql, containerIds);
	}

	public List<MwmPartsTableInfo> getMwmPartsTableInfo(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_TABLE_INFO")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsTableInfo.class, sql, containerIds);
	}

	public List<MwmPartsAttachFile> getMwmPartsAttachFile(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_ATTACH_FILE")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsAttachFile.class, sql, containerIds);
	}

	public List<MwmPartsCalcEc> getMwmPartsCalcEc(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_06")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_CALC_EC")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsCalcEc.class, sql, containerIds);
	}

	public List<MwmPartsCalcItem> getMwmPartsCalcItem(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_06")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_CALC_ITEM")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsCalcItem.class, sql, containerIds);
	}

	public List<MwmScreen> getMwmScreen(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreen.class, sql, screenIds);
	}

	public List<MwmScreenPartsCond> getMwmScreenPartsCond(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_PARTS_COND")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreenPartsCond.class, sql, screenIds);
	}

	public List<MwmScreenPartsCondItem> getMwmScreenPartsCondItem(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_20")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_PARTS_COND_ITEM")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreenPartsCondItem.class, sql, screenIds);
	}

	public List<MwmScreenCalc> getMwmScreenCalc(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_CALC")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreenCalc.class, sql, screenIds);
	}

	public List<MwmScreenCalcEc> getMwmScreenCalcEc(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_07")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_CALC_EC")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreenCalcEc.class, sql, screenIds);
	}

	public List<MwmScreenCalcItem> getMwmScreenCalcItem(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_07")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_CALC_ITEM")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreenCalcItem.class, sql, screenIds);
	}

	public List<MwmContainer> getMwmContainer(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_CONTAINER")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmContainer.class, sql, containerIds);
	}

	public List<MwmContainerJavascript> getMwmContainerJavascript(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_CONTAINER_JAVASCRIPT")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmContainerJavascript.class, sql, containerIds);
	}

	public List<MwmScreenProcessDef> getMwmScreenProcessDef(Long[] screenProcessIds) {
		if (screenProcessIds == null || screenProcessIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_PROCESS_DEF")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_PROCESS_ID", screenProcessIds.length));
		return select(MwmScreenProcessDef.class, sql, screenProcessIds);
	}

	public List<MwmScreenProcessLevel> getMwmScreenProcessLevel(Long[] screenProcessIds) {
		if (screenProcessIds == null || screenProcessIds.length == 0)
			return new ArrayList<>();

		final String replacement = toInListSql("X.SCREEN_PROCESS_ID", screenProcessIds.length);
		final String sql = getSql("UP0001_18").replaceFirst(FILTER_BY_PK, replacement);
		return select(MwmScreenProcessLevel.class, sql, screenProcessIds);
	}

	public List<MwmAccessibleScreen> getMwmAccessibleScreen(Long[] screenProcessIds) {
		if (screenProcessIds == null || screenProcessIds.length == 0)
			return new ArrayList<>();

		final List<Object> params = new ArrayList<>();
		for (Long screenProcessId : screenProcessIds) {
			params.add(screenProcessId);
		}
		params.add(LoginInfo.get().getCorporationCode());

		final String filter = toInListSql("SCREEN_PROCESS_ID", screenProcessIds.length)
				+ " and CORPORATION_CODE = ? ";

		final String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_ACCESSIBLE_SCREEN")
				.replaceFirst(FILTER_BY_PK, filter);
		return select(MwmAccessibleScreen.class, sql, params.toArray());
	}

	public List<MwmBlockDisplay> getMwmBlockDisplay(Long[] screenProcessIds) {
		if (screenProcessIds == null || screenProcessIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_BLOCK_DISPLAY")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_PROCESS_ID", screenProcessIds.length));
		return select(MwmBlockDisplay.class, sql, screenProcessIds);
	}

	public List<MwmScreenProcessMenu> getMwmScreenProcessMenu(Long[] screenProcessIds) {
		if (screenProcessIds == null || screenProcessIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_PROCESS_MENU")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_PROCESS_ID", screenProcessIds.length));
		return select(MwmScreenProcessMenu.class, sql, screenProcessIds);
	}

	public List<MwmJavascript> getMwmJavascript(Set<Long> javascriptIds) {
		if (javascriptIds == null || javascriptIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_JAVASCRIPT")
				.replaceFirst(FILTER_BY_PK, toInListSql("JAVASCRIPT_ID", javascriptIds.size()));
		return select(MwmJavascript.class, sql, javascriptIds.toArray());
	}

	public List<MwmJavascriptHistory> getMwmJavascriptHistory(Set<Long> javascriptIds) {
		if (javascriptIds == null || javascriptIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_JAVASCRIPT_HISTORY")
				.replaceFirst(FILTER_BY_PK, toInListSql("JAVASCRIPT_ID", javascriptIds.size()));
		return select(MwmJavascriptHistory.class, sql, javascriptIds.toArray());
	}

	public List<MwmPartsNumberingFormat> getMwmPartsNumberingFormat(Set<Long> partsNumberingFormatIds) {
		if (partsNumberingFormatIds == null || partsNumberingFormatIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_NUMBERING_FORMAT")
				.replaceFirst(FILTER_BY_PK, toInListSql("PARTS_NUMBERING_FORMAT_ID", partsNumberingFormatIds.size()));
		return select(MwmPartsNumberingFormat.class, sql, partsNumberingFormatIds.toArray());
	}

	public List<MwmPartsSequenceSpec> getMwmPartsSequenceSpec(Set<Long> partsSequenceSpecIds) {
		if (partsSequenceSpecIds == null || partsSequenceSpecIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_SEQUENCE_SPEC")
				.replaceFirst(FILTER_BY_PK, toInListSql("PARTS_SEQUENCE_SPEC_ID", partsSequenceSpecIds.size()));
		return select(MwmPartsSequenceSpec.class, sql, partsSequenceSpecIds.toArray());
	}

	public List<MwmTable> getMwmTable(Set<Long> tableIds) {
		if (tableIds == null || tableIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_TABLE")
				.replaceFirst(FILTER_BY_PK, toInListSql("TABLE_ID", tableIds.size()));
		return select(MwmTable.class, sql, tableIds.toArray());
	}

	public List<MwmTableAuthority> getMwmTableAuthority(String corporationCode, Set<Long> tableIds) {
		if (tableIds == null || tableIds.isEmpty())
			return new ArrayList<>();

		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.addAll(tableIds);

		String sql = getSql("UP0001_17")
				.replaceFirst(TABLE_NAME, "MWM_TABLE_AUTHORITY")
				.replaceFirst(FILTER_BY_PK, toInListSql("TABLE_ID", tableIds.size()));
		return select(MwmTableAuthority.class, sql, params.toArray());
	}

	public List<MwmTableSearch> getMwmTableSearch(String corporationCode, Set<Long> tableSearchIds) {
		if (tableSearchIds == null || tableSearchIds.isEmpty())
			return new ArrayList<>();

		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.addAll(tableSearchIds);

		String sql = getSql("UP0001_17")
				.replaceFirst(TABLE_NAME, "MWM_TABLE_SEARCH")
				.replaceFirst(FILTER_BY_PK, toInListSql("TABLE_SEARCH_ID", tableSearchIds.size()));
		return select(MwmTableSearch.class, sql, params.toArray());
	}

	public List<MwmTableSearchColumn> getMwmTableSearchColumn(Set<Long> tableSearchIds) {
		if (tableSearchIds == null || tableSearchIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_TABLE_SEARCH_COLUMN")
				.replaceFirst(FILTER_BY_PK, toInListSql("TABLE_SEARCH_ID", tableSearchIds.size()));
		return select(MwmTableSearchColumn.class, sql, tableSearchIds.toArray());
	}

	public List<MwmCategoryConfig> getMwmCategoryConfig(String corporationCode, Set<Long> tableIds) {
		if (tableIds == null || tableIds.isEmpty())
			return new ArrayList<>();

		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.addAll(tableIds);

		String sql = getSql("UP0001_17")
				.replaceFirst(TABLE_NAME, "MWM_CATEGORY_CONFIG")
				.replaceFirst(FILTER_BY_PK, toInListSql("TABLE_ID", tableIds.size()));
		return select(MwmCategoryConfig.class, sql, params.toArray());
	}

	public List<MwmCategory> getMwmCategory(String corporationCode, Set<Long> categoryIds) {
		if (categoryIds == null || categoryIds.isEmpty())
			return new ArrayList<>();

		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.addAll(categoryIds);

		String sql = getSql("UP0001_17")
				.replaceFirst(TABLE_NAME, "MWM_CATEGORY")
				.replaceFirst(FILTER_BY_PK, toInListSql("CATEGORY_ID", categoryIds.size()));
		return select(MwmCategory.class, sql, params.toArray());
	}

	public List<MwmCategoryAuthority> getMwmCategoryAuthority(String corporationCode, Set<Long> categoryIds) {
		if (categoryIds == null || categoryIds.isEmpty())
			return new ArrayList<>();

		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.addAll(categoryIds);

		String sql = getSql("UP0001_17")
				.replaceFirst(TABLE_NAME, "MWM_CATEGORY_AUTHORITY")
				.replaceFirst(FILTER_BY_PK, toInListSql("CATEGORY_ID", categoryIds.size()));
		return select(MwmCategoryAuthority.class, sql, params.toArray());
	}

	public List<MwmOption> getMwmOption(Set<Long> optionIds) {
		if (optionIds == null || optionIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_OPTION")
				.replaceFirst(FILTER_BY_PK, toInListSql("OPTION_ID", optionIds.size()));
		return select(MwmOption.class, sql, optionIds.toArray());
	}

	public List<MwmOptionItem> getMwmOptionItem(Set<Long> optionIds) {
		if (optionIds == null || optionIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_OPTION_ITEM")
				.replaceFirst(FILTER_BY_PK, toInListSql("OPTION_ID", optionIds.size()));
		return select(MwmOptionItem.class, sql, optionIds.toArray());
	}

	public List<MwmDc> getMwmDc(Set<Long> dcIds) {
		if (dcIds == null || dcIds.isEmpty())
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_DC")
				.replaceFirst(FILTER_BY_PK, toInListSql("DC_ID", dcIds.size()));
		return select(MwmDc.class, sql, dcIds.toArray());
	}

	public List<MwmMultilingual> getMwmMultilingual(
			String tableName, String pkColName, Object[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_08")
				.replaceAll(TABLE_NAME, tableName)
				.replaceFirst(PK_COL_NAME, pkColName)
				.replaceFirst(FILTER_BY_PK, toInListSql(pkColName, ids.length));
		return select(MwmMultilingual.class, sql, ids);
	}

	public List<MwmMultilingual> getMwmMultilingual(
			String tableName, String pkColName, String filterColName, Object[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_08")
				.replaceAll(TABLE_NAME, tableName)
				.replaceFirst(PK_COL_NAME, pkColName)
				.replaceFirst(FILTER_BY_PK, toInListSql(filterColName, ids.length));
		return select(MwmMultilingual.class, sql, ids);
	}

	public List<MwmMultilingual> getMwmMultilingual(String filterColName, Object[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList<>();
		// これだけ MWM_SCREEN_PROCESS_LEVEL専用だ
		String sql = getSql("UP0001_10")
				.replaceFirst(FILTER_BY_PK, toInListSql(filterColName, ids.length));
		return select(MwmMultilingual.class, sql, ids);
	}

	public List<MwmScreen> getMwmScreenByUniqueKey(String corporationCode, String screenCode) {
		if (isEmpty(screenCode) || isEmpty(corporationCode))
			return new ArrayList<>();

		final Object[] params = { corporationCode, screenCode };
		return select(MwmScreen.class, getSql("UP0001_01"), params);
	}

	public List<MwmDc> getMwmDcAll() {
		return select(MwmDc.class, getSql("UP0001_14"));
	}

	public List<MwmScreenJavascript> getMwmScreenJavascript(Long[] screenIds) {
		if (screenIds == null || screenIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_04")
				.replaceFirst(TABLE_NAME, "MWM_SCREEN_JAVASCRIPT")
				.replaceFirst(FILTER_BY_PK, toInListSql("SCREEN_ID", screenIds.length));
		return select(MwmScreenJavascript.class, sql, screenIds);
	}

	public List<MwmPartsEvent> getMwmPartsEvent(Long[] containerIds) {
		if (containerIds == null || containerIds.length == 0)
			return new ArrayList<>();

		String sql = getSql("UP0001_05")
				.replaceFirst(TABLE_NAME, "MWM_PARTS_EVENT")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_ID", containerIds.length));
		return select(MwmPartsEvent.class, sql, containerIds);
	}

	/** コンテナコードに対するコンテナIDを配列で抽出 */
	public Long[] getContainerIdsByContainerCode(String corporationCode, List<String> containerCodes) {
		final String sql = getSql("UP0001_21")
				.replaceFirst(FILTER_BY_PK, toInListSql("CONTAINER_CODE", containerCodes.size()));
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.addAll(containerCodes);
		return select(MwmContainer.class, sql, params.toArray())
				.stream()
				.map(c -> c.getContainerId())
				.toArray(i -> new Long[i]);
	}

	/** デフォルトブロック表示順マスタを抽出 */
	public List<MwmDefaultBlockDisplay> getMwmDefaultBlockDisplay(String corporationCode) {
		final Object[] params = { corporationCode };
		final String sql = getSql("UP0001_22");
		return select(MwmDefaultBlockDisplay.class, sql, params);
	}
}
