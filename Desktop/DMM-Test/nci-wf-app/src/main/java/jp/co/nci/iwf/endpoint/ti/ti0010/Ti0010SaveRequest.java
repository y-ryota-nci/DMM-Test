package jp.co.nci.iwf.endpoint.ti.ti0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * マスタ取込設定画面の保存リクエスト
 */
public class Ti0010SaveRequest extends BaseRequest {
	public String entityType;
	public Long categoryId;
	public List<Ti0010Table> inputs;
}
