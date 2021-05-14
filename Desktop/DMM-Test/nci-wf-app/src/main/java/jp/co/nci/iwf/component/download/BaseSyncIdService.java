package jp.co.nci.iwf.component.download;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.NativeSqlUtils;

/**
 * アップロードでの採番値同期サービスの基底クラス
 */
public abstract class BaseSyncIdService extends MiscUtils {
	/** 採番用プロパティコード */
	protected Map<Class<?>, String> propertyCodes = new HashMap<>();

	@Inject protected Connection conn;
	@Inject protected SessionHolder sessionHolder;

	protected static final String SQL =
			"update WFM_PROPERTY "
			+ "set PROPERTY_VALUE = ?"
			+ ", TIMESTAMP_UPDATED = ?"
			+ ", CORPORATION_CODE_UPDATED = ?"
			+ ", USER_CODE_UPDATED = ?"
			+ ", IP_UPDATED = ? "
			+ "where PROPERTY_CODE = ? "
			+ "and to_number(PROPERTY_VALUE) < to_number(?)";

	/** 初期化 */
	@PostConstruct
	public abstract void init();

	/** 採番マスタの現在値を、各エンティティのプライマリキー最大値と同期させる */
	@Transactional
	public void syncNumbering(Map<Class<?>, List<?>> allEntities) {

		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		final String userCode = sessionHolder.getLoginInfo().getUserCode();
		final String ipAddr = sessionHolder.getWfUserRole().getIpAddress();

		for (Class<?> clazz : allEntities.keySet()) {
			if (WfmNameLookup.class == clazz)
				continue;

			List<?> entities = allEntities.get(clazz);
			long max = 0L;
			if (entities != null && !allEntities.isEmpty()) {
				// エンティティのIDの最大値を抜き出す
				max = entities.stream()
						.map(entity -> (Long)getPropertyValue(entity, "id"))
						.filter(id -> id != null)
						.max((e1, e2) -> compareTo(e1, e2))
						.orElse(0L);
			}
			final String propertyCode =  propertyCodes.get(clazz);
			if (isEmpty(propertyCode)) {
				throw new IllegalArgumentException("Class[" + clazz.getName() + "]のWFM_PROPERTY.PROPERTY_CODEが未定義です");
			}
			else {
				// WFM_PROPERTY.PROPERTY_VALUEをIDの最大値で更新
				final Object[] params = {
						// set
						String.valueOf(max),
						timestamp(),
						corporationCode,
						userCode,
						ipAddr,
						// where
						propertyCode,
						max
				};
				try {
					NativeSqlUtils.execSql(conn, SQL, params);
				} catch (SQLException e) {
					throw new InternalServerErrorException("WFM_PROPERTYの採番値を同期出来ませんでした", e);
				}
			}
		}
	}
}
