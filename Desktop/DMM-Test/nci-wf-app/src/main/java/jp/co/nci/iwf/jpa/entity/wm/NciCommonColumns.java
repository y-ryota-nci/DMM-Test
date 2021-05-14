package jp.co.nci.iwf.jpa.entity.wm;

import java.sql.Timestamp;

/**
 * 共通Columnを更新する為のマーカーインターフェース
 *
 * @version 1.0
 */
public interface NciCommonColumns {
	/**
	 * IDを取得する
	 * @return ID
	 */
	Long getId();

	/**
	 * IDを設定する
	 * @param id ID
	 */
	void setId(Long id);

	/**
	 * versionを取得する
	 * @return version
	 */
	Long getVersion();

	/**
	 * versionを設定する
	 * @param version バージョン
	 */
	void setVersion(Long version);

	/**
	 * 作成ユーザコードを取得する.
	 * @return 作成ユーザコード	 */
	String getUserCodeCreated();

	/**
	 * 作成ユーザコードを設定する.
	 * @param val 作成ユーザコード	 */
	void setUserCodeCreated(String val);

	/**
	 * 作成プログラムIDを取得する.
	 * @return 作成プログラムID
	 */
	String getProgramIdCreated();

	/**
	 * 作成プログラムIDを設定する.
	 * @param val 作成プログラムID
	 */
	void setProgramIdCreated(String val);

	/**
	 * 作成日時を取得する.
	 * @return TIMESTAMP_CREATED
	 */
	Timestamp getTimestampCreated();

	/**
	 * 作成日時を設定する.
	 * @param val 設定する値
	 */
	void setTimestampCreated(Timestamp val);

	/**
	 * 更新ユーザコードを取得する.
	 * @return 更新ユーザコード
	 */
	String getUserCodeUpdated();

	/**
	 * 更新ユーザコードを設定する.
	 * @param val 更新ユーザコード
	 */
	void setUserCodeUpdated(String val);

	/**
	 * 更新プログラムIDを取得する.
	 * @return 更新プログラムID
	 */
	String getProgramIdUpdated();

	/**
	 * 更新プログラムIDを設定する.
	 * @param val 更新プログラムID
	 */
	void setProgramIdUpdated(String val);

	/**
	 * 更新日時を取得する.
	 * @return TIMESTAMP_UPDATED
	 */
	Timestamp getTimestampUpdated();

	/**
	 * '更新日時(TIMESTAMP_UPDATED)'を設定する.
	 * @param val 設定する値
	 */
	void setTimestampUpdated(Timestamp val);
}
