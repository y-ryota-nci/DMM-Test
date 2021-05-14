package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.sql.Date;
import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プロファイル管理の初期化レスポンス
 */
public class Mm0000InitResponse extends BaseResponse {
	/** 基準日 */
	public Date baseDate;
	/** 有効データのみ表示 */
	public Boolean displayValidOnly;
	/** 初期表示するノード */
	public List<String> nodeIds;
}
