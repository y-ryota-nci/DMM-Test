package jp.co.nci.iwf.endpoint.vd.vd0060;

import java.util.Set;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 外部Javascript一覧の削除リクエスト
 */
public class Vd0060DeleteRequest extends BaseRequest {
	/** 削除対象の選択肢ID一覧 */
	public Set<Long> optionIds;
}
