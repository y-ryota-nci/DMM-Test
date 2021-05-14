package jp.co.nci.iwf.endpoint.ml.ml0030;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;

/**
 * メールテンプレートファイル設定の保存リクエスト
 */
public class Ml0030SaveRequest extends BaseRequest {
	public MwmMailTemplateFile file;
}
