package jp.co.nci.iwf.jersey.base;

import java.util.List;

/**
 * ページ制御する場合のレスポンス基底クラス
 */
public class BasePagingResponse extends BaseResponse {

	/** ページ番号 */
	public int pageNo;
	/** 検索結果 */
	public List<?> results;
	/** 検索結果総件数 */
	public int allCount;
	/** 検索結果総ページ数 */
	public int pageCount;
	/** 対象ページの開始行番号（1スタート） */
	public int start;
	/** 対象ページの終了行番号（1スタート） */
	public int end;
}
