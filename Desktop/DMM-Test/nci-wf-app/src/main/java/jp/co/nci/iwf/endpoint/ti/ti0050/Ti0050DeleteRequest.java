package jp.co.nci.iwf.endpoint.ti.ti0050;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 汎用テーブル検索条件一覧の削除リクエスト
 */
public class Ti0050DeleteRequest extends BaseRequest {

	public List<Long> tableSearchIds;
}
