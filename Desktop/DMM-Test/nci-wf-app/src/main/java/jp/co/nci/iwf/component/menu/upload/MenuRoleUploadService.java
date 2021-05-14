package jp.co.nci.iwf.component.menu.upload;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.integrated_workflow.param.input.SearchWfmMenuRoleInParam;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.download.BaseUploadService;
import jp.co.nci.iwf.component.menu.download.MenuRoleDownloadDto;
import jp.co.nci.iwf.component.menu.download.MenuRoleDownloadService;
import jp.co.nci.iwf.component.route.download.ProcessDefDownloadUtils;
import jp.co.nci.iwf.endpoint.up.up0030.Up0030Request;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * メニューロールのアップロードサービス
 */
@BizLogic
public class MenuRoleUploadService extends BaseUploadService {
	@Inject private MenuRoleIdReplacer replacer;
	@Inject private MenuRoleDownloadService downloader;
	@Inject private MenuRoleUploadSyncIdService syncIdService;
	@Inject private Logger log;

	/**
	 * メニューロールアップロードによるデータ登録処理
	 * @param newCorporationCode
	 * @param req
	 * @param dto
	 */
	@Transactional
	public void persist(String newCorporationCode, Up0030Request req, MenuRoleDownloadDto dto) {
		// ★実際の更新処理：採番マスタ(WFM_PROPERTY)の現在値を、各エンティティのID最大値と同期させる
		final Map<Class<?>, List<?>> allEntities = downloader.toAllEntities(dto);
		syncIdService.syncNumbering(allEntities);

		// ◇ユーザ指定内容でユニークキーを書き換え
		String oldCorporationCode = dto.corporationCode;
		replaceUniqueKeys(oldCorporationCode, newCorporationCode, dto);

		// ◆メニューロールアップロードでシステムエラーとなる不具合の対応
		// 原因はBaseIdReplacer内のgetExistingEntity内でNullPointerExceptionが発生するため
		// (より詳細に書くとBaseIdReplacerが持つWfInstanceWrapper#wfInstanceがNULLだから)
		// これを回避するためWfInstaceWrapper内のメソッドをキックしてやるとWfInstanceHandler経由でwfInstanceおよびConnectionがセットされる
		// 以下は上記を実行するためのダミー関数（呼び出すだけでいいので戻り値などない）
		dummy(newCorporationCode, dto.menuRoleType);

		// ★実際の更新処理：アップロードファイルと現在のデータベース内容の差分更新を行う
		upsertAll(newCorporationCode, req, allEntities);
	}

	/**
	 * アップロードファイルと現在のデータベース内容の差分更新を行う
	 * @param corporationCode アップロード先の企業コード
	 * @param req アップロードリクエスト
	 * @param allEntities アップロードしたエンティティ群
	 */
	@SuppressWarnings("unchecked")
	private void upsertAll(String corporationCode, Up0030Request req, Map<Class<?>, List<?>> allEntities) {
		Map<String, Map<Long, Long>> allChanges = new HashMap<>();

		List<WfmNameLookup> nameLookups = null;
		for (Class<?> clazz : allEntities.keySet()) {
			// WFM_NAME_LOOKUPは他テーブルのID依存なので、ここでは対象外とする
			List<?> entities = allEntities.get(clazz);
			if (WfmNameLookup.class == clazz) {
				nameLookups = (List<WfmNameLookup>)entities;
				continue;
			}
			if (entities == null || entities.isEmpty())
				continue;

			// メニューロール構成は取込対象か？
			if (!req.isMenuRoleDetail && WfmMenuRoleDetail.class == clazz) {
				log.info(clazz.getSimpleName() + "は取込対象外です。");
				continue;
			}

			String tableName = ProcessDefDownloadUtils.getTableName(clazz);
			for (Object entity : entities) {
				// メニューロールコードは取込対象か？
				String menuRoleCode = getPropertyValue(entity, "menuRoleCode");
				if (!req.menuRoleCodes.contains(menuRoleCode))
					continue;

				// プライマリーキーを同じくする既存レコードがあれば、そのIDを取得。
				Long existingId = null;
				Timestamp existingTimestampUpdated = null;
				Object existing = replacer.getExistingEntity(entity);
				if (existing != null) {
					existingId = getPropertyValue(existing, "id");
					existingTimestampUpdated = getPropertyValue(existing, "timestampUpdated");
				}
				setPropertyValue(entity, "id", existingId);
				setPropertyValue(entity, "timestampUpdated", existingTimestampUpdated);

				// 差分更新
				Long newId = null;
				if (existingId == null)
					newId = execInsertAPI(entity);
				else
					newId = execUpdateAPI(entity);

				// 新旧のIDで差異があれば、名称ルックアップ更新用に記録しておく
				if (!eq(newId, existingId)) {
					Map<Long, Long> changes = allChanges.get(tableName);
					if (changes == null) {
						changes = new HashMap<>();
						allChanges.put(tableName, changes);
					}
					changes.put(existingId, newId);
				}

				// メニューロールおよびメニューロール構成は差分更新の結果で不要レコードが
				// 出てきたとしても削除処理は行わない
				// これは万が一、削除した結果がシステムを維持していくうえで必要なメニューロールかもしれないため。
				// 例えばシステム管理者用のメニューロールが削除されたら非常に困る..
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
	}

	/** ユーザ指定内容でユニークキーを書き換え */
	protected void replaceUniqueKeys(String oldCorporationCode, String newCorporationCode, MenuRoleDownloadDto dto) {
		// メニューロール
		for (WfmMenuRole entity : dto.menuRoleList) {
			// 企業コード
			entity.setCorporationCode(newCorporationCode);
		}
		// メニューロール構成
		for (WfmMenuRoleDetail entity : dto.menuRoleDetailList) {
			// 企業コード
			entity.setCorporationCode(newCorporationCode);
			// .アクセス企業コード
			if (eq(oldCorporationCode, entity.getCorporationCodeAccess())) {
				entity.setCorporationCodeAccess(newCorporationCode);
			}
		}
	}

	/**
	 * BaseIdReplacerが持つWfInstanceWrapperにコネクション等をセットしたいためだけのダミー関数.
	 * @param corporationCode 会社コード
	 * @param menuRoleType メニューロールタイプ
	 */
	private void dummy(String corporationCode, String menuRoleType) {
		final SearchWfmMenuRoleInParam in = new SearchWfmMenuRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setMenuRoleType(menuRoleType);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "MR." + WfmMenuRole.MENU_ROLE_TYPE),
				new OrderBy(true, "MR." + WfmMenuRole.CORPORATION_CODE),
				new OrderBy(true, "MR." + WfmMenuRole.MENU_ROLE_CODE),
		});
		wf.searchWfmMenuRole(in);
	}
}
