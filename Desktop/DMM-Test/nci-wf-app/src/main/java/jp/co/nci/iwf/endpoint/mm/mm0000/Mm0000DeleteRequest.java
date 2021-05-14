package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロファイル管理画面のユーザ／組織の削除リクエスト
 */
public class Mm0000DeleteRequest extends BaseRequest {

	/** プロファイル管理ツリーのノードID(企業コード＋組織コード＋組織レベルを含んでいる) */
	public String nodeId;
	/** 基準日 */
	public Date baseDate;
}
