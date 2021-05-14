package jp.co.nci.iwf.endpoint.vd.vd0114;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.tableSearch.TableSearchRepository;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmContainer;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmPart;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsNumberingFormat;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;

/**
 * パーツプロパティ設定画面リポジトリ
 */
@ApplicationScoped
public class Vd0114Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;
	@Inject private TableSearchRepository tsRepository;

	/**
	 * コンテナ一覧
	 * @param excludeContainerId 除外するコンテナID
	 * @return
	 */
	public List<MwmContainer> getMwmContainerList(String corporationCode, long excludeContainerId) {
		final List<Object> params = new ArrayList<>();
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(excludeContainerId);
		params.add(corporationCode);
		return select(MwmContainer.class, getSql("VD0114_01"), params.toArray());
	}

	/**
	 * 業務管理項目一覧
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<MwmBusinessInfoName> getMwmBusinessInfoName(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmBusinessInfoName.class, getSql("VD0114_02"), params);
	}

	/**
	 * 文書管理項目一覧
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<MwmDocBusinessInfoName> getMwmDocBusinessInfoName(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmDocBusinessInfoName.class, getSql("VD0114_10"), params);
	}

	/**
	 * 採番パーツ一覧
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<MwmPartsNumberingFormat> getPartsNumberingFormats(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmPartsNumberingFormat.class, getSql("VD0114_03"), params);
	}

	/**
	 * 選択肢一覧
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<MwmOption> getMwmOptions(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmOption.class, getSql("VD0114_04"), params);
	}

	/**
	 * 選択肢項目一覧
	 * @param corporationCode
	 * @param optionId
	 * @param localeCode
	 * @return
	 */
	public List<MwmOptionItem> getMwmOptionItems(String corporationCode, Long optionId, String localeCode) {
		final Object[] params = { localeCode, corporationCode, optionId };
		return select(MwmOptionItem.class, getSql("VD0114_05"), params);
	}

	/**
	 * 汎用テーブルで「絞込条件 or 検索結果」に使用するカラム一覧
	 * @param tableSearchId 汎用テーブル検索ID
	 * @return
	 */
	public List<OptionItem> getColumnsInOrOut(Long tableSearchId) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<OptionItem> items = tsRepository.getColumns(tableSearchId, corporationCode, localeCode)
			.stream()
			.filter(c -> conditionDisplayTypes.contains(c.conditionDisplayType) || resultDisplayTypes.contains(c.resultDisplayType))
			.map(c -> new OptionItem(c.columnName, defaults(c.logicaColumnName, c.columnName)))
			.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	private final Set<String> resultDisplayTypes = new HashSet<>(Arrays.asList(ResultDisplayType.DISPLAY, ResultDisplayType.HIDDEN));
	private final Set<String> conditionDisplayTypes = new HashSet<>(Arrays.asList(
			ConditionDisplayType.DISPLAY_TEXTBOX, ConditionDisplayType.DISPLAY_DROPDOWN, ConditionDisplayType.HIDDEN));
	/**
	 * 汎用テーブルで「絞込条件 and 検索結果」に使用するカラム一覧
	 * @param tableSearchId 汎用テーブル検索ID
	 * @return
	 */
	public List<OptionItem> getColumnsInAndOut(Long tableSearchId) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<OptionItem> items = tsRepository.getColumns(tableSearchId, corporationCode, localeCode)
			.stream()
			.filter(c -> conditionDisplayTypes.contains(c.conditionDisplayType) && resultDisplayTypes.contains(c.resultDisplayType))
			.map(c -> new OptionItem(c.columnName, defaults(c.logicaColumnName, c.columnName)))
			.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * 汎用テーブルで「検索結果」に使用するカラム一覧
	 * @param tableSearchId 汎用テーブル検索ID
	 * @return
	 */
	public List<OptionItem> getColumnsOut(Long tableSearchId) {
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String localeCode = sessionHolder.getLoginInfo().getLocaleCode();
		List<OptionItem> items = tsRepository.getColumns(tableSearchId, corporationCode, localeCode)
			.stream()
			.filter(c -> resultDisplayTypes.contains(c.resultDisplayType))
			.map(c -> new OptionItem(c.columnName, defaults(c.logicaColumnName, c.columnName)))
			.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * 汎用テーブル一覧を抽出
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<OptionItem> getMwmTables(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		List<OptionItem> items = select(MwmTable.class, getSql("VD0114_06"), params)
				.stream()
				.map(t -> new OptionItem(t.getTableId(), defaults(t.getLogicalTableName(), t.getTableName())))
				.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * 汎用テーブル検索条件一覧
	 * @param tableId
	 * @param corporationCode
	 * @param localeCode
	 * @return
	 */
	public List<OptionItem> getMwmTableSearchs(Long tableId, String corporationCode, String localeCode) {
		if (tableId == null)
			return new ArrayList<>();

		final Object[] params = { localeCode, corporationCode, tableId };
		final List<OptionItem> items = select(MwmTableSearchEx.class, getSql("VD0114_07"), params)
				.stream()
				.map(t -> new OptionItem(t.tableSearchId, t.tableSearchName))
				.collect(Collectors.toList());
		items.add(0, OptionItem.EMPTY);
		return items;
	}

	/**
	 * コンテナ配下のパーツで指定されたパーツ種別のものを抽出
	 * @param childContainerId
	 * @param targetPartsTypes
	 * @param localeCode
	 * @return
	 */
	public List<MwmPart> findChildParts(Long childContainerId, Collection<Integer> targetPartsTypes, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);
		params.add(childContainerId);

		final String byPartsType;
		if (isNotEmpty(targetPartsTypes)) {
			params.addAll(targetPartsTypes);
			byPartsType = toInListSql("P.PARTS_TYPE", targetPartsTypes.size());
		} else {
			byPartsType = "";
		}
		final String sql = getSql("VD0114_08")
				.replaceFirst(quotePattern("{BY_PARTS_TYPE}"), byPartsType);
		return select(MwmPart.class, sql, params.toArray());
	}
}
