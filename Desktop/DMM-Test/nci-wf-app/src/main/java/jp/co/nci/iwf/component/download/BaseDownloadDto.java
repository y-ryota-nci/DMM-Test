package jp.co.nci.iwf.component.download;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * ダウンロード(アップロード)用DTOの基底クラス
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)	// クラスのFQCNをJSONフィールドに出力。クライアントから書き戻す際に必要
public abstract class BaseDownloadDto implements Serializable {
	/** ダウンロード時の企業コード */
	public String corporationCode;
	/** ダウンロード時の企業名 */
	public String corporationName;
	/** ダウンロード時のAPPバージョン */
	public String appVersion;
	/** ダウンロード時のサーバIPアドレス */
	public String hostIpAddr;
	/** ダウンロード時のサーバ名 */
	public String hostName;
	/** ダウンロード時のDB接続先 */
	public String dbDestination;
	/** ダウンロード時のユーザ */
	public String dbUser;
	/** ダウンロード時の時刻 */
	public Timestamp timestampCreated;

	/**
	 * コンストラクタ
	 */
	public BaseDownloadDto() {
	}

	/**
	 * コンストラクタ
	 * @param corporationCode
	 */
	public BaseDownloadDto(String corporationCode, String corporationName) {
		this.corporationCode = corporationCode;
		this.corporationName = corporationName;
	}
}
