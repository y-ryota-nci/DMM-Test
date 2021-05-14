package jp.co.nci.iwf.endpoint.ti.ti0011;

import java.util.List;

import jp.co.nci.iwf.endpoint.ti.ti0010.Ti0010Category;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * カテゴリ編集画面の初期化レスポンス
 */
public class Ti0011Response extends BaseResponse {

	public List<Ti0010Category> categories;
}
