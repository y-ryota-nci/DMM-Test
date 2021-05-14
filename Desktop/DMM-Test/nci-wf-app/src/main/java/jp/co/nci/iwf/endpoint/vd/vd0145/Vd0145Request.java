package jp.co.nci.iwf.endpoint.vd.vd0145;

import java.util.List;

import jp.co.nci.iwf.designer.parts.PartsCalcEc;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Vd0145Request extends BaseRequest {

//	/** パーツ計算式ID */
//	public Long partsEcId;
//	/** バージョン */
//	public Long version;
	/** パーツ計算条件一覧 */
	public List<PartsCalcEc> ecs;
}
