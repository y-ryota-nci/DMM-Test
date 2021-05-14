package jp.co.dmm.customize.endpoint.bd.bd0802;

/**
 * 予算計画履歴作成画面の履歴作成リクエスト
 */
public class Bd0802CreateRequest extends Bd0802InitRequest {
	/** 会社CD */
	public String companyCd;
	/** 履歴名称 */
	public String hstNm;
	/** コメント */
	public String hstRmk;
}
