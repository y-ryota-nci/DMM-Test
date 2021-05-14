package jp.co.nci.iwf.component.upload;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.Table;
import javax.ws.rs.InternalServerErrorException;

import org.slf4j.Logger;

import jp.co.nci.iwf.component.JpaEntityDefService;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.designer.service.upload.ScreenUploadRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 定義アップロード時のPK置換処理の基底クラス
 */
public abstract class BasePkReplacer extends MiscUtils {
	@Inject private ScreenUploadRepository repository;
	@Inject protected NumberingService numbering;
	@Inject protected JpaEntityDefService jpaEntityDef;
	@Inject protected Logger log;

	/**
	 * エンティティのユニークキーからプライマリキーの置換要否を調べ、すでに使われているのであればエンティティのプライマリキーを置き換える
	 * @param uploads エンティティリスト
	 * @return 置換が必要なプライマリキーのリスト
	 */
	protected <E extends MwmBaseJpaEntity> ChangedPKs<E> replacePK(List<E> uploads, Class<E> clazz) {
		final ChangedPKs<E> results = new ChangedPKs<>(clazz);
		for (E upload : uploads) {
			final String pkField = jpaEntityDef.getPkFieldName(clazz);

			// ユニークキーで既存レコードを同定
			final E current = repository.getByUniqueKey(upload);
			Long newPK, version;
			Long uploadPK = getPropertyValue(upload, pkField);
			if (current == null) {
				// 新規なので採番しなおし
				newPK = numbering.newPK(clazz);
				version = null;
			}
			else {
				// 既存レコードありなので、そのPKとバージョンを流用
				newPK = getPropertyValue(current, pkField);
				version = getPropertyValue(current, "version");
			}

			// 差異があればPK置換リストに登録
			if (!eq(newPK, uploadPK) && uploadPK != null && newPK != null) {
				if (results.containsKey(uploadPK)) {
					throw new InternalServerErrorException("すでに同一プライマリキーでの置換Mapを作成済みです。重複した処理が無いかを確認ください。" + clazz.getSimpleName());
				}
				results.put(uploadPK, new ReplacedPK(clazz, uploadPK, newPK));
			}
			// PKとバージョンを反映
			setPropertyValue(upload, pkField, newPK);
			setPropertyValue(upload, "version", version);
		}
		return results;
	}

	/**
	 * エンティティの現在値が置換対象になっていれば、新値で置換
	 * @param propName 置換フィールド名
	 * @param changes 変更点Map
	 * @param entities 対象エンティティリスト
	 */
	protected <E extends MwmBaseJpaEntity> void copyPK(String propName, ChangedPKs<?> changes, List<E> entities) {
		for (E entity : entities) {
			Long oldPK = L(getPropertyValue(entity, propName));
			ReplacedPK change = changes.get(oldPK);
			if (change != null) {
				PropertyDescriptor pd = getPropertyDescriptor(entity, propName);
				Class<?> type = pd.getPropertyType();
				if (type == long.class || type == Long.class)
					setPropertyValue(entity, propName, change.newPK);
				else
					setPropertyValue(entity, propName, String.valueOf(change.newPK));
			}
		}
	}

	/** Object -> Long変換 */
	protected static Long L(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Number)
			return ((Number)obj).longValue();
		if (obj instanceof String)
			if (eq("", obj))
				return null;
			else
				return Long.valueOf((String)obj);
		throw new IllegalArgumentException("不明な値です obj=" + obj);
	}

	/**
	 * 各行へboolean式を適用しtrueを返した行だけのリストを作成。
	 * @param entities リスト
	 * @param predicate boolean式
	 * @return
	 */
	protected <E extends MwmBaseJpaEntity> List<E> filter(List<E> entities, Predicate<? super E> predicate) {
		List<E> screenCalcItemList = entities.stream()
				.filter(predicate)
				.collect(Collectors.toList());
		return screenCalcItemList;
	}

	/**
	 * 多言語対応マスタのIDが各エンティティの新IDへ置換。
	 * その上でエンティティのユニークキーからプライマリキーの置換要否を調べ、
	 * すでに使われているのであればエンティティのプライマリキーを置き換える。
	 * @param uploads 多言語対応マスタリスト
	 * @param changedPKsMap 各エンティティの新IDのMap
	 */
	protected <E extends MwmBaseJpaEntity> ChangedPKs<MwmMultilingual> replacePK(
			List<MwmMultilingual> uploads,
			ChangedPKsMap changedPKsMap
	) {

		// クラスがキーになっているのを、クラスの@Tableアノテーションが示すテーブル名をキーにして再Map
		Map<String, Map<Long, ReplacedPK>> allMap = changedPKsMap.entrySet().stream()
				.filter(entry -> !entry.getValue().isEmpty())
				.collect(Collectors.toMap(
						entry -> entry.getKey().getAnnotation(Table.class).name(),
						entry -> entry.getValue()
				));
		// 旧IDを新IDに置換
		for (MwmMultilingual upload : uploads) {
			Long uploadId = upload.getId();
			String tableName = upload.getTableName();
			Map<Long, ReplacedPK> changes = allMap.get(tableName);
			if (changes != null && changes.containsKey(uploadId)) {
				upload.setId(changes.get(uploadId).newPK);
			}
		}

		// エンティティのユニークキーからプライマリキーの置換要否を調べ、
		// すでに使われているのであればエンティティのプライマリキーを置き換える
		return replacePK(uploads, MwmMultilingual.class);
	}

}
