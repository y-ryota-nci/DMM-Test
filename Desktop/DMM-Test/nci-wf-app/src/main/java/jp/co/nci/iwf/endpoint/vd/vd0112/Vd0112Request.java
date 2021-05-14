package jp.co.nci.iwf.endpoint.vd.vd0112;

import java.util.List;

import jp.co.nci.iwf.designer.parts.PartsCondItem;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Vd0112Request extends BaseRequest {

	/** コンテナID */
	public String containerId;
	/** パーツID */
	public String partsId;
	/** パーツ条件項目一覧 */
	public List<PartsCondItem> items;
}
