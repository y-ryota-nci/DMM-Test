package jp.co.nci.iwf.endpoint.mm.mm0000;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロファイル管理画面のユーザ／組織の追加リクエスト
 */
public class Mm0000AddRequest extends BaseRequest {
	/** プロファイル管理ツリーのノードID(企業コード＋組織コード＋組織レベルを含んでいる) */
	public String nodeId;

	/** 企業を新規追加する際の企業コード */
	public String newCorporationCode;
	public String newCorporationName;

	/** 基準日 */
	public java.sql.Date baseDate;
	/** 有効データのみ表示 */
	public Boolean displayValidOnly;
}
