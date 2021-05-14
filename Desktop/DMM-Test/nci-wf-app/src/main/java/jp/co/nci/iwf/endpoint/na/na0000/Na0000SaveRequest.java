package jp.co.nci.iwf.endpoint.na.na0000;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 新規申請フォルダ設定画面の保存（追加、編集、削除）リクエスト
 */
public class Na0000SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long screenProcessLevelId;
	public String parentLevelCode;
	public String levelCode;
	public String levelName;
	public String expansionFlag;
	public Integer sortOrder;

	public Long screenProcessId;

}
