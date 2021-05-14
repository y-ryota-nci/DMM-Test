package jp.co.nci.iwf.endpoint.mm.mm0101;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 情報共有者定義作成の初期化レスポンス
 */
public class Mm0101InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<OptionItem> informationSharerTypes;

}