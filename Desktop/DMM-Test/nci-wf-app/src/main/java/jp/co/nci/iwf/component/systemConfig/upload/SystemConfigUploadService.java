package jp.co.nci.iwf.component.systemConfig.upload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.api.param.input.InsertWfmCorporationPropertyInParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmCorporationPropertyOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.custom.WfmCorporationPropertyEx;
import jp.co.nci.integrated_workflow.param.input.GetNextMasterIdInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.component.systemConfig.download.SystemConfigDownloadDto;
import jp.co.nci.iwf.component.systemConfig.download.SystemConfigDownloadService;
import jp.co.nci.iwf.component.upload.BaseUploadRepository;
import jp.co.nci.iwf.endpoint.up.up0060.Up0060Request;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailConfig;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationProperty;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorporationPropertyPK;

/**
 * システム環境設定アップロードサービス
 */
@ApplicationScoped
public class SystemConfigUploadService extends BaseUploadRepository {
	@Inject private SystemConfigDownloadService downloader;
	@Inject private Logger log;
	@Inject private SystemConfigUploadSyncIdService syncIdService;
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;
	@Inject private CorporationPropertyService corpProp;


	/**
	 * メールテンプレート定義アップロードによるデータ登録処理
	 * @param newCorporationCode
	 * @param req
	 * @param dto
	 */
	@Transactional
	public void persist(String newCorporationCode, Up0060Request req, SystemConfigDownloadDto dto) {
		// ◇ユーザ指定内容で企業コードを書き換え
		replaceUniqueKeys(newCorporationCode, dto);

		// ★実際の更新処理：採番マスタの現在値を、各エンティティのID最大値と同期させる
		syncMwmNumbering(dto);
		syncIdService.syncNumbering(toWfmEntities(dto));

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		SystemConfigDownloadDto currentDto = downloader.createDto(newCorporationCode);
		upsertAll(newCorporationCode, req, dto, currentDto);

		// 企業別プロパティが更新されたのでキャッシュを初期化
		corpProp.clearCache();
	}

	/** WFM系エンティティのデータ登録処理 */
	private void upsertAll(String newCorporationCode, Up0060Request req, SystemConfigDownloadDto dto, SystemConfigDownloadDto currentDto) {

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		// 会社別プロパティマスタ
		Map<String, Map<Long, Long>> allChangeIds = new HashMap<>();
		if (req.enableCorporationProperty){
			Map<WfmCorporationPropertyPK, WfmCorporationProperty> currentMap = currentDto.corporationPropertyList
					.stream()
					.collect(Collectors.toMap(WfmCorporationProperty::getPk, cp -> cp));
			Map<Long, Long> changeIds = new HashMap<>();
			for (WfmCorporationProperty upload : dto.corporationPropertyList) {
				WfmCorporationProperty current = currentMap.remove(upload.getPk());
				Long oldId = upload.getId();
				Long newId = null;
				if (current == null) {
					newId = insertWfmCorporationProperty(upload);
				} else {
					newId = updateWfmCorporationProperty(upload, current);
				}
				changeIds.put(oldId, newId);
			}
			allChangeIds.put("WFM_CORPORATION_PROPERTY", changeIds);
		}
		// プロパティマスタ
		if (req.enableCorpPropMaster) {
			Map<String, WfmCorpPropMaster> currentMap = currentDto.corpPropMasterList
					.stream()
					.collect(Collectors.toMap(WfmCorpPropMaster::getPropertyCode, cpm -> cpm));
			Map<Long, Long> changeIds = new HashMap<>();
			for (WfmCorpPropMaster upload : dto.corpPropMasterList) {
				WfmCorpPropMaster current = currentMap.remove(upload.getPropertyCode());
				Long oldId = upload.getId();
				Long newId = null;
				if (current == null) {
					newId = insertWfmCorpPropMaster(upload);
				} else {
					newId = updateWfmCorpPropMaster(upload, current);
				}
				changeIds.put(oldId, newId);
			}
			allChangeIds.put("WFM_CORP_PROP_MASTER", changeIds);
		}
		// メール環境設定
		if (req.enableMailConfig) {
			Map<String, MwmMailConfig> currentMap = currentDto.mailConfigList
					.stream()
					.collect(Collectors.toMap(MwmMailConfig::getConfigCode, mc -> mc));
			Map<Long, Long> changeIds = new HashMap<>();
			for (MwmMailConfig upload : dto.mailConfigList) {
				MwmMailConfig current = currentMap.remove(upload.getConfigCode());
				Long oldId = upload.getMailConfigId();
				Long newId = null;
				if (current == null) {
					newId = insertMwmMailConfig(upload);
				} else {
					newId = updateMwmMailConfig(upload, current);
				}
				changeIds.put(oldId, newId);
			}
			allChangeIds.put("MWM_MAIL_CONFIG", changeIds);
		}

		// 名称ルックアップ
		// 名称ルックアップの対応テーブル側でIDが置換されていれば、名称ルックアップ側のIDも書き換え
		for (WfmNameLookup lookup : dto.nameLookupList) {
			String tableName = lookup.getId().getTableName();
			if (!req.enableCorporationProperty && eq(tableName, "WFM_CORPORATION_PROPERTY"))
				continue;
			if (!req.enableCorpPropMaster && eq(tableName, "WFM_CORP_PROP_MASTER"))
				continue;
			if (!req.enableMailConfig && eq(tableName, "MWM_MAIL_CONFIG"))
				continue;

			Long oldId = lookup.getId().getId();
			Map<Long, Long> changes = allChangeIds.get(tableName);
			if (changes != null) {
				if (changes.containsKey(oldId)) {
					Long newId = changes.get(oldId);
					if (newId != null)
						lookup.getId().setId(newId);
				}
			}
			em.merge(lookup);
		}
	}

