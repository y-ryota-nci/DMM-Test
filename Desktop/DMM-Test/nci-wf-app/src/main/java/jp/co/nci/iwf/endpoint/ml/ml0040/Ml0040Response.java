package jp.co.nci.iwf.endpoint.ml.ml0040;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailVariable;

/**
 * メール変数設定レスポンス
 */
public class Ml0040Response extends BaseResponse {

	public List<MwmMailVariable> results;

}
