package jp.co.nci.iwf.endpoint.vd.vd0040;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面プロセス定義一覧の削除リクエスト
 */
public class Vd0040DeleteRequest extends BaseRequest {
	public List<Long> screenProcessIds;
}
