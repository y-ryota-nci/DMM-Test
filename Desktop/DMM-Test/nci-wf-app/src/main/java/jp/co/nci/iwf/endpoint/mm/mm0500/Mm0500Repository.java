package jp.co.nci.iwf.endpoint.mm.mm0500;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.JpaEntityDefService;
import jp.co.nci.iwf.component.MultilingalService;
import jp.co.nci.iwf.component.NumberingService;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocTrayConfigResult;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookup;
import jp.co.nci.iwf.jpa.entity.mw.MwmLookupGroup;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmOption;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigResult;

/**
 * マスター初期値設定のリポジトリ
 */
@ApplicationScoped
public class Mm0500Repository extends BaseRepository {
	@Inject private NumberingService numbering;
	@Inject private MultilingalService multi;
	@Inject private JpaEntityDefService entityDef;

	/** 検索 */
	public List<Mm0500Entity> search(String srcCorporationCode, String localeCode) {
		final Object[] params = { srcCorporationCode, localeCode };
		final List<Mm0500Entity> list = select(Mm0500Entity.class, getSql("MM0500_01"), params);
		em.clear();
		return list;
	}

	/** 指定企業に対して、不足しているレコードをASPをベースにINSERT～SELECT */
	public void insertSelectMwm(String srcCorporationCode, String destCorporationCode, String localeCode) {
		// 多言語対応
		final List<Mm0500Multi> multilinguals = new ArrayList<>();

		// MWM_LOOKUP_GROUP
		insertSelectMwmLookupGroup(srcCorporationCode, destCorporationCode);

		// MWM_LOOKUP
		insertSelectMwmLookup(srcCorporationCode, destCorporationCode);
		// MWM_BUSINESS_INFO_NAME
		insertSelectMwmBusinessInfoName(srcCorporationCode, destCorporationCode, multilinguals);

		// MWM_DOC_BUSINESS_INFO_NAME
		insertSelectMwmDocBusinessInfoName(srcCorporationCode, destCorporationCode, multilinguals);
		// トレイ設定関連
		final Object[] params = { srcCorporationCode, destCorporationCode };
		List<MwmTrayConfig> trayConfigs = select(MwmTrayConfig.class, getSql("MM0500_06"), params);
		if (trayConfigs.size() > 0) {
			// 採番されたトレイ設定ID（必要分をあらかじめ一括採番している）
			long newTrayConfigId = numbering.newPK(MwmTrayConfig.class, trayConfigs.size());

			for (MwmTrayConfig src : trayConfigs) {
				// MWM_TRAY_CONFIG
				insertSelectMwmTrayConfig(destCorporationCode, newTrayConfigId, src, multilinguals);

				// MWM_TRAY_CONFIG_CONDITION
				insertSelectMwmTrayConfigCondition(destCorporationCode, newTrayConfigId, src);

				// MWM_TRAY_CONFIG_RESULT
				insertSelectMwmTrayConfigResult(destCorporationCode, newTrayConfigId, src);

				newTrayConfigId++;	// 一括採番しているので、次の値はインクリメントするだけで求められる
			}
		}
		// 文書管理トレイ設定関連
		List<MwmDocTrayConfig> docTrayConfigs = select(MwmDocTrayConfig.class, getSql("MM0500_09"), params);
		if (docTrayConfigs.size() > 0) {
			// 採番されたトレイ設定ID（必要分をあらかじめ一括採番している）
			long newDocTrayConfigId = numbering.newPK(MwmDocTrayConfig.class, docTrayConfigs.size());

			for (MwmDocTrayConfig src : docTrayConfigs) {
				// MWM_DOC_TRAY_CONFIG
				insertSelectMwmDocTrayConfig(destCorporationCode, newDocTrayConfigId, src, multilinguals);

				// MWM_DOC_TRAY_CONFIG_CONDITION
				insertSelectMwmDocTrayConfigCondition(destCorporationCode, newDocTrayConfigId, src);

				// MWM_DOC_TRAY_CONFIG_RESULT
				insertSelectMwmDocTrayConfigResult(destCorporationCode, newDocTrayConfigId, src);

				newDocTrayConfigId++;	// 一括採番しているので、次の値はインクリメントするだけで求められる
			}
		}
		// メール変数マスタ
		insertSelectMwmMailVariable(srcCorporationCode, destCorporationCode, multilinguals);
		// 選択肢マスタ
		insertSelectMwmOption(srcCorporationCode, destCorporationCode, multilinguals);
		// 選択肢項目マスタ
		insertSelectMwmOptionItem(srcCorporationCode, destCorporationCode, multilinguals);
		// 多言語
		insertMwmMultilingual(multilinguals);
	}

