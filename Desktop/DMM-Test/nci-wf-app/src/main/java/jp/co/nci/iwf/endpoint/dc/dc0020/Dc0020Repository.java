package jp.co.nci.iwf.endpoint.dc.dc0020;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.document.DocHelper;
import jp.co.nci.iwf.endpoint.dc.DcCodeBook.SearchType;
import jp.co.nci.iwf.jersey.base.BaseRepository;

@ApplicationScoped
public class Dc0020Repository extends BaseRepository {
	private static final String REPLACE = "###REPLACE###";
	private static final String REPLACE2 = "###REPLACE2###";

	@Inject
	private DocHelper helper;

	/**
	 * 件数抽出
	 * @param req
	 * @return
	 */
	public int count(Dc0020SearchRequest req) {
		// 条件
		final List<Object> params = new ArrayList<>();
		final LoginInfo login = LoginInfo.get();

		// フォルダ条件
		final StringBuilder sqlFolder = new StringBuilder();
		// 通常検索の場合、文書フォルダも検索対象
		if (eq(SearchType.NORMAL, req.searchType)) {
			sqlFolder.append(getSql("DC0020_03"));
			fillConditionFolder(req, sqlFolder, params, login);
			sqlFolder.append(" union all ");
		}

		// 文書条件
		final StringBuilder sqlDoc = new StringBuilder(getSql("DC0020_04"));
		fillConditionDoc(req, sqlDoc, params, login);

		final StringBuilder sql = new StringBuilder(getSql("DC0020_01").replaceFirst("###REPLACE_DC0020_03###", sqlFolder.toString()).replaceFirst("###REPLACE_DC0020_04###", sqlDoc.toString()));
		fillCondition(req, login, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 検索エンティティ抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<Dc0020Entity> select(Dc0020SearchRequest req, Dc0020SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		final List<Object> params = new ArrayList<>();
		final LoginInfo login = LoginInfo.get();

		// フォルダ条件
		final StringBuilder sqlFolder = new StringBuilder();
		// 通常検索の場合、文書フォルダも検索対象
		if (eq(SearchType.NORMAL, req.searchType)) {
			sqlFolder.append(getSql("DC0020_03"));
			fillConditionFolder(req, sqlFolder, params, login);
			sqlFolder.append(" union all ");
		}

		// 文書条件
		final StringBuilder sqlDoc = new StringBuilder(getSql("DC0020_04"));
		fillConditionDoc(req, sqlDoc, params, login);

		final StringBuilder sql = new StringBuilder(getSql("DC0020_02").replaceFirst("###REPLACE_DC0020_03###", sqlFolder.toString()).replaceFirst("###REPLACE_DC0020_04###", sqlDoc.toString()));
		fillCondition(req, login, sql, params, true);

		return select(Dc0020Entity.class, sql.toString(), params.toArray());
	}

	/** 文書情報のEntity取得. */
	public Dc0020Entity get(long docId) {
		final List<Object> params = new ArrayList<>();
		final LoginInfo login = LoginInfo.get();
		final Dc0020SearchRequest req = new Dc0020SearchRequest();
		req.docId = docId;
		final StringBuilder sql = new StringBuilder(getSql("DC0020_04"));
		fillConditionDoc(req, sql, params, login);
		final List<Dc0020Entity> list = select(Dc0020Entity.class, sql.toString(), params.toArray());
		return list.stream().findFirst().orElse(null);
	}

	/** SELECT/COUNTでの共通SQLを追記(フォルダ) */
	private void fillConditionFolder(Dc0020SearchRequest req, StringBuilder sql, List<Object> params, LoginInfo login) {
		params.add(login.isCorpAdmin() ? CommonFlag.ON : CommonFlag.OFF);
		params.add(login.getLocaleCode());

		// 一般ユーザの場合、フォルダに対する権限の有無をチェック
		if (!login.isCorpAdmin()) {
			// ログイン者のログイン情報より文書権限のハッシュ値を取得
			final Set<String> hashValues = helper.toHashValues(login);
			StringBuilder replace = new StringBuilder();
			for (String hashValue: hashValues) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}
			sql.append( getSql("DC0001_01").replaceFirst("###REPLACE###", replace.toString()) );
		}
	}

	/** SELECT/COUNTでの共通SQLを追記(文書) */
	private void fillConditionDoc(Dc0020SearchRequest req, StringBuilder sql, List<Object> params, LoginInfo login) {
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());
		params.add(login.isCorpAdmin() ? CommonFlag.ON : CommonFlag.OFF);
		params.add(login.getCorporationCode());
		params.add(login.getUserCode());

		// ログイン者のログイン情報より文書権限のハッシュ値を取得
		{
			final Set<String> hashValues = helper.toHashValues(login);
			StringBuilder replace = new StringBuilder();
			for (String hashValue: hashValues) {
				replace.append(replace.length() == 0 ? "?" : ", ?");
				params.add(hashValue);
			}

			int start = sql.indexOf(REPLACE);
			int end = start + REPLACE.length();
			sql.replace(start, end, replace.toString());
		}

		// ここで簡易検索時の全文検索用検索文字列を生成
		final String searchWord = eq(SearchType.SIMPLE, req.searchType) ? createOracleTextWord(req.keyword) : null;

		// 文書ファイルに対する絞込み条件
		{
			StringBuilder replace = new StringBuilder(" B.DELETE_FLAG = '0' ");
			// 簡易検索かつ"文書ファイルを含む"にチェックがついていた場合、全文検索用キーワードを含む文書ファイルのみを一覧の表示対象とする
			// そのためここで絞込み条件を追加する
			if (eq(SearchType.SIMPLE, req.searchType) && isNotEmpty(searchWord) && eq(CommonFlag.ON, req.includeDocFile)) {
				replace.append(" and (CONTAINS(C.FILE_DATA, ?) > 0  OR CONTAINS(D.FILE_DATA, ?) > 0)");
				params.add(searchWord);
				params.add(searchWord);
			}
			int start = sql.indexOf(REPLACE2);
			int end = start + REPLACE2.length();
			sql.replace(start, end, replace.toString());
		}

		if (req.docId != null) {
			sql.append(" and MDI.DOC_ID = ?");
			params.add(req.docId);
		}

		// 簡易(キーワード)検索の場合
		if (eq(SearchType.SIMPLE, req.searchType) && isNotEmpty(searchWord)) {
			sql.append(" and (");
			// 文書コンテンツに対する全文検索用文字列を条件に追加
			{
				sql.append(getSql("DC0020_05"));
				params.add(searchWord);
			}
			// 文書ファイルに対する全文検索用文字列を条件に追加
			// ただし上記の147～150行目で文書ファイルに対する絞込みは行っているので
			// ここではデータがあるかないかだけで判断すればよいはず
			if (eq(CommonFlag.ON, req.includeDocFile)) {
				sql.append(" or DFI.DOC_ID IS NOT NULL");
			}
			sql.append(")");
		}
	}


