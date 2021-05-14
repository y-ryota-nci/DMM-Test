package jp.co.nci.iwf.component.route.upload;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmAuthTransfer;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmWfRelationDefEx;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.download.BaseUploadService;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloadDto;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloadService;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloadUtils;
import jp.co.nci.iwf.endpoint.up.up0020.Up0020Config;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * プロセス定義のアップロードサービス
 */
@BizLogic
public class ProcessDefUploadService extends BaseUploadService {
	@Inject protected ProcessDefIdReplacer replacer;
	@Inject protected ProcessDefDownloadService downloader;
	@Inject protected ProcessDefUploadSyncIdService syncIdService;
	@Inject protected Logger log;

	/** 差分更新で不要となってもレコードを削除しないテーブルの定義 */
	protected Set<Class<?>> notDeletions;

	@PostConstruct
	public void init(){
		// 差分更新で不要となってもレコードを削除しないテーブルの定義
		notDeletions = new HashSet<>(Arrays.asList(
				WfmMenuRole.class,
				WfmMenuRoleDetail.class,
				WfmAssignRole.class,
				WfmAssignRoleDetail.class,
				WfmAuthTransfer.class,
				WfmChangeRole.class,
				WfmChangeRoleDetail.class
		));
	}

	/**
	 * プロセス定義アップロードによるデータ登録処理
	 * @param newCorporationCode
	 * @param req
	 * @param dto
	 */
	public void persist(String newCorporationCode, Up0020Config config, ProcessDefDownloadDto dto) {
		// ★実際の更新処理：採番マスタ(WFM_PROPERTY)の現在値を、各エンティティのID最大値と同期させる
		final Map<Class<?>, List<?>> allEntities = downloader.toAllEntities(dto);
		syncIdService.syncNumbering(allEntities);

		// ◇ユーザ指定内容でカラム値を書き換え
		String oldCorporationCode = dto.corporationCode;
		replaceByUploadConfig(oldCorporationCode, newCorporationCode, config, allEntities);

		// ◇差分更新するため、現在のデータベースの内容を抽出（メモリ上のみ）
		final ProcessDefDownloadDto existingDto = downloader.createDto(
				newCorporationCode, dto.processDefCode, dto.processDefDetailCode);
		final Map<Class<?>, List<?>> allExistingEntities = downloader.toAllEntities(existingDto);

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		upsertAll(newCorporationCode, config, allEntities, allExistingEntities);
	}

	/**
	 * アップロードファイルと現在のデータベース内容の差分更新を行う
	 * @param corporationCode アップロード先の企業コード
	 * @param config アップロードリクエスト
	 * @param allEntities アップロードしたエンティティ群
	 * @param allCurrentEntities 既存のエンティティ群
	 */
	@SuppressWarnings("unchecked")
	protected void upsertAll(String corporationCode, Up0020Config config, Map<Class<?>, List<?>> allEntities, Map<Class<?>, List<?>> allCurrentEntities) {
		Map<String, Map<Long, Long>> allChanges = new HashMap<>();

		List<WfmNameLookup> nameLookups = null;
		for (Class<?> clazz : allEntities.keySet()) {
			// WFM_NAME_LOOKUPは他テーブルのID依存なので、ここでは対象外とする
			List<?> entities = allEntities.get(clazz);
			if (WfmNameLookup.class == clazz) {
				nameLookups = (List<WfmNameLookup>)entities;
				continue;
			}
			// 代理者情報が取込対象か？
			if (!config.isAuthTransfer && WfmAuthTransfer.class == clazz) {
				log.info(clazz.getSimpleName() + "は取込対象外です。");
				continue;
			}
			// 参加者ロールは取込対象か？
			if (!config.isAssignRole && WfmAssignRole.class == clazz) {
				log.info(clazz.getSimpleName() + "は取込対象外です。");
				continue;
			}
			// 参加者ロール構成は取込対象か？
			if (!config.isAssignRoleDetail && WfmAssignRoleDetail.class == clazz) {
				log.info(clazz.getSimpleName() + "は取込対象外です。");
				continue;
			}
			// 参加者変更ロールは取込対象か？
			if (!config.isChangeRole && WfmChangeRole.class == clazz) {
				log.info(clazz.getSimpleName() + "は取込対象外です。");
				continue;
			}
			// 参加者変更ロール構成は取込対象か？
			if (!config.isChangeRoleDetail && WfmChangeRoleDetail.class == clazz) {
				log.info(clazz.getSimpleName() + "は取込対象外です。");
				continue;
			}

			// 各エンティティのプライマリキーを連結して文字列化
			Map<String, Object> currentEntities = allCurrentEntities.get(clazz).stream()
					.collect(Collectors.toMap(e -> replacer.toKey(e), e -> e));
			String tableName = ProcessDefDownloadUtils.getTableName(clazz);

			for (Object entity : entities) {
				// プライマリーキーを同じくする既存レコードがあれば、そのIDを取得。
				Long existingId = null;
				Timestamp existingTimestampUpdated = null;

				// PKで同定したレコードをは使用中なので、既存リストから除去
				String key = replacer.toKey(entity);
				Object existing = currentEntities.remove(key);
				if (existing != null) {
					existing = replacer.getExistingEntity(entity);
					existingId = getPropertyValue(existing, "id");
					existingTimestampUpdated = getPropertyValue(existing, "timestampUpdated");
					assert (existingId != null);
					assert (existingTimestampUpdated != null);
				}
				Long uploadId = getPropertyValue(entity, "id");
				setPropertyValue(entity, "id", existingId);
				setPropertyValue(entity, "timestampUpdated", existingTimestampUpdated);

				// 差分更新
				Long newId = null;
				if (existingId == null)
					newId = execInsertAPI(entity);
				else
					newId = execUpdateAPI(entity);

				// 新旧のIDで差異があれば、名称ルックアップ更新用に記録しておく
				if (!eq(uploadId, newId)) {
					Map<Long, Long> changes = allChanges.get(tableName);
					if (changes == null) {
						changes = new HashMap<>();
						allChanges.put(tableName, changes);
					}
					changes.put(uploadId, newId);
				}
			}

			// 使用していない既存レコードを論理削除
			// ただしメニューロールやメニューロール構成など削除すると不測の事態になる可能性のあるレコードは削除対象から外す
			if (!notDeletions.contains(clazz)) {
				for (Object entity : currentEntities.values()) {
					setPropertyValue(entity, "deleteFlag", DeleteFlag.ON);
					execUpdateAPI(entity);
				}
			}
		}
		// 名称ルックアップの対応テーブル側でIDが置換されていれば、名称ルックアップ側のIDも書き換え
		for (WfmNameLookup lookup : nameLookups) {
			String tableName = lookup.getId().getTableName();
			Long oldId = lookup.getId().getId();

			if (allChanges.containsKey(tableName)) {
				Map<Long, Long> changes = allChanges.get(tableName);
				if (changes.containsKey(oldId)) {
					Long newId = changes.get(oldId);
					lookup.getId().setId(newId);
				}
			}
			em.merge(lookup);
		}
		em.flush();
		em.clear();
	}

