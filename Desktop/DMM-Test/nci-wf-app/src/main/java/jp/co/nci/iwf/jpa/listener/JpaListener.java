package jp.co.nci.iwf.jpa.listener;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jpa.entity.mw.MwCommonColumns;
import jp.co.nci.iwf.jpa.entity.wm.NciCommonColumns;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * JPAでのListener
 */
@ApplicationScoped
public class JpaListener {
	private static final String NULL_CORPORATION_CODE = "---";
	private static final String NULL_USER_CODE = "---";

	private final Map<Class<?>, Set<Field>> trimProperties = new HashMap<Class<?>, Set<Field>>();

	/**
	 * DBインサート処理の前に呼び出される
	 * @param entity
	 */
	@PrePersist
	public void prePresist(Object entity) {
		// @NotTrimが付与されていないStringフィールドに trimを行う
		trimStringFields(entity);

		fillCreator(entity);
		fillUpdator(entity);
	}

	/**
	 * DBインサート処理の後に呼び出される
	 * @param entity
	 */
	@PreUpdate
	public void preUpdate(Object entity) {
		// @NotTrimが付与されていないStringフィールドに trimを行う
		trimStringFields(entity);

		fillUpdator(entity);
	}

	/**
	 * データをDBから読み込んだあとに呼び出される
	 * @param entity
	 * @throws Exception
	 */
	@PostLoad
	public void postLoad(final Object entity) throws Exception {
	}

	/** アノテーション「＠NotTrim」が付与されていないStringフィールドにtrimを行う */
	private void trimStringFields(final Object entity) {
		try {
			Set<Field> trimProperties = getTrimProperties(entity.getClass());
			for (final Field fieldToTrim : trimProperties) {
				final String propertyValue = (String) fieldToTrim.get(entity);
				if (propertyValue != null)
					fieldToTrim.set(entity, propertyValue.trim());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * トリムする対象フィールドを特定
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	private Set<Field> getTrimProperties(Class<?> entityClass) {
		if (Object.class.equals(entityClass))
			return Collections.emptySet();
		Set<Field> propertiesToTrim = trimProperties.get(entityClass);
		if (propertiesToTrim == null) {
			propertiesToTrim = new HashSet<Field>();
			for (final Field field : entityClass.getDeclaredFields()) {
				// 文字列ならトリム
				// ただしアノテーション@NotTrimが付与されているフィールドは除外
				if (field.getType().equals(String.class)
						&& field.getAnnotation(NotTrim.class) == null
				) {
					field.setAccessible(true);
					propertiesToTrim.add(field);
				}
			}
			trimProperties.put(entityClass, propertiesToTrim);
		}
		return propertiesToTrim;
	}

	/** WHO列のうち、作成者・作成日を設定 */
	private void fillCreator(Object entity) {
		final LoginInfo u = getLoginInfo();
		final java.sql.Timestamp now = MiscUtils.timestamp();
		if (entity instanceof MwCommonColumns) {
			MwCommonColumns e = (MwCommonColumns)entity;
			e.setTimestampCreated(now);
			if (u == null) {
				e.setCorporationCodeCreated(NULL_CORPORATION_CODE);
				e.setUserCodeCreated(NULL_USER_CODE);
			} else {
				e.setCorporationCodeCreated(u.getCorporationCode());
				e.setUserCodeCreated(u.getUserCode());
			}
		}
		else if (entity instanceof NciCommonColumns) {
			final String programId = getProgramId();
			final NciCommonColumns e = (NciCommonColumns)entity;
			e.setProgramIdCreated(programId);
			e.setTimestampCreated(now);
			if (u == null)
				e.setUserCodeCreated(NULL_USER_CODE);
			else
				e.setUserCodeCreated(u.getUserCode());
			e.setVersion(1L);
		}
	}

	/** WHO列のうち、更新者・更新日を設定 */
	private void fillUpdator(Object entity) {
		final java.sql.Timestamp now = MiscUtils.timestamp();
		final LoginInfo u = getLoginInfo();
		if (entity instanceof MwCommonColumns) {
			MwCommonColumns e = (MwCommonColumns)entity;
			e.setTimestampUpdated(now);
			if (u == null) {
				e.setCorporationCodeUpdated(NULL_CORPORATION_CODE);
				e.setUserCodeUpdated(NULL_USER_CODE);
			} else {
				e.setCorporationCodeUpdated(u.getCorporationCode());
				e.setUserCodeUpdated(u.getUserCode());
			}
		}
		else if (entity instanceof NciCommonColumns) {
			final String programId = getProgramId();
			final NciCommonColumns e = (NciCommonColumns)entity;
			e.setProgramIdUpdated(programId);
			e.setTimestampUpdated(now);
			if (u == null)
				e.setUserCodeUpdated(NULL_USER_CODE);
			else
				e.setUserCodeUpdated(u.getUserCode());
		}
	}

	/**
	 * スタックトレースをさかのぼって、プログラムIDを求める
	 * @return
	 */
	private String getProgramId() {
		// スタックトレースをさかのぼって、最後に処理したNCIパッケージのクラスをプログラムIDとする
		final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		final Optional<String> op = Stream.of(traces)
				.map(t -> t.getClassName())
				.filter(s -> s.startsWith("jp.co.nci") && !s.startsWith("jp.co.nci.iwf.jpa"))
				.map(s -> s.substring(s.lastIndexOf('.') + 1))
				.findFirst();
		final String programId = op.isPresent() ?  op.get() : "JPA";
		return programId;
	}

	private LoginInfo getLoginInfo() {
		try {
			return LoginInfo.get();
		}
		catch (Exception e) {
			return null;
		}
	}
}
