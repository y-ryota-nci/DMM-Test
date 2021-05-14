package jp.co.nci.iwf.endpoint.wl.wl0011;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigResult;

/**
 * トレイ編集リポジトリ
 */
@ApplicationScoped
public class Wl0011Repository extends BaseRepository {
	@Inject private NumberingService numbering;

	/** 業務管理項目名一覧を抽出 */
	public List<MwmBusinessInfoName> getBusinessInfos(String localeCode, String corporationCode) {
		final Object[] params = { localeCode, corporationCode };
		return select(MwmBusinessInfoName.class, getSql("WL0011_02"), params);
	}

	/** トレイ設定を抽出 */
	public Wl0011Entity getEntity(long trayConfigId, String localeCode) {
		final Object[] params = { localeCode, trayConfigId };
		final Wl0011Entity entity = selectOne(Wl0011Entity.class, getSql("WL0011_03"), params);
		return entity;
	}

	/** トレイ設定検索条件を抽出 */
	public List<Wl0011Condition> getConditions(long trayConfigId, String localeCode) {
		final Object[] params = { localeCode, trayConfigId };
		return select(Wl0011Condition.class, getSql("WL0011_04"), params);
	}

	/** トレイ設定検索結果を抽出 */
	public List<Wl0011Result> getResults(long trayConfigId, String localeCode) {
		final Object[] params = { localeCode, trayConfigId };
		return select(Wl0011Result.class, getSql("WL0011_05"), params);
	}

	public MwmTrayConfig getMwmTrayConfig(long trayConfigId) {
		return em.find(MwmTrayConfig.class, trayConfigId);
	}

	public List<MwmTrayConfigCondition> getMwmTrayConfigConditions(long trayConfigId) {
		final Object[] params = { trayConfigId };
		return select(MwmTrayConfigCondition.class, getSql("WL0011_06"), params);
	}

	public Collection<MwmTrayConfigResult> getMwmTrayConfigResults(long trayConfigId) {
		final Object[] params = { trayConfigId };
		return select(MwmTrayConfigResult.class, getSql("WL0011_07"), params);
	}

	private String defaultIfEmpty(String s, String defaultValue) {
		return isEmpty(s) ? defaultValue : s;
	}

	/** トレイ表示設定の差分更新 */
	public long saveEntity(Wl0011SaveRequest req, String localeCode, String corporationCode) {
		Wl0011Entity input = req.entity;
		MwmTrayConfig tc = null;
		if (input.trayConfigId == 0L) {
			tc = new MwmTrayConfig();
			tc.setTrayConfigId(numbering.newPK(MwmTrayConfig.class));
			tc.setCorporationCode(corporationCode);
		}
		else {
			tc = getMwmTrayConfig(input.trayConfigId);
		}
		tc.setCorporationCode(input.corporationCode);
		tc.setTrayConfigCode(input.trayConfigCode);
		tc.setDeleteFlag(DeleteFlag.OFF);
		tc.setPageSize(input.pageSize);
		tc.setSortOrder(input.sortOrder);
		tc.setSystemFlag(defaultIfEmpty(input.systemFlag, CommonFlag.OFF));
		tc.setTrayConfigName(input.trayConfigName);
		tc.setPersonalUseFlag(input.personalUseFlag);
		tc.setVersion(input.version);

		if (tc.getVersion() == null)
			em.persist(tc);

		return tc.getTrayConfigId();
	}

	/** 検索条件の差分更新 */
	public void saveConditions(Wl0011SaveRequest req, long trayConfigId) {
		final Map<String, MwmTrayConfigCondition> currents =
				getMwmTrayConfigConditions(trayConfigId)
				.stream()
				.collect(Collectors.toMap(c -> c.getBusinessInfoCode(), c -> c));
		for (Wl0011Condition input : req.conditions) {
			// 使用中のモノを消し込んでいく
			MwmTrayConfigCondition current = currents.remove(input.businessInfoCode);
			if (current == null) {
				current = new MwmTrayConfigCondition();
				current.setTrayConfigConditionId(numbering.newPK(MwmTrayConfigCondition.class));
				current.setTrayConfigId(trayConfigId);
				current.setCorporationCode(input.corporationCode);
			}
			current.setBusinessInfoCode(input.businessInfoCode);
			current.setConditionMatchType(input.conditionMatchType);
			current.setDeleteFlag(DeleteFlag.OFF);
			current.setSortOrder(input.sortOrder);
			current.setTrayInitValue1(input.trayInitValue1);
			current.setTrayInitValue2(input.trayInitValue2);
			current.setTrayInitValue3(input.trayInitValue3);
			current.setTrayInitValue4(input.trayInitValue4);

			if (current.getVersion() == null)
				em.persist(current);
		}
		// 残余は不要なので削除
		for (MwmTrayConfigCondition current : currents.values()) {
			em.remove(current);
		}
	}


	/** 検索結果の差分更新 */
	public void saveResults(Wl0011SaveRequest req, long trayConfigId) {
		final Map<String, MwmTrayConfigResult> currents =
				getMwmTrayConfigResults(trayConfigId)
				.stream()
				.collect(Collectors.toMap(c -> c.getBusinessInfoCode(), c -> c));
		for (Wl0011Result input : req.results) {
			// 使用中のモノを消し込んでいく
			MwmTrayConfigResult current = currents.remove(input.businessInfoCode);
			if (current == null) {
				current = new MwmTrayConfigResult();
				current.setTrayConfigResultId(numbering.newPK(MwmTrayConfigResult.class));
				current.setTrayConfigId(trayConfigId);
				current.setCorporationCode(input.corporationCode);
			}
			current.setAlignType(input.alignType);
			current.setColWidth(input.colWidth);
			current.setBusinessInfoCode(input.businessInfoCode);
			current.setDeleteFlag(DeleteFlag.OFF);
			current.setInitialSortDescFlag(defaultIfEmpty(input.initialSortDescFlag, CommonFlag.OFF));
			current.setInitialSortFlag(defaultIfEmpty(input.initialSortFlag, CommonFlag.OFF));
			current.setLinkFlag(defaultIfEmpty(input.linkFlag, CommonFlag.OFF));
			current.setSortOrder(input.sortOrder);

			if (current.getVersion() == null)
				em.persist(current);
		}
		// 残余は不要なので削除
		for (MwmTrayConfigResult current : currents.values()) {
			em.remove(current);
		}
	}

	public void removeMwmTrayConfig(Long trayConfigId, Long version) {
		MwmTrayConfig entity = getMwmTrayConfig(trayConfigId);
		if (entity != null) {
			entity.setVersion(version);
			em.remove(entity);
		}
	}

	public void removeMwmTrayConfigCondition(Long trayConfigId) {
		getMwmTrayConfigConditions(trayConfigId).forEach(entity -> em.remove(entity));
	}

	public void removeMwmTrayConfigResult(Long trayConfigId) {
		getMwmTrayConfigResults(trayConfigId).forEach(entity -> em.remove(entity));
	}

	/** 自レコードの以外で同一トレイ設定コード存在するか */
	public boolean existsTrayCongifCode(String corporationCode, String trayConfigCode, long excludeTrayConfigId) {
		final Object[] params = { corporationCode, trayConfigCode, excludeTrayConfigId };
		final int cnt = count(getSql("WL0011_11"), params);
		return cnt > 0;
	}
}
