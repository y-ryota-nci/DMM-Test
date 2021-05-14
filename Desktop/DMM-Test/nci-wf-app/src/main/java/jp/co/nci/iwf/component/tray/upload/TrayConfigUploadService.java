package jp.co.nci.iwf.component.tray.upload;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.tray.download.TrayConfigDownloadDto;
import jp.co.nci.iwf.component.tray.download.TrayConfigDownloadService;
import jp.co.nci.iwf.component.upload.BaseUploadRepository;
import jp.co.nci.iwf.component.upload.ChangedPKs;
import jp.co.nci.iwf.component.upload.ChangedPKsMap;
import jp.co.nci.iwf.endpoint.up.up0040.Up0040Request;
import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;
import jp.co.nci.iwf.jpa.entity.mw.MwmBusinessInfoName;
import jp.co.nci.iwf.jpa.entity.mw.MwmMultilingual;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfig;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigCondition;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigPerson;
import jp.co.nci.iwf.jpa.entity.mw.MwmTrayConfigResult;

/**
 * トレイ表示設定アップロードサービス
 */
@BizLogic
public class TrayConfigUploadService extends BaseUploadRepository {
	@Inject private TrayConfigPkReplacer replacer;
	@Inject private TrayConfigDownloadService downloader;
	@Inject private Logger log;


	/**
	 * トレイ表示設定アップロードによるデータ登録処理
	 * @param newCorporationCode
	 * @param req
	 * @param dto
	 */
	@Transactional
	public void persist(String newCorporationCode, Up0040Request req, TrayConfigDownloadDto dto) {
		// ★実際の更新処理：採番マスタの現在値を、各エンティティのID最大値と同期させる
		syncMwmNumbering(dto);

		// ◇ユーザ指定内容で企業コードを書き換え
		replaceUniqueKeys(dto.corporationCode, newCorporationCode, dto);

		// ◇既存データがあれば、プライマリキーを既存データのもので置換（メモリ上のみ）
		final ChangedPKsMap changedPKsMap = replacer.replaceAllPK(req, dto);

		// ◇差分更新するため、現在のデータベースの内容を抽出（メモリ上のみ）
		final TrayConfigDownloadDto currentDto = downloader.createDto(newCorporationCode);

		// ◇取込対象でないトレイ設定を処理対象から外す
		filterByUserRequest(req, dto);
		filterByUserRequest(req, currentDto);

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		upsertAll(req, dto, currentDto, changedPKsMap);
	}

