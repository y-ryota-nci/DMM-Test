package jp.co.nci.iwf.endpoint.mm.mm0000;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロファイル管理画面の初期化リクエスト
 */
public class Mm0000InitRequest extends BaseRequest {
	/** 企業コード */
	public String corporationCode;
	/** 組織コード */
	public String organizationCode;
	/** 基準日 */
	public java.sql.Date baseDate;
	/** 有効データのみ表示 */
	public Boolean displayValidOnly;
}
