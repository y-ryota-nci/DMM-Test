package jp.co.nci.iwf.jersey.base;

/**
 * ページ制御する場合のリクエスト基底クラス
 */
public class BasePagingRequest extends BaseRequest {

	/** ページあたりの行数 */
	public Integer pageSize;
	/** ページ番号 */
	public Integer pageNo;
	/** ソートカラム */
	public String sortColumn;
	/** 降順でソートか */
	public boolean sortAsc;
}