	/** メール環境設定マスタの更新 */
	private Long updateMwmMailConfig(MwmMailConfig entity, MwmMailConfig current) {
		current.setConfigName(entity.getConfigName());
		current.setConfigValue(entity.getConfigValue());
		return current.getMailConfigId();
	}

	/** メール環境設定マスタのインサート */
	private Long insertMwmMailConfig(MwmMailConfig entity) {
		entity.setMailConfigId(numbering.newPK(MwmMailConfig.class));
		em.persist(entity);
		return entity.getMailConfigId();
	}

	/** プロパティマスタの更新 */
	private Long updateWfmCorpPropMaster(WfmCorpPropMaster entity, WfmCorpPropMaster current) {
		current.setDefaultValue(entity.getDefaultValue());
		return current.getId();
	}

	/** プロパティマスタのインサート */
	private Long insertWfmCorpPropMaster(WfmCorpPropMaster upload) {
		// 採番
		final GetNextMasterIdInParam in = new GetNextMasterIdInParam();
		in.setTableName("WFM_CORP_PROP_MASTER");
		in.setWfUserRole(sessionHolder.getWfUserRole());
		long id = wf.getNextMasterId(in).getNextMasterId();

		// インサート（WFM_CORP_PROP_MASTERは INSERT用APIがないので、JPA経由で）
		final WfmCorpPropMaster entity = new WfmCorpPropMaster();
		copyProperties(upload, entity);
		entity.setId(id);
		em.persist(entity);

		return id;
	}

	/** 会社別プロパティマスタの更新 */
	private long updateWfmCorporationProperty(WfmCorporationProperty entity, WfmCorporationProperty current) {
		// JPA経由で更新
		current.setPropertyValue(entity.getPropertyValue());
		em.merge(current);
		return current.getId();
	}

	/** 会社別プロパティマスタのインサート */
	private long insertWfmCorporationProperty(WfmCorporationProperty entity) {
		// WF APIでインサート
		final WfmCorporationPropertyEx current = new WfmCorporationPropertyEx();
		copyProperties(entity, current);
		current.setCorporationCode(entity.getPk().getCorporationCode());
		current.setPropertyCode(entity.getPk().getPropertyCode());
		current.setId(null);
		current.setTimestampUpdated(null);

		final InsertWfmCorporationPropertyInParam in = new InsertWfmCorporationPropertyInParam();
		in.setWfmCorporationProperty(current);
		in.setWfUserRole(sessionHolder.getWfUserRole());

		final InsertWfmCorporationPropertyOutParam out = wf.insertWfmCorporationProperty(in);
		if (!eq(ReturnCode.SUCCESS, out.getReturnCode()))
			throw new WfException(out);

		return out.getWfmCorporationProperty().getId();
	}

	/** WFM系のエンティティをMap化 */
	private Map<Class<?>, List<?>> toWfmEntities(SystemConfigDownloadDto dto) {
		final Map<Class<?>, List<?>> map = new HashMap<>();
		map.put(WfmCorpPropMaster.class, dto.corpPropMasterList);
		map.put(WfmCorporationProperty.class, dto.corporationPropertyList);
		map.put(WfmNameLookup.class, dto.nameLookupList);

		return map;
	}

	/** ユーザ指定内容でユニークキーを書き換え */
	private void replaceUniqueKeys(String newCorporationCode, SystemConfigDownloadDto dto) {
		// 企業プロパティマスタ
		for (WfmCorporationProperty cp : dto.corporationPropertyList)
				cp.getPk().setCorporationCode(newCorporationCode);
	}

	/**
	 * 採番マスタの現在値を、各エンティティのプライマリキー最大値と同期させる
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 */
	private void syncMwmNumbering(SystemConfigDownloadDto dto) {
		log.debug("START syncMwmNumbering()");

		Map<Class<?>, List<? extends MwmBaseJpaEntity>> map = new HashMap<>();
		map.put(MwmMailConfig.class, dto.mailConfigList);

		// DTOのフィールドがリストであればアップロード用エンティティとみなす
		for (List<? extends MwmBaseJpaEntity> entities : map.values()) {
			if (entities == null || entities.isEmpty())
				continue;

			// 各エンティティのプライマリキーの最大値を求める
			final Class<? extends MwmBaseJpaEntity> clazz = entities.get(0).getClass();
			final String pkFieldName = jpaEntityDef.getPkFieldName(clazz);
			final long max = entities.stream()
					.map(e -> (Long)getPropertyValue(e, pkFieldName))
					.filter(id -> id != null)
					.max((v1, v2) -> compareTo(v1, v2))
					.orElse(0L);

			// エンティティのプライマリキーの最大値で「採番マスタの現在値」を更新
			// （採番マスタ.現在値＞新しい値なら更新されないよ）
			if (max > 0L)
				numbering.sync(clazz, max);
		};
		log.debug("END syncMwmNumbering()");
	}

}
