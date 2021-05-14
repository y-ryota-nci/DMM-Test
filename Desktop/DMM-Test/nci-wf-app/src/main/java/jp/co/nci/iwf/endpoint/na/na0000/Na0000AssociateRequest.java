package jp.co.nci.iwf.endpoint.na.na0000;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;

/**
 * 新規申請フォルダ設定画面の紐付けリクエスト
 */
public class Na0000AssociateRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String parentLevelCode;
	public List<MwmScreenProcessDef> defs;

}
