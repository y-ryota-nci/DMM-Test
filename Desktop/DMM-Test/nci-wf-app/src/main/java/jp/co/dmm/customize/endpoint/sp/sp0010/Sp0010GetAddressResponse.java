package jp.co.dmm.customize.endpoint.sp.sp0010;

import java.util.List;

import jp.co.dmm.customize.endpoint.sp.ZipMstEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 住所取得結果レスポンス
 */
public class Sp0010GetAddressResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** 取得結果 */
	public List<ZipMstEntity> results;
}
