package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.endpoint.vd.vd0310.entity.BlockDisplayEntity;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.TrayEntity;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * コンテナ一覧リポジトリ
 */
@ApplicationScoped
public class Vd0310Repository extends BaseRepository {

	public TrayEntity getTrayEntity(String corporationCode, Long processId, Long activityId, Long screenProcessId, Collection<String> menuRoleCds, String localeCode) {
		final List<Object> params = new ArrayList<>();
		params.add(localeCode);

		StringBuilder sql = new StringBuilder();
		if (isNotEmpty(processId)) {
			// 既存
			params.add(corporationCode);
			params.add(processId);
			params.add(activityId);
			sql.append(getSql("VD0310_01"));
		} else if (isNotEmpty(screenProcessId)) {
			// 新規起票
			StringBuilder replace = new StringBuilder();
			for (String menuRoleCd : menuRoleCds) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(menuRoleCd);
			}
			sql.append(getSql("VD0310_02").replaceFirst("###REPLACE###", replace.toString()));
			params.add(screenProcessId);
		}
		List<TrayEntity> result = select(TrayEntity.class, sql, params.toArray());
		if (result == null || result.isEmpty()) {
			return null;
		}
		final TrayEntity trayEntity = result.get(0);
		em.detach(trayEntity);
		return trayEntity;
	}

	public List<BlockDisplayEntity> getBlockDisplayList(String corporationCode, Long dcId, Long screenProcessId, String localeCode) {
		final StringBuilder sql = new StringBuilder(getSql("VD0310_03"));
		final List<Object> params = new ArrayList<>();
		params.add(dcId);
		params.add(dcId);
		params.add(screenProcessId);
		params.add(corporationCode);
		params.add(localeCode);
		List<BlockDisplayEntity> result = select(BlockDisplayEntity.class, sql.toString(), params.toArray());
		if (result == null) {
			result = new ArrayList<>();
		}
		result.stream().forEach(e -> em.detach(e));
		return result;
	}
}