	/** 多言語対応マスタのINSERT */
	private void insertMwmMultilingual(List<Mm0500Multi> srcList) {
		final Map<String, Map<String, Map<Long, Long>>> byTable =
		srcList.stream().collect(
				Collectors.groupingBy(s -> s.tableName,
				Collectors.groupingBy(s -> s.columnName,
				Collectors.toMap(s -> s.idSrc, s -> s.idDest))));

		// コピー元の多言語マスタを抽出
		final List<MwmMultilingual> inserts = new ArrayList<>();
		for (String tableName : byTable.keySet()) {
			Map<String, Map<Long, Long>> byColumn = byTable.get(tableName);
			for (String columnName : byColumn.keySet()) {
				Map<Long, Long> byId = byColumn.get(columnName);
				for (final Long idSrc : byId.keySet()) {
					final Long idDest = byId.get(idSrc);
					multi.get(tableName, idSrc, columnName).forEach(s -> {
						final MwmMultilingual m = new MwmMultilingual();
						m.setTableName(s.getTableName());
						m.setId(idDest);
						m.setColumnName(s.getColumnName());
						m.setLocaleCode(s.getLocaleCode());
						m.setDeleteFlag(DeleteFlag.OFF);
						m.setVal(s.getVal());
						inserts.add(m);
					});
				}
			}
		}
		// 一括インサート
		if (!inserts.isEmpty()) {
			long multilingualId = numbering.newPK(MwmMultilingual.class, inserts.size());
			for (MwmMultilingual insert : inserts) {
				insert.setMultilingualId(multilingualId++);
				em.persist(insert);
			}
		}
	}

	/** 選択肢項目マスタのINSERT～SELECT */
	private void insertSelectMwmOptionItem(String srcCorporationCode, String destCorporationCode, List<Mm0500Multi> multilinguals) {
		final Object[] params = { destCorporationCode, srcCorporationCode };
		final List<MwmOptionItem> options = select(MwmOptionItem.class, getSql("MM0500_14"), params);

		// optionItemIdがコピー元のなので、JPAの制御から外す
		options.forEach(o -> em.detach(o));

		if (options.size() > 0) {
			long optionItemId = numbering.newPK(MwmOptionItem.class, options.size());
			for (MwmOptionItem src : options) {

				MwmOptionItem dest = new MwmOptionItem();
				copyProperties(src, dest);
				dest.setOptionId(src.getOptionId());
				dest.setOptionItemId(optionItemId++);
				dest.setVersion(null);
				em.persist(dest);

				// 多言語
				multilinguals.add(toMutlilingual(src, dest, "LABEL"));
			}
		}
		em.flush();
	}

