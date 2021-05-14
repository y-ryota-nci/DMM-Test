package jp.co.nci.iwf.endpoint.mm.mm0003;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 【プロファイル管理】ユーザ編集画面の初期化リクエスト
 */
public class Mm0003InitRequest extends BaseRequest {
	/** 企業コード */
	public String corporationCode;
	/** ユーザコード */
	public String userCode;
	/** 基準日 */
	public Date baseDate;
	/** 最終更新日時 */
	public Long timestampUpdated;
}
