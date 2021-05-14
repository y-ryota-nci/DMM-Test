package jp.co.nci.iwf.endpoint.mm.mm0060;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ブロック表示順設定での更新リクエスト
 */
public class Mm0060SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** コンテナID */
	public long screenProcessId;
	/** 表示条件マスタリスト */
	public List<Mm0060Dc> dcs;
	/** ブロック表示順リスト */
	public List<Mm0060BlockDisplay> blockDisplays;

}