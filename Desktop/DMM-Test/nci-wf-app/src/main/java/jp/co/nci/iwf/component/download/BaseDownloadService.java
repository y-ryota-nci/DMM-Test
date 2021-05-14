package jp.co.nci.iwf.component.download;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.iwf.component.JpaEntityDefService;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * 定義ダウンロードサービスの抽象基底クラス
 */
public abstract class BaseDownloadService extends BaseService {
	@Inject protected JpaEntityDefService jpaEntityDef;
	@Inject private Logger log;

	/**
	 * DTOの各フィールド内で、エンティティの値がテーブルのユニークキーと矛盾しないかを検証
	 * @param dto
	 */
	protected <DTO extends BaseDownloadDto> void validateUniqueKeys(DTO dto) {
		for (Field f : dto.getClass().getDeclaredFields()) {

			if (!List.class.isAssignableFrom(f.getType()))
				// リストじゃないのはエンティティでもない
				continue;
			final List<? extends MwmBaseJpaEntity> entities = getFieldValue(dto, f.getName());
			if (entities == null || entities.isEmpty())
				// 要素がないなら無意味
				continue;
			if (!(entities.get(0) instanceof MwmBaseJpaEntity))
				// エンティティにはJPAアノテーションがないとダメ
				continue;

			final Class<? extends MwmBaseJpaEntity> clazz = entities.get(0).getClass();
			final List<PropertyDescriptor[]> uniqueKeys = getUniqueKeys(entities.get(0));
			log.trace("{}.{} --> {", dto.getClass().getSimpleName(), f.getName());

			for (int i = 0; i < uniqueKeys.size(); i++) {
				final PropertyDescriptor[] pds = uniqueKeys.get(i);
				final Set<String> uniques = new HashSet<>();
				log.trace("  uniqueKey{} {", (i + 1));
				for (MwmBaseJpaEntity entity : entities) {
					// このエンティティのユニークキーを求める
					StringBuilder values = new StringBuilder();
					for (PropertyDescriptor pd : pds) {
						Object v = getPropertyValue(entity, pd);
						if (values.length() > 0) {
							values.append("^");
						}
						values.append(v);
					}
					String unique = values.toString();
					if (uniques.contains(values))
						throw new InternalServerErrorException("ユニークキーが重複しています class=" + clazz.getSimpleName() + " ユニークキー=" + unique);

					uniques.add(unique);
					log.trace("    {}", unique);
				}
				log.trace("  }");
			}
			log.trace("}");
		}
	}

	private List<PropertyDescriptor[]> getUniqueKeys(MwmBaseJpaEntity entity) {
		final Class<? extends MwmBaseJpaEntity> clazz = entity.getClass();
		final List<String[]> ukFieldNames = jpaEntityDef.getUkFieldNames(clazz);
		final List<PropertyDescriptor[]>  uniqueKeys = new ArrayList<>(ukFieldNames.size());
		for (String[] fields : ukFieldNames) {
			List<PropertyDescriptor> pdList = new ArrayList<>();
			for (String field : fields) {
				PropertyDescriptor pd = getPropertyDescriptor(entity, field);
				pdList.add(pd);
			}
			uniqueKeys.add(pdList.toArray( new PropertyDescriptor[pdList.size()]));
		}
		return uniqueKeys;
	}
}
