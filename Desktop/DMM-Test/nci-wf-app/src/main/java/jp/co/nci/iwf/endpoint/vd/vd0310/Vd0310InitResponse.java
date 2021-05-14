package jp.co.nci.iwf.endpoint.vd.vd0310;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 申請・承認画面の初期化レスポンス
 */
public class Vd0310InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public Vd0310Contents contents;

	/** パーツとしてレンダリングされたHTML */
	public String html;

	/** コメント欄を使用するか */
	public boolean useActionComment;

}
