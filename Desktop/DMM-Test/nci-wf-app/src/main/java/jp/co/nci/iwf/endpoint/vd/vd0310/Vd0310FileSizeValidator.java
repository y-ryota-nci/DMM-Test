package jp.co.nci.iwf.endpoint.vd.vd0310;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.i18n.I18nService;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFile;
import jp.co.nci.iwf.designer.parts.runtime.PartsAttachFileRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsImage;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 申請文書のファイルサイズに関するバリデーター
 */
@ApplicationScoped
public class Vd0310FileSizeValidator extends BaseRepository {
	@Inject private CorporationPropertyService prop;
	@Inject private I18nService i18n;

	/**
	 * その申請文書に添付されている画像や添付ファイルのファイルサイズを合算して、
	 * システムプロパティに定義されたファイルサイズの上限を超過していないかをチェックする
	 * @param req 申請リクエスト
	 * @param ctx デザイナーコンテキスト
	 * @return 判定結果
	 */
	public Vd0310FileSizeValidatorResult validate(Vd0310ExecuteRequest req, RuntimeContext ctx) {
		// プロセスインスタンスあたりの添付ファイルサイズの条件(0以下はチェック不要)
		final Vd0310FileSizeValidatorResult result = new Vd0310FileSizeValidatorResult(i18n.getText(MessageCd.MSG0274));
		result.setMaxFileSize(getMaxFileSize());

		// 申請文書内の添付ファイルパーツ／画像パーツのファイルサイズ
		result.setPartsFileSize(getPartsFileSize(ctx));

		// 添付ファイルブロックのファイルサイズ
		result.setWfFileSize(getWfFileSize(req));

		// 文書管理の文書ファイルサイズ
		result.setDocFileSize(getDocFileSize(req));

		return result;
	}

	/** 文書管理の文書ファイルサイズ */
	private int getDocFileSize(Vd0310ExecuteRequest req) {
		// 処理前に添付されていたワークフロー添付ファイルID
		final Set<Long> ids = req.contents.docFileWfList.stream()
				.map(f -> f.docFileWfId)
				.collect(Collectors.toSet());
		// 今回追加されたワークフロー添付ファイルID
		ids.addAll(req.additionAttachFileWfList.stream()
				.map(f -> f.attachFileWfId)
				.collect(Collectors.toSet()));
		// 今回削除されたワークフロー添付ファイルID
		ids.removeAll(req.removeAttachFileWfIdList);

		// ワークフロー添付ファイルIDをキーにファイルサイズを集計
		if (ids.isEmpty()) {
			return 0;
		}
		final StringBuilder sql = new StringBuilder()
				.append(getSql("VD0310_23"))
				.append(toInListSql("and DOC_FILE_WF_ID", ids.size()));
		return count(sql.toString(), ids.toArray());
	}

	/** 添付ファイルブロックのファイルサイズ */
	private int getWfFileSize(Vd0310ExecuteRequest req) {
		// 処理前に添付されていたワークフロー添付ファイルID
		final Set<Long> ids = req.contents.attachFileWfList.stream()
				.map(f -> f.attachFileWfId)
				.collect(Collectors.toSet());
		// 今回追加されたワークフロー添付ファイルID
		ids.addAll(req.additionAttachFileWfList.stream()
				.map(f -> f.attachFileWfId)
				.collect(Collectors.toSet()));
		// 今回削除されたワークフロー添付ファイルID
		ids.removeAll(req.removeAttachFileWfIdList);

		// ワークフロー添付ファイルIDをキーにファイルサイズを集計
		if (ids.isEmpty()) {
			return 0;
		}
		final StringBuilder sql = new StringBuilder()
				.append(getSql("VD0310_22"))
				.append(toInListSql("and ATTACH_FILE_WF_ID", ids.size()));
		return count(sql.toString(), ids.toArray());
	}

	/** 申請文書内の添付ファイルパーツ／画像パーツのファイルサイズ */
	private int getPartsFileSize(RuntimeContext ctx) {
		if (ctx == null || ctx.runtimeMap == null || ctx.runtimeMap.isEmpty()) {
			return 0;
		}

		// この申請文書で扱っている画像パーツ／添付ファイルパーツのワークフローパーツ添付ファイルIDを求める
		final Set<Long> ids = new HashSet<>();
		ctx.runtimeMap.values().forEach(p -> {
			if (p instanceof PartsImage) {
				// 画像パーツ
				final PartsImage img = (PartsImage)p;
				if (img.partsAttachFileWfId != null)
					ids.add(img.partsAttachFileWfId);
			}
			else if (p instanceof PartsAttachFile) {
				// 添付ファイルパーツ
				final PartsAttachFile attach = (PartsAttachFile)p;
				for (PartsAttachFileRow row : attach.rows) {
					if (row.partsAttachFileWfId != null)
						ids.add(row.partsAttachFileWfId);
				}
			}
		});

		// ワークフローパーツ添付ファイルIDをキーにファイルサイズを集計
		if (ids.isEmpty()) {
			return 0;
		}
		final StringBuilder sql = new StringBuilder()
				.append(getSql("VD0310_21"))
				.append(toInListSql("and PARTS_ATTACH_FILE_WF_ID", ids.size()));
		return count(sql.toString(), ids.toArray());
	}

	/** プロセスインスタンスあたりの添付ファイルサイズ上限を抽出 */
	public int getMaxFileSize() {
		// システムプロパティの設定値に従う（単位がMBなので、バイトに換算）
		int max = prop.getInt(CorporationProperty.MAX_FILE_SIZE_PER_PROCESS, 0) * 1024 * 1024;
		return max;
	}
}
