package jp.co.nci.iwf.endpoint.vd.vd0144;

import java.util.List;

import jp.co.nci.iwf.designer.parts.PartsCalcItem;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Vd0144Request extends BaseRequest {

//	/** パーツ計算式ID */
//	public Long partsEcId;
//	/** バージョン */
//	public Long version;
	/** パーツ計算式名 */
	public String partsCalcName;
	/** パーツ計算式項目一覧 */
	public List<PartsCalcItem> items;

}
