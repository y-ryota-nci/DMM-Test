package jp.co.nci.iwf.endpoint.mm.mm0020;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 表示条件設定での更新リクエスト
 */
public class Mm0020SaveRequest extends BaseRequest {
	/** コンテナID */
	public long containerId;
	/** 表示条件マスタリスト */
	public List<Mm0020Dc> dcList;
	/** パーツ表示条件リスト */
	public List<Mm0020PartsDc> partsDcList;
}
