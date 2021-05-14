package jp.co.nci.iwf.designer.parts.runtime;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.designer.DesignerCodeBook.UserTable;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignImage;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;

/**
 * 【実行時】画像パーツ。
 * 画像データのアップロード機能、およびアップロードしたファイルをイメージとして表示するパーツ
 */
public class PartsImage extends PartsBase<PartsDesignImage> {
	/** ワークフローパーツ添付ファイルID（実行時専用。画像を添付ファイルとして保存している） */
	public Long partsAttachFileWfId;
	/** パーツ添付ファイルID（デザイン時専用。画像を添付ファイルとして保存している） */
	public Long partsAttachFileId;
	/** 画像未設定時にダミー画像を表示するか */
	public boolean useDummyIfNoImage;
	/** 許可されたファイル名の正規表現 */
	public String fileRegExp;

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignImage d, DesignerContext ctx, boolean checkRequired,
			Map<String, EvaluateCondition> ecResults) {
		return null;
	}

	/** パーツにユーザデータを反映 */
	public void fromUserData(PartsDesignImage d, Map<String, Object> userData) {
		final Long runtimeId = toLong(userData.get(UserTable.RUNTIME_ID));
		final Long partsId = d.partsId;
		final UserDataService service = CDI.current().select(UserDataService.class).get();

		// ランタイムIDをキーにワークフローパーツ添付ファイル情報を抽出
		final List<MwtPartsAttachFileWf> files = service.getMwtPartsAttachFileWf(runtimeId, partsId);
		if (files != null && !files.isEmpty()) {
			partsAttachFileWfId = files.get(0).getPartsAttachFileWfId();
		} else {
			partsAttachFileWfId = null;
		}
	}

	/** ユーザデータ更新後の、パーツ固有の後処理 */
	@Override
	public void afterUpdateUserData(PartsDesignImage d, RuntimeContext ctx, PartsContainerBase<?> pc, PartsContainerRow row, Map<String, EvaluateCondition> ecCache) {
		// 既存データを抽出
		final UserDataService service = CDI.current().select(UserDataService.class).get();
		final Map<Long, MwtPartsAttachFileWf> currents = service.getMwtPartsAttachFileWf(row.runtimeId, partsId)
				.stream()
				.collect(Collectors.toMap(f -> f.getPartsAttachFileWfId(), f -> f));

		// パーツが有効であれば更新
		final EvaluateCondition ec = ecCache.get(this.htmlId);
		if (ec == null || ec.enabled) {
			// 画像データははアップロード完了時点でDB登録済みである。
			// ここではアップロード時点では不定だった可能性のあるランタイムID／プロセスID等を紐付けてやるだけ。
			// JPAなので、永続化コンテキストで管理されている 'current'を書き換えればDBへ自動反映される。
			if (partsAttachFileWfId != null) {
				MwtPartsAttachFileWf current = currents.remove(partsAttachFileWfId);
				if (current == null) {
					// 添付ファイルを登録直後だとランタイムIDが未採番なので抽出できない（新規起票だと添付ファイル登録時点ではランタイムIDが未採番なので）。
					// この場合はリクエストに設定されているプライマリキーで抽出してやる
					current = service.getMwtPartsAttachFileWf(partsAttachFileWfId);
				}

				if (current != null) {
					// この更新処理が動いた後ならランタイムID＋パーツIDで抽出できる
					current.setRuntimeId(row.runtimeId);
					current.setPartsId(this.partsId);
					current.setCorporationCode(ctx.corporationCode);
					current.setProcessId(ctx.processId);
					current.setDeleteFlag(DeleteFlag.OFF);
				}
			}
		}

		// 残余はいらなくなった添付ファイルなので削除
		final EntityManager em = CDI.current().select(EntityManager.class).get();
		for (MwtPartsAttachFileWf current : currents.values()) {
			em.remove(current);
		}
	}
}
