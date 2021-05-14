package jp.co.dmm.customize.endpoint.dc.dc1000;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * OCR状況一覧の検索リクエスト
 */
public class Dc1000Request extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** OCR対象フラグ */
	public String ocrFlag;
	/** OCR実行日From */
	public Date ocrExecutionDateFrom;
	/** OCR実行日To */
	public Date ocrExecutionDateTo;

}
