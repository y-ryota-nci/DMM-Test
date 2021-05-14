package jp.co.nci.iwf.endpoint.mm.mm0110;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * システムプロパティ編集画面の保存リクエスト
 */
public class Mm0110SaveRequest extends BaseRequest {
	public String corporationCode;
	public List<Mm0110Entity> inputs;
}
