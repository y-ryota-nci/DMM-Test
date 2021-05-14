package jp.co.nci.iwf.endpoint.up.up0100;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * アップロード履歴画面リポジトリ
 */
@ApplicationScoped
public class Up0100Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");

	@Inject private SessionHolder sessionHolder;

	/** 件数のみ抽出 */
	public int count(Up0100Request req) {
		StringBuilder sql = new StringBuilder(getSql("UP0100_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/** カラム指定ありで抽出 */
	public List<Up0100Entity> select(Up0100Request req, int allCount) {
		if (allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(getSql("UP0100_01").replaceFirst(REPLACE, getSql("UP0100_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Up0100Entity.class, sql.toString(), params.toArray());
	}

	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Up0100Request req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(sessionHolder.getLoginInfo().getLocaleCode());
		params.add(sessionHolder.getLoginInfo().getCorporationCode());

		// アップロード元の企業コード
		if (isNotEmpty(req.fileCorporationCode)) {
			sql.append(" and FILE_CORPORATION_CODE = ?");
			params.add(req.fileCorporationCode);
		}
		// アップロードファイル名
		if (isNotEmpty(req.fileName)) {
			sql.append(" and FILE_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.fileName));
		}
		// ファイルAPPバージョン
		if (isNotEmpty(req.fileAppVersion)) {
			sql.append(" and FILE_APP_VERSION like ? escape '~'");
			params.add(escapeLikeBoth(req.fileAppVersion));
		}
		// ファイル登録フラグ
		if (isNotEmpty(req.registeredFlag)) {
			sql.append(" and REGISTERED_FLAG = ?");
			params.add(req.registeredFlag);
		}
		// アップロード日時From/To
		if (req.uploadDatetimeFrom != null) {
			sql.append(" and trunc(UPLOAD_DATETIME) >= ?");
			params.add(req.uploadDatetimeFrom);
		}
		if (req.uploadDatetimeTo != null) {
			sql.append(" and trunc(UPLOAD_DATETIME) <= ?");
			params.add(req.uploadDatetimeTo);
		}
		// ファイル作成日時From/To
		if (req.fileTimestampFrom != null) {
			sql.append(" and trunc(FILE_TIMESTAMP) >= ?");
			params.add(req.fileTimestampFrom);
		}
		if (req.fileTimestampTo != null) {
			sql.append(" and trunc(FILE_TIMESTAMP) <= ?");
			params.add(req.fileTimestampTo);
		}
		// 種別
		if (isNotEmpty(req.uploadKind)) {
			sql.append(" and UPLOAD_KIND = ?");
			params.add(req.uploadKind);
		}

		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	/** アップロードファイル登録情報を抽出 */
	public List<Up0100History> getHistory(Long uploadFileId) {
		String sql = getSql("UP0100_03");
		Object[] params = { sessionHolder.getLoginInfo().getLocaleCode(), uploadFileId };
		return select(Up0100History.class, sql, params);
	}
}
