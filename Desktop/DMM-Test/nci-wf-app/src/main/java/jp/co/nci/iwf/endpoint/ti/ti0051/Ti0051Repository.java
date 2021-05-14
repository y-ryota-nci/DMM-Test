package jp.co.nci.iwf.endpoint.ti.ti0051;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearch;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableSearchColumn;

/**
 * 汎用テーブル検索条件設定リポジトリ
 */
@ApplicationScoped
public class Ti0051Repository extends BaseRepository {
	@Inject private NumberingService numbering;


	/** 検索条件コードの重複チェック */
	public int count(String corporationCode, long tableId, String tableSearchCode, long tableSearchId) {
		Object[] params = { corporationCode, tableId, tableSearchCode, tableSearchId };
		return count(getSql("TI0051_03"), params);
	}

	/** 汎用テーブル検索条件をPKで抽出 */
	public MwmTableSearch getMwmTableSearch(long tableSearchId) {
		return em.find(MwmTableSearch.class, tableSearchId);
	}

	/** 汎用テーブル検索条件をインサート */
	public long insert(MwmTableSearch table) {
		long tableSearchId = numbering.newPK(MwmTableSearch.class);
		table.setTableSearchId(tableSearchId);
		String f = table.getDefaultSearchFlag();
		table.setDefaultSearchFlag(CommonFlag.ON.equals(f) ? CommonFlag.ON : CommonFlag.OFF);
		em.persist(table);
		em.flush();
		return tableSearchId;
	}

	/** 汎用テーブル検索条件をアップデート */
	public long update(MwmTableSearch current, MwmTableSearch input) {
		current.setTableSearchName(input.getTableSearchName());
		current.setDefaultSearchFlag(CommonFlag.ON.equals(input.getDefaultSearchFlag()) ? CommonFlag.ON : CommonFlag.OFF);
		current.setDeleteFlag(DeleteFlag.OFF);
		current.setDisplayName(input.getDisplayName());
		current.setVersion(input.getVersion());
		em.merge(current);
		em.flush();
		return current.getTableSearchId();
	}

	/** 検索条件ID配下の検索条件カラムを抽出 */
	public List<MwmTableSearchColumn> getMwmTableSearchColumns(long tableSearchId) {
		final Object[] params = { tableSearchId };
		return select(MwmTableSearchColumn.class, getSql("TI0051_04"), params);
	}

	/** 汎用テーブル検索条件カラムをインサート */
	public long insert(long tableSearchId, MwmTableSearchColumnEx input) {
		long tableSearchColumnId = numbering.next("MWM_TABLE_SEARCH_COLUMN", "TABLE_SEARCH_COLUMN_ID");
		MwmTableSearchColumn c = new MwmTableSearchColumn();
		copyFieldsAndProperties(input, c);
		c.setTableSearchColumnId(tableSearchColumnId);
		c.setTableSearchId(tableSearchId);
		em.persist(c);
		return tableSearchColumnId;
	}

	/** 汎用テーブル検索条件カラムをアップデート */
	public void update(MwmTableSearchColumn c, MwmTableSearchColumnEx input) {
		c.setConditionDisplayPosition(input.conditionDisplayPosition);
		c.setConditionDisplayType(input.conditionDisplayType);
		c.setConditionInitType1(input.conditionInitType1);
		c.setConditionInitType2(input.conditionInitType2);
		c.setConditionInitValue1(input.conditionInitValue1);
		c.setConditionInitValue2(input.conditionInitValue2);
		c.setConditionMatchType(input.conditionMatchType);
		c.setConditionOptionId(input.conditionOptionId);
		c.setConditionBlankType(input.conditionBlankType);
		c.setConditionTrimFlag(input.conditionTrimFlag);
		c.setConditionKanaConvertType(input.conditionKanaConvertType);
		c.setDeleteFlag(DeleteFlag.OFF);
		c.setLogicaColumnName(input.logicaColumnName);
		c.setSearchColumnType(input.searchColumnType);
		c.setResultAlignType(input.resultAlignType);
		c.setResultDisplayPosition(input.resultDisplayPosition);
		c.setResultDisplayType(input.resultDisplayType);
		c.setResultDisplayWidth(input.resultDisplayWidth);
		c.setResultOrderByDirection(input.resultOrderByDirection);
		c.setResultOrderByPosition(input.resultOrderByPosition);
		c.setSortOrder(input.sortOrder);
		c.setVersion(input.version);
		em.merge(c);
	}

	/** 汎用テーブル検索条件カラムを削除 */
	public void delete(Map<Long, MwmTableSearchColumn> cols) {
		for (MwmTableSearchColumn c : cols.values()) {
			c.setDeleteFlag(DeleteFlag.ON);
			em.merge(c);
		}
		em.flush();
	}
}
