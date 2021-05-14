package jp.co.nci.iwf.endpoint.al.al0011;

import java.util.List;

import jp.co.nci.iwf.endpoint.al.al0010.Al0010Entity;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwtAccessLogDetail;

/**
 * アクセスログ詳細の初期化レスポンス
 */
public class Al0011InitResponse extends BaseResponse {
	public Al0010Entity accessLog;
	public List<MwtAccessLogDetail> details;
}
