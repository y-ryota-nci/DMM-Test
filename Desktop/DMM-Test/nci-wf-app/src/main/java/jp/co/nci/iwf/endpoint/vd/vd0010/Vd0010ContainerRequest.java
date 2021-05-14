package jp.co.nci.iwf.endpoint.vd.vd0010;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * コンテナID単位で処理する際のリクエスト
 */
public class Vd0010ContainerRequest extends BaseRequest {

	public List<Long> containerIds;
}
