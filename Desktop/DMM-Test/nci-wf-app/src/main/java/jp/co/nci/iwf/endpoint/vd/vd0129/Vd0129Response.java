package jp.co.nci.iwf.endpoint.vd.vd0129;

import java.util.List;
import java.util.Set;

import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * DBカラム名一覧レスポンス
 */
public class Vd0129Response extends BaseResponse {

	public List<PartsColumn> columns;
	public boolean existRecord;
	/** DB予約語リスト */
	public Set<String> dbmsReservedColNames;
	/** 画面デザイナー予約語リスト */
	public Set<String> designerReservedColNames;

}
