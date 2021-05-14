package jp.co.dmm.customize.component.dowjones;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

public class DowjonesResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** レコード総数 */
	public int totalHits;
	public List<DowjonesEntity> matchs;

}
