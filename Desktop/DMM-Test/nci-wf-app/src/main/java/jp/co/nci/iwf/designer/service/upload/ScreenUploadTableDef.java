package jp.co.nci.iwf.designer.service.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.enterprise.inject.spi.CDI;

import jp.co.nci.iwf.component.JpaEntityDefService;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 画面定義アップロードのテーブル定義の構造体
 */
public class ScreenUploadTableDef {
	public String tableName;
	public Class<? extends MwmBaseJpaEntity> clazz;
	public String pkJavaField;
	public String pkColumnName;
	public List<String[]> ukJavaFields = new ArrayList<>();
	public List<String[]> ukColumnNames = new ArrayList<>();

	/**
	 * コンストラクタ
	 * @param clazz アップロード対象とするエンティティのクラス
	 * @param uniques ユニークキーのフィールド配列の配列（ネストした配列）
	 */
	public ScreenUploadTableDef(Class<? extends MwmBaseJpaEntity> clazz, String[]...uniques) {
		this.clazz = clazz;
		JpaEntityDefService service = CDI.current().select(JpaEntityDefService.class).get();
		this.tableName = service.getTableName(clazz);
		this.pkColumnName = service.getPkColumnName(clazz);
		this.pkJavaField = service.getPkFieldName(clazz);
		this.ukColumnNames = service.getUkColumnNames(clazz);
		this.ukJavaFields = service.getUkFieldNames(clazz);

/*
		// テーブル名
		final Table t = clazz.getAnnotation(Table.class);
		if (t != null)
			this.tableName = t.name();

		// プライマリキー
		for (Field f : clazz.getDeclaredFields()) {
			final Id id = f.getAnnotation(Id.class);
			if (id != null) {
				this.pkJavaField = f.getName();
				final Column col = f.getAnnotation(Column.class);
				if (col == null)
					this.pkColumnName = this.pkJavaField.toUpperCase();
				else
					this.pkColumnName = col.name();
			}
		}

		// ユニークキー
		for (String[] fields : uniques) {
			if (fields != null && fields.length > 0) {
				ukJavaFields.add(fields);

				List<String> colNames = new ArrayList<>();
				for (String field : fields) {
					try {
						Field f = clazz.getDeclaredField(field);
						Column c = f.getAnnotation(Column.class);
						if (c == null) {
							colNames.add(field.toUpperCase());	// '_'がないときは @Columnが付与されないこともある
						} else {
							colNames.add(c.name());
						}
					}
					catch (NoSuchFieldException | SecurityException e) {
						throw new InternalServerErrorException(e);
					}
				}
				ukColumnNames.add(colNames.toArray(new String[colNames.size()]));
			}
		}
		*/

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ScreenUploadTableDef) {
			ScreenUploadTableDef t1 = (ScreenUploadTableDef)o;
			return MiscUtils.eq(t1.clazz, this.clazz);
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return this.clazz == null ? 17 : Objects.hash(this.clazz);
	}
}
