package jp.co.dmm.customize.endpoint.po.po0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 発注ステータスの更新リクエスト
 */
public class Po0010SaveRequest extends BasePagingRequest {
	/** 発注Noリスト */
	public List<String> purordNoList;
}
