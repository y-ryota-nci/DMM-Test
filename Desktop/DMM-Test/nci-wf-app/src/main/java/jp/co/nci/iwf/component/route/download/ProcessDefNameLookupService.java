package jp.co.nci.iwf.component.route.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * プロセス定義アップロード用名称ルックアップサービス
 */
@ApplicationScoped
public class ProcessDefNameLookupService extends BaseRepository {
	/** 全エンティティから対応する名称ルックアップリスト(WFM_NAME_LOOKUP)の全言語／全カラムを抽出 */
	public List<WfmNameLookup> getNameLookupList(Map<Class<?>, List<?>> allEntities) {
		final List<WfmNameLookup> allNames = new ArrayList<>(1024);
		for (Class<?> clazz : allEntities.keySet()) {

			if (clazz == WfmNameLookup.class)
				break;

			List<?> entities = allEntities.get(clazz);
			if (entities == null || entities.isEmpty())
				continue;

			// エンティティのテーブル名とIDを抜き出す
			Object entity = entities.get(0);
			final String tableName = ProcessDefDownloadUtils.getTableName(entity);
			final Set<Long> ids = entities.stream()
					.map(e -> (Long)getPropertyValue(e, "id"))
					.filter(id -> id != null)
					.collect(Collectors.toSet());

			// エンティティの名称ルックアップをカラム名／言語を無視して抜き出し
			if (!ids.isEmpty()) {
				final List<WfmNameLookup> names = getNameLookupList(tableName, ids);
				allNames.addAll(names);
			}
		}
		return allNames;
	}

	/** 名称ルックアップを抽出 */
	private List<WfmNameLookup> getNameLookupList(String tableName, Set<Long> ids) {
		final List<Object> params = new ArrayList<>();
		params.add(tableName);
		ids.forEach(id -> params.add(id));

		final String sql = new StringBuilder()
				.append(getSql("MM0000_02"))
				.append(toInListSql("ID", ids.size()))
				.toString();

		return select(WfmNameLookup.class, sql, params.toArray());
	}
}
