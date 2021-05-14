package jp.co.nci.iwf.endpoint.mm.mm0510;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * バージョン情報画面の初期化レスポンス
 */
public class Mm0510InitResponse extends BaseResponse {
	/** WARグループID */
	public String groupId;
	/** WARアーティファクトID */
	public String artifactId;
	/** WARバージョン */
	public String version;
	/** WARビルド者 */
	public String buildBy;
	/** WARビルド日付時刻 */
	public String buildTimestamp;
	/** WARビルド時のJDK */
	public String buildJdk;

	/** DB接続文字列 */
	public String databaseURL;
	/** DBユーザ */
	public String databaseUser;
	/** DB名前 */
	public String databaseName;

}
