package jp.co.nci.iwf.endpoint.dc.dc0110;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面文書定義一覧の削除リクエスト
 */
public class Dc0110DeleteRequest extends BaseRequest {

	/** */
	private static final long serialVersionUID = 1L;

	public List<Long> screenDocIds;

}