	/** SELECT/COUNTでの共通SQLを追記 */
	private void fillCondition(Dc0020SearchRequest req, LoginInfo login, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(login.getCorporationCode());
		// 通常検索の場合は必ずフォルダIDで絞込み
		if (eq(SearchType.NORMAL, req.searchType)) {
			sql.append(" and D.DOC_FOLDER_ID = ?");
			params.add(req.docFolderId);
		}
		// 簡易(キーワード)検索の場合はトップフォルダであれば絞込みは除外する
		else if (eq(SearchType.SIMPLE, req.searchType) && req.docFolderId != 0) {
			sql.append(" and D.DOC_FOLDER_ID = ?");
			params.add(req.docFolderId);
		}

		if (paging && isNotEmpty(req.sortColumn)) {
			// ソート
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}

	/**
	 * 全文検索に使用する検索文字列生成.
	 * @param keyword
	 * @return
	 */
	private String createOracleTextWord(String keyword) {
		StringBuilder keywords = new StringBuilder();
		if (isNotEmpty(keyword)) {
			String[] words = keyword.split("[　 ]", 0);
			if (words.length > 0) {
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					if (StringUtils.isNotBlank(word)) {
						keywords.append(keywords.length() == 0 ? "" : " | ");
						keywords.append("{" + this.escapeSpecialCharacter(word) + "}");
					}
				}
			}
		}
		return keywords.toString();
	}

	/**
	 * 全文検索の禁則文字を変換する.
	 * @param str 対象文字列
	 * @return 結果
	 */
	private String escapeSpecialCharacter(final String str) {
		// Oracle全文検索に空文字を渡すと落ちるので半角スペースに変換
		if (str == null || str.length() == 0) {
			return " ";
		}
		String s = new String(str);
		// エスケープ文字列は \ { } の３つ
		s = s.replaceAll("\\\\", "\\\\\\\\");
		s = s.replaceAll("\\{", "{{");
		s = s.replaceAll("\\}", "}}");
		return s;
	}
}
