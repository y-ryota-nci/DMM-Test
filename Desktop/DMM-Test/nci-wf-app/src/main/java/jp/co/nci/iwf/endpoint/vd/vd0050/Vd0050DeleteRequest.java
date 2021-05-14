package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 外部Javascript一覧の削除リクエスト
 */
public class Vd0050DeleteRequest extends BaseRequest {

	public Set<Long> javascriptIds;
}
