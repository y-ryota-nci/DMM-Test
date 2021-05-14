package jp.co.nci.iwf.endpoint.dc.dc0100;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 業務文書の登録・更新画面の初期化レスポンス
 */
public class Dc0100InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public Dc0100Contents contents;

	/** パーツとしてレンダリングされたHTML */
	public String html;

}
