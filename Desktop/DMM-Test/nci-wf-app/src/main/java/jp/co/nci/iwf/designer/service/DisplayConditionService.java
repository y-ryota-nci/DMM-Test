package jp.co.nci.iwf.designer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsDc;

/**
 * 表示条件サービス
 */
@BizLogic
public class DisplayConditionService extends BaseRepository {

	/**
	 * コンテナID＋表示条件IDをキーにパーツ表示条件を抽出し、パーツIDをキーとした表示区分Mapを返す
	 * @param containerIds
	 * @param dcId
	 * @return
	 */
	public Map<Long, Integer> getMwmpartsDcMap(Collection<Long> containerIds, Long dcId) {
		return getMwmPartsDc(containerIds, dcId)
				.stream()
				.collect(Collectors.toMap(MwmPartsDc::getPartsId, pdc -> pdc.getDcType()));
	}

	/**
	 * コンテナID＋表示条件IDをキーにパーツ表示条件を抽出
	 * @param containerIds
	 * @param dcId
	 * @return
	 */
	private List<MwmPartsDc> getMwmPartsDc(Collection<Long> containerIds, Long dcId) {
		if (dcId == null || containerIds.isEmpty()) {
			return new ArrayList<>();
		}

		// パラメータ
		final List<Object> params = new ArrayList<>();
		params.add(dcId);
		for (Long containerId : containerIds)
			params.add(containerId);

		// コンテナIDへのIN句
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("VD0115_01"));
		sql.append(" and ").append(toInListSql("P.CONTAINER_ID", containerIds.size()));

		return select(MwmPartsDc.class, sql.toString(), params.toArray());
	}
}
