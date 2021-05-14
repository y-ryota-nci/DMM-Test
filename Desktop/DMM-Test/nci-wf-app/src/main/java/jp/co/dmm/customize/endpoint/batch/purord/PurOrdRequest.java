package jp.co.dmm.customize.endpoint.batch.purord;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 定期発注リクエスト
 */
public class PurOrdRequest extends BasePagingRequest {

	/** バッチ実施日付 */
	public Date processingDate;

	/** 会社コードリスト */
	public List<String> companyCdList;

	/** 会社別発注申請プロセス定義コード */
	public Map<String, String[]> targetOrderMap;

	/** データ */
	public PurOrdData data;
}