	/** 選択肢マスタのINSERT～SELECT */
	private void insertSelectMwmOption(String srcCorporationCode, String destCorporationCode, List<Mm0500Multi> multilinguals) {
		final Object[] params = { srcCorporationCode, destCorporationCode };
		final List<MwmOption> options = select(MwmOption.class, getSql("MM0500_13"), params);

		// optionItemIdがコピー元のなので、JPAの制御から外す
		options.forEach(o -> em.detach(o));

		if (options.size() > 0) {
			long optionId = numbering.newPK(MwmOption.class, options.size());
			for (MwmOption src : options) {
				MwmOption dest = new MwmOption();
				copyProperties(src, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setOptionId(optionId++);
				dest.setVersion(null);
				em.persist(dest);

				// 多言語
				multilinguals.add(toMutlilingual(src, dest, "OPTION_NAME"));
			}
		}
		em.flush();	// 子テーブルであるMWM_OPTION_ITEMの存在チェックにつかうので、先に更新SQLを発行してやる
	}

	/** メール変数マスタのINSERT～SELECT  */
	private void insertSelectMwmMailVariable(String srcCorporationCode, String destCorporationCode, List<Mm0500Multi> multilinguals) {
		final Object[] params = { LoginInfo.get().getLocaleCode(), srcCorporationCode, destCorporationCode };
		List<MwmMailVariable> variables = select(MwmMailVariable.class, getSql("MM0500_12"), params);
		if (variables.size() > 0) {
			long mailVariableId = numbering.newPK(MwmMailVariable.class, variables.size());
			for (MwmMailVariable src : variables) {
				MwmMailVariable dest = new MwmMailVariable();
				copyProperties(src, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setMailVariableId(mailVariableId++);
				dest.setVersion(null);
				em.persist(dest);

				// 多言語
				multilinguals.add(toMutlilingual(src, dest, "MAIL_VARIABLE_VALUE"));
				multilinguals.add(toMutlilingual(src, dest, "MAIL_VARIABLE_LABEL"));
			}
		}
	}

	/** 文書管理トレイ設定検索結果マスタのINSERT～SELECT */
	private void insertSelectMwmDocTrayConfigResult(String corporationCode, long newDocTrayConfigId, MwmDocTrayConfig src) {
		Object[] args = { src.getDocTrayConfigId(), corporationCode, newDocTrayConfigId };
		List<MwmDocTrayConfigResult> results = select(MwmDocTrayConfigResult.class, getSql("MM0500_11"), args);
		if (results.size() > 0) {
			long trayConfigResultId = numbering.newPK(MwmDocTrayConfigResult.class, results.size());
			for (MwmDocTrayConfigResult r : results) {
				MwmDocTrayConfigResult dest = new MwmDocTrayConfigResult();
				copyProperties(r, dest);
				dest.setCorporationCode(corporationCode);
				dest.setDocTrayConfigId(newDocTrayConfigId);
				dest.setDocTrayConfigResultId(trayConfigResultId++);
				dest.setVersion(null);
				em.persist(dest);
			}
		}
	}

	/** 文書管理トレイ設定検索条件マスタのINSERT～SELECT */
	private void insertSelectMwmDocTrayConfigCondition(String corporationCode, long newDocTrayConfigId, MwmDocTrayConfig src) {
		Object[] args = { src.getDocTrayConfigId(), corporationCode, newDocTrayConfigId };
		List<MwmDocTrayConfigCondition> conditions = select(MwmDocTrayConfigCondition.class, getSql("MM0500_10"), args);
		if (conditions.size() > 0) {
			long trayDocConfigConditionId = numbering.newPK(MwmDocTrayConfigCondition.class, conditions.size());
			for (MwmDocTrayConfigCondition c : conditions) {
				MwmDocTrayConfigCondition dest = new MwmDocTrayConfigCondition();
				copyProperties(c, dest);
				dest.setCorporationCode(corporationCode);
				dest.setDocTrayConfigId(newDocTrayConfigId);
				dest.setDocTrayConfigConditionId(trayDocConfigConditionId++);
				dest.setVersion(null);
				em.persist(dest);
			}
		}
	}

	/** 文書管理トレイ設定マスタのINSERT～SELECT */
	private void insertSelectMwmDocTrayConfig(String corporationCode, long newDocTrayConfigId, MwmDocTrayConfig src, List<Mm0500Multi> multilinguals) {
		MwmDocTrayConfig dest = new MwmDocTrayConfig();
		copyProperties(src, dest);
		dest.setCorporationCode(corporationCode);
		dest.setDocTrayConfigId(newDocTrayConfigId);
		dest.setVersion(null);
		em.persist(dest);

		// 多言語
		multilinguals.add(toMutlilingual(src, dest, "DOC_TRAY_CONFIG_NAME"));
	}

	/** トレイ設定検索結果マスタのINSERT～SELECT */
	private void insertSelectMwmTrayConfigResult(String corporationCode, long newTrayConfigId, MwmTrayConfig src) {
		Object[] args = { src.getTrayConfigId(), corporationCode, newTrayConfigId };
		List<MwmTrayConfigResult> results = select(MwmTrayConfigResult.class, getSql("MM0500_08"), args);
		if (results.size() > 0) {
			long trayConfigResultId = numbering.newPK(MwmTrayConfigResult.class, results.size());
			for (MwmTrayConfigResult r : results) {
				MwmTrayConfigResult dest = new MwmTrayConfigResult();
				copyProperties(r, dest);
				dest.setCorporationCode(corporationCode);
				dest.setTrayConfigId(newTrayConfigId);
				dest.setTrayConfigResultId(trayConfigResultId++);
				dest.setVersion(null);
				em.persist(dest);
			}
		}
	}

	/** トレイ設定検索条件マスタのINSERT～SELECT */
	private void insertSelectMwmTrayConfigCondition(String destCorporationCode, long newTrayConfigId, MwmTrayConfig src) {
		Object[] args = { src.getTrayConfigId(), destCorporationCode, newTrayConfigId };
		List<MwmTrayConfigCondition> conditions = select(MwmTrayConfigCondition.class, getSql("MM0500_07"), args);
		if (conditions.size() > 0) {
			long trayConfigConditionId = numbering.newPK(MwmTrayConfigCondition.class, conditions.size());
			for (MwmTrayConfigCondition c : conditions) {
				MwmTrayConfigCondition dest = new MwmTrayConfigCondition();
				copyProperties(c, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setTrayConfigId(newTrayConfigId);
				dest.setTrayConfigConditionId(trayConfigConditionId++);
				dest.setVersion(null);
				em.persist(dest);
			}
		}
	}

	/** トレイ設定マスタのINSERT～SELECT */
	private void insertSelectMwmTrayConfig(String destCorporationCode, long newTrayConfigId, MwmTrayConfig src, List<Mm0500Multi> multilinguals) {
		MwmTrayConfig dest = new MwmTrayConfig();
		copyProperties(src, dest);
		dest.setCorporationCode(destCorporationCode);
		dest.setTrayConfigId(newTrayConfigId);
		dest.setVersion(null);
		em.persist(dest);

		// 多言語
		multilinguals.add(toMutlilingual(src, dest, "TRAY_CONFIG_NAME"));
	}

	/** 文書管理業務管理項目名称マスタのINSERT～SELECT */
	private void insertSelectMwmDocBusinessInfoName(String srcCorporationCode, String destCorporationCode, List<Mm0500Multi> multilinguals) {
		final Object[] params = { srcCorporationCode, destCorporationCode };
		List<MwmDocBusinessInfoName> docBusinessInfos = select(MwmDocBusinessInfoName.class, getSql("MM0500_05"), params);
		if (docBusinessInfos.size() > 0) {
			long docBusinessInfoNameId = numbering.newPK(MwmDocBusinessInfoName.class, docBusinessInfos.size());
			for (MwmDocBusinessInfoName src : docBusinessInfos) {
				MwmDocBusinessInfoName dest = new MwmDocBusinessInfoName();
				copyProperties(src, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setDocBusinessInfoNameId(docBusinessInfoNameId++);
				dest.setVersion(null);
				em.persist(dest);

				// 多言語
				multilinguals.add(toMutlilingual(src, dest, "DOC_BUSINESS_INFO_NAME"));
			}
		}
	}

	/** 業務管理項目名称マスタのINSERT～SELECT */
	private void insertSelectMwmBusinessInfoName(String srcCorporationCode, String destCorporationCode, List<Mm0500Multi> multilinguals) {
		final Object[] params = { srcCorporationCode, destCorporationCode };
		List<MwmBusinessInfoName> businessInfos = select(MwmBusinessInfoName.class, getSql("MM0500_04"), params);
		if (businessInfos.size() > 0) {
			long businessInfoNameId = numbering.newPK(MwmBusinessInfoName.class, businessInfos.size());
			for (MwmBusinessInfoName src : businessInfos) {
				MwmBusinessInfoName dest = new MwmBusinessInfoName();
				copyProperties(src, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setBusinessInfoNameId(businessInfoNameId++);
				dest.setVersion(null);
				em.persist(dest);

				// 多言語
				multilinguals.add(toMutlilingual(src, dest, "BUSINESS_INFO_NAME"));
			}
		}
	}

	/** 画面ルックアップマスタのINSERT～SELECT */
	private void insertSelectMwmLookup(String srcCorporationCode, String destCorporationCode) {
		final Object[] params = { srcCorporationCode, destCorporationCode };
		List<MwmLookup> lookups = select(MwmLookup.class, getSql("MM0500_03"), params);
		if (lookups.size() > 0) {
			long screenLookupId = numbering.newPK(MwmLookup.class, lookups.size());
			for (MwmLookup src : lookups) {
				MwmLookup dest = new MwmLookup();
				copyProperties(src, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setScreenLookupId(screenLookupId++);
				dest.setVersion(null);
				em.persist(dest);
			}
		}
	}

	/** 画面ルックアップグループマスタのINSERT～SELECT */
	private Object[] insertSelectMwmLookupGroup(String srcCorporationCode, String destCorporationCode) {
		final Object[] params = { srcCorporationCode, destCorporationCode };
		List<MwmLookupGroup> groups = select(MwmLookupGroup.class, getSql("MM0500_02"), params);
		if (groups.size() > 0) {
			// 必要分を予め一括採番
			long screenLookupGroupId = numbering.newPK(MwmLookupGroup.class, groups.size());
			for (MwmLookupGroup src : groups) {
				MwmLookupGroup dest = new MwmLookupGroup();
				copyProperties(src, dest);
				dest.setCorporationCode(destCorporationCode);
				dest.setScreenLookupGroupId(screenLookupGroupId++);
				dest.setVersion(null);
				em.persist(dest);
			}
		}
		return params;
	}

	/** コピー元エンティティ、コピー先エンティティから、多言語対応ソースを生成 */
	public <E extends BaseJpaEntity> Mm0500Multi toMutlilingual(E src, E dest, String columnName) {
		String tableName = entityDef.getTableName(src.getClass());
		String pkFieldName = entityDef.getPkFieldName(src.getClass());
		Long idSrc = getPropertyValue(src, pkFieldName);
		Long idDest = getPropertyValue(dest, pkFieldName);
		final Mm0500Multi m = new Mm0500Multi(tableName, columnName, idSrc, idDest);
		return m;
	}
}
