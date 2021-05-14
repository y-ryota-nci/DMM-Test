package jp.co.nci.iwf.endpoint.dc.dc0120;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 業務文書メニュー編集画面の保存（追加、編集、削除）リクエスト
 */
public class Dc0120SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long screenDocLevelId;
	public String parentLevelCode;
	public String levelCode;
	public String levelName;
	public String expansionFlag;
	public Integer sortOrder;

	public Long screenDocId;

}
