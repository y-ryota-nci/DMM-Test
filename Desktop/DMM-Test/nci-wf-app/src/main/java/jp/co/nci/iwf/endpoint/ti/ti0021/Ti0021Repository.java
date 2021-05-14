package jp.co.nci.iwf.endpoint.ti.ti0021;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmCategoryAuthority;
import jp.co.nci.iwf.jpa.entity.mw.MwmTableAuthority;

/**
 * マスタ権限設定のリポジトリ
 */
@ApplicationScoped
public class Ti0021Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** 汎用カテゴリ権限一覧を返す */
	public List<Ti0021Category> getCategories(String corporationCode, String menuRoleCode, String localeCode) {
		final Object[] params = { localeCode, menuRoleCode, corporationCode };
		final List<Ti0021Category> list = select(Ti0021Category.class, getSql("TI0021_01"), params);
		list.forEach(c -> c.selected = isNotEmpty(c.menuRoleCode) && eq(DeleteFlag.OFF, c.deleteFlag));
		return list;
	}

	/** 汎用テーブル権限一覧を返す */
	public Map<Long, List<Ti0021Table>> getTables(String corporationCode, String menuRoleCode, String localeCode) {
		final Object[] params = { localeCode, menuRoleCode, corporationCode };
		final List<Ti0021Table> list = select(Ti0021Table.class, getSql("TI0021_02"), params);
		return list.stream()
				.peek(t -> t.selected = isNotEmpty(t.menuRoleCode) && eq(DeleteFlag.OFF, t.deleteFlag))
				.collect(Collectors.groupingBy(t -> t.categoryId, Collectors.toList()));
	}

	/** 汎用テーブルカテゴリ権限を抽出 */
	public Map<Long, MwmCategoryAuthority> getMwmCategoryAuthority(String corporationCode, String menuRoleCode) {
		final Object[] params = { corporationCode, menuRoleCode };
		return select(MwmCategoryAuthority.class, getSql("TI0021_03"), params)
				.stream()
				.collect(Collectors.toMap(ca -> ca.getCategoryAuthorityId(), ca -> ca));
	}

	/** 汎用テーブルカテゴリ権限をインサート */
	public long insert(String corporationCode, String menuRoleCode, Ti0021Category input) {
		final long categoryAuthorityId = numbering.newPK(MwmCategoryAuthority.class);
		final MwmCategoryAuthority ca = new MwmCategoryAuthority();
		ca.setCorporationCode(corporationCode);
		ca.setDeleteFlag(DeleteFlag.OFF);
		ca.setMenuRoleCode(menuRoleCode);
		ca.setCategoryAuthorityId(categoryAuthorityId);
		ca.setCategoryId(input.categoryId);
		em.persist(ca);
		return categoryAuthorityId;
	}

	/** 汎用テーブルカテゴリ権限をアップデート */
	public long update(MwmCategoryAuthority ca, Ti0021Category input) {
		ca.setDeleteFlag(DeleteFlag.OFF);
		if (input.version != null)
			ca.setVersion(input.version);
		em.merge(ca);
		return ca.getCategoryAuthorityId();
	}

	/** 汎用テーブルカテゴリ権限を削除 */
	public void delete(MwmCategoryAuthority ca) {
		ca.setDeleteFlag(DeleteFlag.ON);
		em.merge(ca);
	}

	/** 汎用テーブル権限を抽出 */
	public Map<Long, MwmTableAuthority> getMwmTableAuthority(String corporationCode, String menuRoleCode) {
		final Object[] params = { corporationCode, menuRoleCode };
		return select(MwmTableAuthority.class, getSql("TI0021_04"), params)
				.stream()
				.collect(Collectors.toMap(ca -> ca.getTableAuthorityId(), ca -> ca));
	}

	public long insert(String corporationCode, String menuRoleCode, Ti0021Table input) {
		final long tableAuthorityId = numbering.next("MWM_TABLE_AUTHORITY", "TABLE_AUTHORITY_ID");
		final MwmTableAuthority ta = new MwmTableAuthority();
		ta.setCorporationCode(corporationCode);
		ta.setDeleteFlag(DeleteFlag.OFF);
		ta.setMenuRoleCode(menuRoleCode);
		ta.setTableAuthorityId(tableAuthorityId);
		ta.setTableId(input.tableId);
		em.persist(ta);
		return tableAuthorityId;
	}

	public long update(MwmTableAuthority ta, Ti0021Table input) {
		ta.setDeleteFlag(DeleteFlag.OFF);
		if (input.tableVersion != null)
			ta.setVersion(input.tableVersion);
		em.merge(ta);
		return ta.getTableAuthorityId();
	}

	public void delete(MwmTableAuthority ta) {
		ta.setDeleteFlag(DeleteFlag.ON);
		em.merge(ta);
	}

}
