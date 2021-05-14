package jp.co.nci.iwf.endpoint.up.up0200;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.iwf.component.CorporationProperty;
import jp.co.nci.iwf.component.system.CorporationPropertyService;
import jp.co.nci.iwf.endpoint.up.up0200.sheet.Up0200Book;
import jp.co.nci.iwf.jpa.entity.wm.WfmCorpPropMaster;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * プロファイル情報アップロード画面の登録ロジック
 */
@ApplicationScoped
public class Up0200Register extends MiscUtils {
	@Inject private Up0200Repository repository;
	@Inject private CorporationPropertyService prop;

	/** ロック実行 */
	@Transactional
	public WfmCorpPropMaster lock() {
		// 他ユーザが同時にアップロードしないよう、排他ロック
		return repository.lock();
	}

	/** ロック解除 */
	@Transactional
	public void unlock(WfmCorpPropMaster entity) {
		repository.unlock(entity);
	}

	/**
	 * DBへの差分登録処理
	 * @param book
	 * @param deleteIfNotUse
	 */
	@Transactional
	public void save(Up0200Book book, boolean deleteIfNotUse) {
		// WORKテーブルの以前のレコードを削除
		truncateTables();

		// 組織の差分更新
		upsertOrg(book, deleteIfNotUse);
		// 役職の差分更新
		upsertPost(book, deleteIfNotUse);
		// ユーザの差分更新
		upsertUser(book, deleteIfNotUse);
		// ユーザ所属の差分更新
		upsertUserBelong(book, deleteIfNotUse);
	}

	/** ユーザ所属の差分更新 */
	private void upsertUserBelong(Up0200Book book, boolean deleteIfNotUse) {
		// アップロード内容をいったんWORKへ仮登録
		repository.bulkInsertUserBelong(book.sheetUserBelong.userBelongs);

		// WORKとWFM_POSTをマージしながら差分更新
		repository.upsertUserBelong();

		// 未使用なら削除
		if (deleteIfNotUse) {
			String tblName = "WFM_USER_BELONG";
			String[] pkColNames = { "CORPORATION_CODE", "USER_CODE", "SEQ_NO_USER_BELONG" };
			repository.deleteIfNotUse(tblName, pkColNames);
		}
	}

	/** ユーザの差分更新 */
	private void upsertUser(Up0200Book book, boolean deleteIfNotUse) {
		// アップロード内容をいったんWORKへ仮登録
		repository.bulkInsertUser(book.sheetUser.users);

		// WORKとWFM_POSTをマージしながら差分更新
		repository.upsertUser();

		// ユーザマスタ(ワーク)にいるがパスワードなしのユーザに、パスワードマスタを生成
		// 初回ログイン時のパスワード変更を強制するか
		boolean changeRequestFlag = prop.getBool(CorporationProperty.FIRST_LOGIN_CHANGE, true);
		repository.insertInitPassword(changeRequestFlag);

		// 多言語 WFM_NAME_LOOKUPの差分更新
		final String tblName = "WFM_USER";
		final String[] pkColNames = { "CORPORATION_CODE", "USER_CODE" };
		final String[] colNames = { "USER_NAME", "USER_NAME_ABBR", "ADDRESS" };
		for (String localeCode : book.localeCodes) {
			for (String columnName : colNames) {
				repository.upsertNameLookup(tblName, columnName, localeCode, pkColNames);
			}
		}

		// 未使用なら削除
		if (deleteIfNotUse) {
			repository.deleteIfNotUse(tblName, pkColNames);
		}
	}

	/** 役職マスタの差分更新 */
	private void upsertPost(Up0200Book book, boolean deleteIfNotUse) {
		// アップロード内容をいったんWORKへ仮登録
		repository.bulkInsertPost(book.sheetPost.posts);

		// WORKとWFM_POSTをマージしながら差分更新
		repository.upsertPost();

		// 多言語 WFM_NAME_LOOKUPの差分更新
		final String tblName = "WFM_POST";
		final String[] pkColNames = { "CORPORATION_CODE", "POST_CODE" };
		final String[] colNames = { "POST_NAME", "POST_NAME_ABBR" };
		for (String localeCode : book.localeCodes) {
			for (String columnName : colNames) {
				repository.upsertNameLookup(tblName, columnName, localeCode, pkColNames);
			}
		}

		// 未使用なら削除
		if (deleteIfNotUse) {
			repository.deleteIfNotUse(tblName, pkColNames);
		}
	}

	/** 組織マスタの差分更新 */
	private void upsertOrg(Up0200Book book, boolean deleteIfNotUse) {
		// アップロード内容をいったんWORKへ仮登録
		repository.bulkInsertOrg(book.sheetOrg.organizations);

		// WORKとWFM_ORGANIZATIONをマージしながら差分更新
		repository.upsertOrg();

		// 多言語 WFM_NAME_LOOKUPの差分更新
		final String tblName = "WFM_ORGANIZATION";
		final String[] pkColNames = { "CORPORATION_CODE", "ORGANIZATION_CODE" };
		final String[] colNames = { "ORGANIZATION_NAME", "ORGANIZATION_NAME_ABBR", "ADDRESS" };
		for (String localeCode : book.localeCodes) {
			for (String columnName : colNames) {
				repository.upsertNameLookup(tblName, columnName, localeCode, pkColNames);
			}
		}

		// 未使用なら削除
		if (deleteIfNotUse) {
			repository.deleteIfNotUse(tblName, pkColNames);
		}
	}

	/** WORKテーブルの以前のレコードを削除 */
	private void truncateTables() {
		repository.truncateTable("WFM_ORGANIZATION_WORK");
		repository.truncateTable("WFM_POST_WORK");
		repository.truncateTable("WFM_USER_WORK");
		repository.truncateTable("WFM_USER_BELONG_WORK");
	}
}
