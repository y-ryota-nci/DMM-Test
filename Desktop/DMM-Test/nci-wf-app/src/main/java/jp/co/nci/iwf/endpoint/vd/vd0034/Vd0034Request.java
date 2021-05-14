package jp.co.nci.iwf.endpoint.vd.vd0034;

import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.designer.parts.PartsCalc;
import jp.co.nci.iwf.designer.parts.PartsCond;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/** 画面属性コピー設定画面のリクエスト */
public class Vd0034Request extends BaseRequest {
	/** コピー先画面ID */
	public Long destScreenId;
	/** コピー先コンテナID */
	public Long destContainerId;

	/** コピー元画面の画面ID */
	public Long srcScreenId;
	/** コピー元のパーツ */
	public List<Vd0034Entity> srcParts;

	/** コピー先画面のパーツ条件Map */
	public Map<Long, List<PartsCond>> destCondMap;
	/** コピー先画面の計算式Map */
	public Map<Long, List<PartsCalc>> destCalcMap;
}