	/**
	 * 採番マスタの現在値を、各エンティティのプライマリキー最大値と同期させる
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 */
	private void syncMwmNumbering(TrayConfigDownloadDto dto) {
		log.debug("START syncMwmNumbering()");

		// DTOのフィールドがリストであればアップロード用エンティティとみなす
		for (Field f : dto.getClass().getDeclaredFields()) {
			if (!List.class.isAssignableFrom(f.getType()))
				continue;
			final List<? extends MwmBaseJpaEntity> entities = getFieldValue(dto, f.getName());
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

	/** 取込対象でないトレイ設定を処理対象から外す */
	private void filterByUserRequest(Up0040Request req, TrayConfigDownloadDto dto) {
		final Set<Long> targetTrayConfigIds = dto.configList.stream()
				.filter(r -> eq(CommonFlag.OFF, r.getPersonalUseFlag()) || req.inlcudeUser )
				.map(r -> r.getTrayConfigId())
				.collect(Collectors.toSet());

		// トレイ設定マスタ
		for (Iterator<MwmTrayConfig> it = dto.configList.iterator(); it.hasNext(); ) {
			final MwmTrayConfig c = it.next();
			if (!targetTrayConfigIds.contains(c.getTrayConfigId()))
				it.remove();
		}
		// トレイ設定検索条件マスタ
		for (Iterator<MwmTrayConfigCondition> it = dto.conditionList.iterator(); it.hasNext(); ) {
			final MwmTrayConfigCondition c = it.next();
			if (!targetTrayConfigIds.contains(c.getTrayConfigId()))
				it.remove();
		}
		// トレイ設定検索結果マスタ
		for (Iterator<MwmTrayConfigResult> it = dto.resultList.iterator(); it.hasNext(); ) {
			final MwmTrayConfigResult c = it.next();
			if (!targetTrayConfigIds.contains(c.getTrayConfigId()))
				it.remove();
		}
		// トレイ設定個人マスタ
		for (Iterator<MwmTrayConfigPerson> it = dto.personList.iterator(); it.hasNext(); ) {
			final MwmTrayConfigPerson c = it.next();
			if (!targetTrayConfigIds.contains(c.getTrayConfigId()))
				it.remove();
		}
	}

	/**
	 * アップロードファイルと現在のデータベース内容の差分更新を行う
	 * @param req リクエスト
	 * @param dto ダウンロードDTO（＝アップロード内容）
	 * @param currentDto 現在のデータベースの内容
	 * @param changedPKsMap PK変更内容
	 * @param targetTrayConfigIds 取込対象のトレイ設定ID
	 */
	private void upsertAll(
			Up0040Request req,
			TrayConfigDownloadDto dto,
			TrayConfigDownloadDto currentDto,
			ChangedPKsMap changedPKsMap) {
		log.debug("START upsertAll()");

		//-------------------------------------
		// 業務管理項目マスタ
		//-------------------------------------
		{
			deleteAndInsert(dto.businessNameList, currentDto.businessNameList);
		}
		//-------------------------------------
		// トレイ設定　関連
		//-------------------------------------
		{
			// トレイ設定マスタ(MWM_TRAY_CONFIG)
			upsert(dto.configList, currentDto.configList, changedPKsMap, false);
			// トレイ設定検索条件マスタ(MWM_TRAY_CONFIG_CONDITION)
			upsert(dto.conditionList, currentDto.conditionList, changedPKsMap, false);
			// トレイ設定検索結果マスタ(MWM_TRAY_CONFIG_RESULT)
			upsert(dto.resultList, currentDto.resultList, changedPKsMap, false);
			// トレイ設定個人マスタ
			upsert(dto.personList, currentDto.personList, changedPKsMap, false);
		}
		//------------------------------------------
		// 多言語
		//------------------------------------------
		{
			// 多言語対応マスタ（MWM_MULTILINGUAL）
			// 多言語マスタは参照元レコードのIDが変わっている可能性があるので、差し替える
			for (MwmMultilingual m : dto.multilingualList) {
				ChangedPKs<?> changed = null;
				if (eq("MWM_BUSINESS_INFO_NAME", m.getTableName())) {
					changed = changedPKsMap.get(MwmBusinessInfoName.class);
				}
				else if (eq("MWM_TRAY_CONFIG", m.getTableName())) {
					changed = changedPKsMap.get(MwmTrayConfig.class);
				}
				if (changed != null && changed.containsKey(m.getId())) {
					m.setId(changed.get(m.getId()).newPK);
				}
			}
			deleteAndInsert(dto.multilingualList, currentDto.multilingualList);
		}
	}

	/** ユーザ指定内容でユニークキーを書き換え */
	private void replaceUniqueKeys(String oldCorporationCode, String newCorporationCode, TrayConfigDownloadDto dto) {
		// トレイ設定マスタ
		for (MwmTrayConfig c : dto.configList)
			c.setCorporationCode(newCorporationCode);

		// トレイ設定検索条件マスタ
		for (MwmTrayConfigCondition c : dto.conditionList)
			c.setCorporationCode(newCorporationCode);

		// トレイ設定検索結果マスタ
		for (MwmTrayConfigResult c : dto.resultList)
			c.setCorporationCode(newCorporationCode);

		// トレイ設定個人マスタ
		for (MwmTrayConfigPerson c : dto.personList)
			c.setCorporationCode(newCorporationCode);

		// 業務管理項目マスタ
		for (MwmBusinessInfoName c : dto.businessNameList)
			c.setCorporationCode(newCorporationCode);
	}

}
