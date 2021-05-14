package jp.co.nci.iwf.endpoint.na.na0000;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessLevel;

/**
 * 新規申請フォルダ設定画面の移動リクエスト
 */
public class Na0000MoveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<MwmScreenProcessLevel> levels;
	public List<MwmScreenProcessDef> defs;

}
