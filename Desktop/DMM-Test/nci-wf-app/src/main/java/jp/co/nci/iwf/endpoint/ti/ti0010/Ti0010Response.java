package jp.co.nci.iwf.endpoint.ti.ti0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * マスタ取込設定画面の初期化レスポンス
 */
public class Ti0010Response extends BaseResponse {
	/** カテゴリ一覧 */
	public List<Ti0010Category> categories;
	/** 全テーブル一覧 */
	public List<Ti0010Table> allTables;
	/** 選択中のカテゴリID */
	public Long categoryId;
}
