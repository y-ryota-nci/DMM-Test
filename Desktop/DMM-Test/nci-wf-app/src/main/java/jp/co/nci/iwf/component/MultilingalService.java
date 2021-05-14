package jp.co.nci.iwf.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.i18n.LocaleService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;

/**
 * 画面多言語対応サービス
 */
@BizLogic
public class MultilingalService extends BaseRepository {
	@Inject
	private NumberingService numbering;
	@Inject
	private LocaleService locale;

	/**
	 * 多言語対応マスタを抽出
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @return
	 */
	public MwmMultilingual get(String tableName, long id, String columnName, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(tableName);
		params.add(id);
		params.add(columnName);
		params.add(localeCode);

		final List<MwmMultilingual> list = select(MwmMultilingual.class, getSql("CM0010"), params.toArray());
		return (list.isEmpty() ? null : list.get(0));
	}

	/**
	 * 多言語対応マスタを抽出
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @return
	 */
	public List<MwmMultilingual> get(String tableName, long id, String columnName) {
		final List<Object> params = new ArrayList<>();
		params.add(tableName);
		params.add(id);
		params.add(columnName);

		return select(MwmMultilingual.class, getSql("CM0020"), params.toArray());
	}

	/**
	 * 多言語対応マスタを抽出
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @return
	 */
	public Map<String, String> getValues(String tableName, long id, String columnName) {
		return get(tableName, id, columnName)
				.stream()
				.collect(Collectors.toMap(e -> e.getLocaleCode(), e -> e.getVal()));
	}

	/**
	 * 多言語値を抽出
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @return
	 */
	public String getVal(String tableName, long id, String columnName) {
		final String localeCode = locale.getLocaleCode();
		final MwmMultilingual entity = get(tableName, id, columnName, localeCode);
		return (entity == null ? null : entity.getVal());
	}

	/**
	 * 差分更新
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @param val 値
	 */
	public void save(String tableName, long id, String columnName, String val) {
		String localeCode = locale.getLocaleCode();
		save(tableName, id, columnName, localeCode, val);
	}

	/**
	 * 差分更新
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @param localeCode 言語コード
	 * @param val 値
	 */
	public void save(String tableName, long id, String columnName, String localeCode, String val) {
		MwmMultilingual entity = get(tableName, id, columnName, localeCode);
		if (entity == null) {
			long multilingualId = numbering.newPK(MwmMultilingual.class);
			entity = new MwmMultilingual();
			entity.setMultilingualId(multilingualId);
			entity.setTableName(tableName);
			entity.setId(id);
			entity.setColumnName(columnName);
			entity.setLocaleCode(localeCode);
			entity.setVal(val);
			entity.setDeleteFlag(DeleteFlag.OFF);
			em.persist(entity);
		}
		else {
			entity.setVal(val);
			entity.setDeleteFlag(DeleteFlag.OFF);
			em.merge(entity);
		}
		em.flush();
	}

	/**
	 * 物理削除
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @param columnName 取得先
	 * @return
	 */
	public int physicalDelete(String tableName, long id, String columnName) {
		final List<Object> params = new ArrayList<>();
		params.add(tableName);
		params.add(id);
		params.add(columnName);
		return execSql(getSql("CM0011"), params.toArray());
	}

	/**
	 * 物理削除
	 * @param tableName テーブル名
	 * @param id テーブル名のPK
	 * @return
	 */
	public int physicalDelete(String tableName, long id) {
		final List<Object> params = new ArrayList<>();
		params.add(tableName);
		params.add(id);
		return execSql(getSql("CM0012"), params.toArray());
	}
}
