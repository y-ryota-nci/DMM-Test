package jp.co.nci.iwf.endpoint.ti.ti0011;

import java.util.List;

import jp.co.nci.iwf.endpoint.ti.ti0010.Ti0010Category;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * カテゴリ編集画面の保存リクエスト
 */
public class Ti0011SaveRequest extends BaseRequest {

	public List<Ti0010Category> inputs;
}