	/** ◇ユーザ指定内容でカラム値を書き換え */
	protected void replaceByUploadConfig(String oldCorporationCode, String newCorporationCode, Up0020Config config, Map<Class<?>, List<?>> allEntities) {

		for (Class<?> clazz : allEntities.keySet()) {
			if (clazz == WfmNameLookup.class)
				continue;

			List<?> entities = allEntities.get(clazz);
			if (entities.isEmpty())
				continue;

			for (Object entity : entities) {
				// 企業コード
				{
					PropertyDescriptor pd = getPropertyDescriptor(entity, "corporationCode");
					if (pd != null) {
						setPropertyValue(entity, "corporationCode", newCorporationCode);
					}

					// =====================================================================
					// 企業を超えて定義される可能性のあるテーブル（参加者ロール構成や情報共有者情報などは
					// 例えばグループ企業間でプロセス定義を共有しているなど、あえて自企業とは異なる企業コードを
					// 設定している場合がある。
					// このようなケースでは、アップロード元の企業コードと同一値の企業コードであれば置換する
					// =====================================================================
					// 参加者ロール構成.参加先企業コード
					if (entity instanceof WfmAssignRoleDetail) {
						final String old = getPropertyValue(entity, "corporationCodeAssigned");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeAssigned", newCorporationCode);
					}
					// 代理設定.プロセス定義企業コード
					if (entity instanceof WfmAuthTransfer) {
						final String old = getPropertyValue(entity, "corporationCodeP");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeP", newCorporationCode);
					}
					// アクション遷移先定義.遷移先企業コード
					if (entity instanceof WfmConditionDef) {
						final String old = getPropertyValue(entity, "corporationCodeTransit");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeTransit", newCorporationCode);
					}
					// 情報共有者情報.参加先企業コード
					if (entity instanceof WfmInformationSharerDef) {
						final String old = getPropertyValue(entity, "corporationCodeAssign");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeAssign", newCorporationCode);
					}
					// メニューロール構成.アクセス企業コード
					if (entity instanceof WfmMenuRoleDetail) {
						final String old = getPropertyValue(entity, "corporationCodeAccess");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeAccess", newCorporationCode);
					}
					// 参加者変更ロール構成.アクセス企業コード
					if (entity instanceof WfmChangeRoleDetail) {
						final String old = getPropertyValue(entity, "corporationCodeAssigned");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeAssigned", newCorporationCode);
					}
					// WF間連携定義.連携先プロセス定義明細コード
					if (entity instanceof WfmWfRelationDefEx) {
						final String old = getPropertyValue(entity, "corporationCodeRelation");
						if (eq(oldCorporationCode, old))
							setPropertyValue(entity, "corporationCodeRelation", newCorporationCode);
					}
				}
				// プロセス定義コード
				{
					PropertyDescriptor pd = getPropertyDescriptor(entity, "processDefCode");
					if (pd != null) {
						setPropertyValue(entity, pd, config.newProcessDefCode);
					}
					// アクション遷移先定義.遷移先プロセス定義コード
					if (clazz == WfmConditionDef.class)
						setPropertyValue(entity, "processDefCodeTransit", config.newProcessDefCode);
					// WF間連携定義.連携先プロセス定義コード
					if (clazz == WfmWfRelationDefEx.class)
						setPropertyValue(entity, "processDefCodeRelation", config.newProcessDefCode);
				}
				// プロセス定義明細コード
				{
					PropertyDescriptor pd = getPropertyDescriptor(entity, "processDefDetailCode");
					if (pd != null) {
						setPropertyValue(entity, pd, config.newProcessDefDetailCode);
					}
					// アクション遷移先定義.遷移先プロセス定義明細コード
					if (clazz == WfmConditionDef.class)
						setPropertyValue(entity, "processDefDetailCodeTran", config.newProcessDefDetailCode);
					// WF間連携定義.連携先プロセス定義明細コード
					if (clazz == WfmWfRelationDefEx.class)
						setPropertyValue(entity, "processDefDetailCodeR", config.newProcessDefDetailCode);
				}
			}
		}
	}
}
