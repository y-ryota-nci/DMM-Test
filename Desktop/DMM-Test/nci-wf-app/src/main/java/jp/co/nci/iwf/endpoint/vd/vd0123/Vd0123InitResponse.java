package jp.co.nci.iwf.endpoint.vd.vd0123;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 外部Javascript参照設定の初期化リクエスト
 */
public class Vd0123InitResponse extends BaseResponse {

	public List<Vd0123Entity> scripts;

}
