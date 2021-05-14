package jp.co.nci.iwf.jersey.base;

/**
 * Endpointインターフェース
 *
 * @param <REQ>
 */
public interface IEndpoint<REQ extends IRequest> {

	/**
	 * Endpointが必ず持つべき画面の初期化リクエスト。これによって画面のアクセスログが書き込まれる
	 * @param req
	 * @return
	 */
	public BaseResponse init(REQ req);
}
