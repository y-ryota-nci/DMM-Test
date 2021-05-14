package jp.co.nci.iwf.endpoint.ti.ti0010;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.designer.service.tableInfo.TableMetaData;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategory;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;

/**
 * マスタ取込設定画面リポジトリ
 */
@ApplicationScoped
public class Ti0010Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** カテゴリ一覧を抽出 */
	public List<MwmCategory> getCategories(String corporationCode, String localeCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmCategory.class, getSql("TI0010_01"), params);
	}

	/** 取込済みテーブル一覧を抽出 */
	public Map<String, MwmTable> getMwmTable(String corporationCode, String localeCode, Long categoryId, String entityType) {
		final StringBuilder sql = new StringBuilder(getSql("TI0010_02"));
		final List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(localeCode);
		params.add(categoryId);
		if (isNotEmpty(entityType)) {
			sql.append(" and T.ENTITY_TYPE = ?");
			params.add(entityType);
		}
		return select(MwmTable.class, sql.toString(), params.toArray())
				.stream()
				.collect(Collectors.toMap(MwmTable::getTableName, t -> t));
	}

	/** 取込済みテーブル一覧を抽出 */
	public MwmTable getMwmTableByName(String tableName) {
		final StringBuilder sql = new StringBuilder(getSql("TI0010_08"));
		final Object[] params = { tableName };
		return selectOne(MwmTable.class, sql.toString(), params);
	}

	public long insertMwmTable(String tableName, String comment, TableMetaData meta) {
		final long tableId = numbering.newPK(MwmTable.class);
		MwmTable t = new MwmTable();
		t.setTableId(tableId);
		t.setTableName(tableName);
		t.setLogicalTableName(comment);
		t.setEntityType(meta.entityType);
		t.setDeleteFlag(DeleteFlag.OFF);
		em.persist(t);
		return tableId;
	}

	public void deleteMwmTable(MwmTable t) {
		em.remove(t);
	}

	public void deleteMwmCategoryConfig(MwmCategoryConfig cc) {
		em.remove(cc);
	}

	public void deleteMwmTableSearch(long tableId) {
		final Object[] params = { tableId } ;
		execSql(getSql("TI0010_04"), params);
	}

	public void deleteMwmTableSearchColumn(long tableId) {
		final Object[] params = { tableId } ;
		execSql(getSql("TI0010_05"), params);
	}

	public Map<Long, MwmCategoryConfig> getMwmCategoryConfig(String corporationCode) {
		final Object[] params = { corporationCode };
		return select(MwmCategoryConfig.class, getSql("TI0010_06"), params)
				.stream()
				.collect(Collectors.toMap(cc -> cc.getTableId(), cc -> cc));
	}

	public void insertMwmCategoryConfig(Long tableId, Long categoryId, String corporationCode) {
		long categoryConfigId = numbering.next("MWM_CATEGORY_CONFIG", "CATEGORY_CONFIG_ID");
		MwmCategoryConfig c = new MwmCategoryConfig();
		c.setCorporationCode(corporationCode);
		c.setDeleteFlag(DeleteFlag.OFF);
		c.setCategoryConfigId(categoryConfigId);
		c.setCategoryId(categoryId);
		c.setTableId(tableId);
		em.persist(c);
	}

	public void updateMwmCategoryConfig(MwmCategoryConfig cc, Long categoryId) {
		cc.setCategoryId(categoryId);
		cc.setDeleteFlag(DeleteFlag.OFF);
		em.merge(cc);
	}

	public Long updateMwmTable(MwmTable t, String comment, TableMetaData meta) {
		t.setLogicalTableName(comment);
		t.setEntityType(meta.entityType);
		em.merge(t);
		return t.getTableId();
	}
}
