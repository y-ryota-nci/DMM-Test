package jp.co.dmm.customize.endpoint.mg.mg0281;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * メディアマスタ更新リクエスト
 *
 */
public class Mg0281UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** メディアID */
	public String mdaId;
	/** メディア名称 */
	public String mdaNm;
	/** 削除フラグ */
	public String dltFg;
}
