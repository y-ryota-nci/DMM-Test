package jp.co.nci.iwf.designer.parts.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.DesignerCodeBook.UserTable;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAttachFile;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.designer.service.userData.UserDataService;
import jp.co.nci.iwf.jpa.entity.mw.MwtPartsAttachFileWf;

/**
 * 【実行時】添付ファイルパーツ。
 * 申請／承認時に動的にファイルアップロード可能な添付ファイルコントロール。
 */
public class PartsAttachFile extends PartsBase<PartsDesignAttachFile> implements IPartsPaging {
	/** 行の要素 */
	public List<PartsAttachFileRow> rows = new ArrayList<>();
	/** ページ番号 */
	public Integer pageNo;
	/** ページあたりの行数 */
	public Integer pageSize;
	/** 複数ファイルのみを扱うか(trueなら複数添付ファイル、falseだと単一添付ファイルのみを扱う) */
	public boolean multiple;
	/** 最大添付ファイル数 */
	public int maxFileCount;
	/** (複数ファイルを扱うときに)コメント欄を使用するか */
	public boolean notUseComment;
	/** 許可されたファイル名の正規表現 */
	public String fileRegExp;

	/**
	 * ページ数を返す
	 * @return ページ制御するなら総ページ数、ページ制御なしなら -1
	 */
	@JsonIgnore
	public int calcPageCount() {
		return PartsUtils.getPageNo(rows.size(), pageSize);
	}

	/**
	 * 新しいページ番号を総ページ数で補正してセット
	 */
	@JsonIgnore
	public void adjustNewPageNo(int newPageNo) {
		int pageCount = calcPageCount();
		pageNo = PartsUtils.adjustPageNo(pageCount, newPageNo);
	}

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignAttachFile d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		long count = rows.stream().filter(r -> isNotEmpty(r.fileName)).count();
		if (multiple) {
			// 複数ファイルあり
			if (checkRequired && d.requiredFlag && d.requiredFileCount != null && d.requiredFileCount > 0) {
				// 必須ファイル数
				if (count < d.requiredFileCount) {
					return new PartsValidationResult(htmlId, null, d.labelText, MessageCd.MSG0135, d.requiredFileCount);
				}
			}
			if (maxFileCount > 0 && count > maxFileCount) {
				// 最大ファイル数
				return new PartsValidationResult(htmlId, null, d.labelText, MessageCd.MSG0155, d.maxFileCount);
			}
		}
		else {
			// 単一ファイル
			if (checkRequired && d.requiredFlag && count == 0L) {
				// 必須
				return new PartsValidationResult(htmlId, null, d.labelText, MessageCd.MSG0003, MessageCd.attachFile);
			}
		}
		return null;
	}

	/** パーツにユーザデータを反映 */
	public void fromUserData(PartsDesignAttachFile d, Map<String, Object> userData) {
		final Long runtimeId = toLong(userData.get(UserTable.RUNTIME_ID));
		final Long partsId = d.partsId;
		final UserDataService service = CDI.current().select(UserDataService.class).get();

		// ランタイムIDをキーにワークフローパーツ添付ファイル情報を抽出
		final List<MwtPartsAttachFileWf> files = service.getMwtPartsAttachFileWf(runtimeId, partsId);
		rows.clear();
		for (int i = 0; i < files.size(); i++) {
			MwtPartsAttachFileWf file = files.get(i);
			if (eq(DeleteFlag.OFF, file.getDeleteFlag())) {
				rows.add(new PartsAttachFileRow(file));
			}
		}
	}

	/** ユーザデータ更新後の、パーツ固有の後処理 */
	@Override
	public void afterUpdateUserData(PartsDesignAttachFile d, RuntimeContext ctx, PartsContainerBase<?> pc, PartsContainerRow row, Map<String, EvaluateCondition> ecCache) {
		// 既存データを抽出
		final UserDataService service = CDI.current().select(UserDataService.class).get();
		final Map<Long, MwtPartsAttachFileWf> currents = service.getMwtPartsAttachFileWf(row.runtimeId, partsId)
				.stream()
				.collect(Collectors.toMap(f -> f.getPartsAttachFileWfId(), f -> f));

		// パーツが有効であれば更新
		final EvaluateCondition ec = ecCache.get(this.htmlId);
		if (ec == null || ec.enabled) {
			for (PartsAttachFileRow input : rows) {
				// 添付ファイルはアップロード完了時点でDB登録済みである。
				// ここではアップロード時点では不定だった可能性のあるランタイムID／プロセスID等を紐付けてやるだけ。
				// JPAなので、永続化コンテキストで管理されている 'current'を書き換えればDBへ自動反映される。
				MwtPartsAttachFileWf current = currents.remove(input.partsAttachFileWfId);
				if (current == null) {
					// 添付ファイルを登録直後だとランタイムIDが未採番なので抽出できない（新規起票だと添付ファイル登録時点ではランタイムIDが未採番なので）。
					// この場合はリクエストに設定されているプライマリキーで抽出してやる
					current = service.getMwtPartsAttachFileWf(input.partsAttachFileWfId);
				}

				if (current != null) {
					// この更新処理が動いた後ならランタイムID＋パーツIDで抽出できる
					current.setRuntimeId(row.runtimeId);
					current.setPartsId(this.partsId);
					current.setCorporationCode(ctx.corporationCode);
					current.setProcessId(ctx.processId);
					current.setDeleteFlag(DeleteFlag.OFF);
					current.setSortOrder(input.sortOrder);
					current.setComments(input.comments);
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
